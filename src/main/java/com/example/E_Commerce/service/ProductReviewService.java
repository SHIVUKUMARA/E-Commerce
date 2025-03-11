package com.example.E_Commerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.E_Commerce.entity.Order;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.ProductReview;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.exceptionHandler.ResourceNotFoundException;
import com.example.E_Commerce.repository.OrderRepository;
import com.example.E_Commerce.repository.ProductRepository;
import com.example.E_Commerce.repository.ProductReviewRepository;
import com.example.E_Commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ProductReview addReview(String email, Long productId, double rating, String reviewText) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));

        List<Order> orders = orderRepository.findByCustomerId(customer.getId());
        boolean hasOrdered = orders.stream()
                .filter(order -> order.getStatus().equals("DELIVERED") || order.getStatus().equals("CANCELED"))
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getProduct().getId().equals(productId));

        if (!hasOrdered) {
            throw new IllegalStateException("You can only review products from completed or canceled orders.");
        }

        if (productReviewRepository.existsByCustomerIdAndProductId(customer.getId(), productId)) {
            throw new IllegalStateException("You have already reviewed this product.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(rating);
        review.setReview(reviewText);

        ProductReview savedReview = productReviewRepository.save(review);

        String subject = "Thank You for Your Review!";
        String message = "<h3>Hello " + customer.getName() + ",</h3>"
                + "<p>Thank you for reviewing <b>" + product.getName() + "</b>!</p>"
                + "<p><b>Your Review:</b> " + reviewText + "</p>"
                + "<p><b>Your Rating:</b> " + rating + " ⭐</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p><b>Name:</b> " + product.getName() + "</p>"
                + "<p><b>Price:</b> $" + product.getPrice() + "</p>"
                + "<p><b>Status:</b> Reviewed</p>"
                + "<br><p>Best regards,<br><b>E-Commerce Team</b></p>";

        emailService.sendEmail(customer.getEmail(), subject, message);

        return savedReview;
    }

    public void deleteReview(String email, Long reviewId) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        ProductReview review = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalStateException("You can only delete your own reviews.");
        }

        productReviewRepository.delete(review);

        // Send email notification
        String subject = "Your Review Has Been Deleted!";
        String message = "<h3>Hello " + customer.getName() + ",</h3>"
                + "<p>Your review for <b>" + review.getProduct().getName() + "</b> has been deleted.</p>"
                + "<p>We appreciate your feedback!</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p><b>Name:</b> " + review.getProduct().getName() + "</p>"
                + "<p><b>Price:</b> $" + review.getProduct().getPrice() + "</p>"
                + "<p><b>Status:</b> Deleted</p>"
                + "<br><p>Best regards,<br><b>E-Commerce Team</b></p>";

        emailService.sendEmail(customer.getEmail(), subject, message);
    }

    public ProductReview updateReview(String email, Long reviewId, double rating, String reviewText) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        ProductReview review = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalStateException("You can only edit your own reviews.");
        }

        review.setRating(rating);
        review.setReview(reviewText);
        ProductReview updatedReview = productReviewRepository.save(review);

        String subject = "Your Review Has Been Updated!";
        String message = "<h3>Hello " + customer.getName() + ",</h3>"
                + "<p>Your review for <b>" + review.getProduct().getName() + "</b> has been updated.</p>"
                + "<p><b>Updated Review:</b> " + reviewText + "</p>"
                + "<p><b>Updated Rating:</b> " + rating + " ⭐</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p><b>Name:</b> " + review.getProduct().getName() + "</p>"
                + "<p><b>Price:</b> $" + review.getProduct().getPrice() + "</p>"
                + "<p><b>Status:</b> Updated</p>"
                + "<br><p>Best regards,<br><b>E-Commerce Team</b></p>";

        emailService.sendEmail(customer.getEmail(), subject, message);

        return updatedReview;
    }
    
    public Map<String, Object> getProductRatingStats(Long productId) {
        List<ProductReview> reviews = productReviewRepository.findByProductId(productId);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for this product.");
        }

        double averageRating = reviews.stream().mapToDouble(ProductReview::getRating).average().orElse(0.0);
        long totalReviews = reviews.size();

        Map<String, Object> ratingStats = new HashMap<>();
        ratingStats.put("productId", productId);
        ratingStats.put("averageRating", averageRating);
        ratingStats.put("totalReviews", totalReviews);

        return ratingStats;
    }


    public List<ProductReview> getReviewsForProduct(Long productId) {
        return productReviewRepository.findByProductId(productId);
    }
}
