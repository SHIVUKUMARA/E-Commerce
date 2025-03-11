package com.example.E_Commerce.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.E_Commerce.entity.*;
import com.example.E_Commerce.exceptionHandler.PaymentProcessingException;
import com.example.E_Commerce.repository.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final EmailService emailService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createCheckoutSession(Long customerId) throws StripeException {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new PaymentProcessingException("Cart is empty");
        }

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            lineItems.add(SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(item.getProduct().getPrice().multiply(BigDecimal.valueOf(100)).longValue()) // Convert to cents
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getProduct().getName())
                                    .build())
                            .build())
                    .build());
        }

        String finalSuccessUrl = successUrl + "?session_id={CHECKOUT_SESSION_ID}";

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(finalSuccessUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    @Transactional
    public String handlePaymentSuccess(Long customerId, String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        if (session == null || !"paid".equals(session.getPaymentStatus())) {
            throw new PaymentProcessingException("Invalid or unpaid Stripe session.");
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new PaymentProcessingException("Cart is empty or already processed.");
        }

        Address address = addressRepository.findLatestAddressByCustomerId(customerId)
                .orElseThrow(() -> new PaymentProcessingException("No address found for the user."));

        // Creating a new order
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PROCESSING"); 
        order.setAddress(address);
        orderRepository.save(order);

        // Save order items to DB
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        // Generating 6-digit OTP
        String otp = String.format("%06d", new SecureRandom().nextInt(1000000));
        LocalDateTime otpExpiry = LocalDateTime.now().plusDays(10); 

        order.setOtp(otp);
        order.setOtpExpiry(otpExpiry);
        orderRepository.save(order);

        // Storing transaction ID
        String paymentIntentId = session.getPaymentIntent();
        String transactionId = (paymentIntentId != null) ? paymentIntentId : session.getId();

        Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        payment.setCustomer(cart.getCustomer());
        payment.setAmount(cart.getTotalAmount());
        payment.setCurrency("INR");
        payment.setPaymentDate(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        // Clear cart after payment successfull
        cartItemRepository.deleteAll(cart.getItems());
        cartRepository.delete(cart);

        // Send OTP in email to confirm or cancel order
        String emailContent = "<h2>Order Confirmation OTP</h2>" +
                "<p>Your order is being processed. To confirm or cancel your order, use the OTP:</p>" +
                "<h3 style='color: blue;'>" + otp + "</h3>" +
                "<p>Valid for 10 days.</p>";

        emailService.sendEmail(cart.getCustomer().getEmail(), "Order OTP - Order #" + order.getId(), emailContent);

        return "Payment successful. Order ID: " + order.getId() + ". OTP sent to your email.";
    }
}
