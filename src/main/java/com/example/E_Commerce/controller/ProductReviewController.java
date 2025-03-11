package com.example.E_Commerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Commerce.dto.ReviewRequest;
import com.example.E_Commerce.entity.ProductReview;
import com.example.E_Commerce.service.ProductReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<ProductReview> addReview(@PathVariable Long productId, @RequestBody ReviewRequest reviewRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(productReviewService.addReview(email, productId, reviewRequest.getRating(), reviewRequest.getReviewText()));
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ProductReview> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(productReviewService.updateReview(email, reviewId, reviewRequest.getRating(), reviewRequest.getReviewText()));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        productReviewService.deleteReview(email, reviewId);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductReview>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(productReviewService.getReviewsForProduct(productId));
    }
    
    @GetMapping("/stats/{productId}")
    public ResponseEntity<Map<String, Object>> getProductRatingStats(@PathVariable Long productId) {
        return ResponseEntity.ok(productReviewService.getProductRatingStats(productId));
    }
}