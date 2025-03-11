package com.example.E_Commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.E_Commerce.entity.Order;
import com.example.E_Commerce.entity.OrderItem;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.exceptionHandler.ResourceNotFoundException;
import com.example.E_Commerce.repository.OrderRepository;
import com.example.E_Commerce.repository.ProductRepository;
import com.example.E_Commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<Order> getAllOrdersForCustomer(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());

        orders.forEach(order -> order.getOrderItems().size());

        return orders;
    }

    @Transactional
    public List<Order> getOrdersByStatus(String email, String status) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
        List<Order> orders = orderRepository.findByCustomerIdAndStatus(customer.getId(), status);

        // Force loading order items
        orders.forEach(order -> order.getOrderItems().size());

        return orders;
    }
    
    @Transactional
    public String confirmOrder(Long orderId, String otp) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getOtp() == null || LocalDateTime.now().isAfter(order.getOtpExpiry())) {
            return "OTP has expired. Please contact support.";
        }

        if (!order.getOtp().equals(otp)) {
            return "Invalid OTP. Please try again.";
        }

        // Reduce stock for confirmed order
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("CONFIRMED");
        order.setOtp(null);
        order.setOtpExpiry(null);
        orderRepository.save(order);

        return "Your order has been received successfully!";
    }

    @Transactional
    public String cancelOrder(Long orderId, String otp) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getOtp() == null || LocalDateTime.now().isAfter(order.getOtpExpiry())) {
            return "OTP has expired. Please contact support.";
        }

        if (!order.getOtp().equals(otp)) {
            return "Invalid OTP. Please try again.";
        }

        order.setStatus("CANCELED");
        order.setOtp(null);
        order.setOtpExpiry(null);
        orderRepository.save(order);

        return "Your order canceled successfully!";
    }

}
