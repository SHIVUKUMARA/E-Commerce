package com.example.E_Commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Commerce.entity.Address;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.exceptionHandler.UserNotFoundException;
import com.example.E_Commerce.repository.AddressRepository;
import com.example.E_Commerce.repository.UserRepository;
import com.example.E_Commerce.service.PaymentService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @GetMapping("/checkout")
    public ResponseEntity<String> checkout() {
        User customer = getAuthenticatedUser();
        try {
            String checkoutUrl = paymentService.createCheckoutSession(customer.getId());
            return ResponseEntity.ok(checkoutUrl);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam(value = "session_id") String sessionId) {
    	if (sessionId == null || sessionId.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: session_id is missing!");
        }

    	User customer = getAuthenticatedUser();
        try {
            String message = paymentService.handlePaymentSuccess(customer.getId(), sessionId);
            return ResponseEntity.ok(message);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/latest-address")
    public ResponseEntity<?> getLatestAddress() {
        User customer = getAuthenticatedUser();
        Address latestAddress = addressRepository.findLatestAddressByCustomerId(customer.getId())
                .orElse(null); 
        return ResponseEntity.ok(latestAddress);
    }
}
