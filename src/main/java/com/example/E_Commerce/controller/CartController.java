package com.example.E_Commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Commerce.dto.CartResponse;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.UserRepository;
import com.example.E_Commerce.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final UserRepository userRepository;

	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
		// Getting the authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User customer = userRepository.findByEmail(email).orElse(null);

		if (customer == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		// Adding item to cart
		String message = cartService.addItemToCart(customer.getId(), productId, quantity);
		return ResponseEntity.ok(message);
	}

	@GetMapping
	public ResponseEntity<?> getCart() {
		// Get the authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User customer = userRepository.findByEmail(email).orElse(null);

		if (customer == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		// Retrieve cart details
		CartResponse cartResponse = cartService.getCustomerCart(customer.getId());
		if (cartResponse == null) {
			return ResponseEntity.ok("Cart is empty");
		}
		return ResponseEntity.ok(cartResponse);
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<String> removeFromCart(@RequestParam Long productId) {
	    // Get authenticated user
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User customer = userRepository.findByEmail(email).orElse(null);

	    if (customer == null) {
	        return ResponseEntity.badRequest().body("User not found");
	    }

	    // Remove item from cart
	    String message = cartService.removeItemFromCart(customer.getId(), productId);
	    return ResponseEntity.ok(message);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateCartItem(@RequestParam Long productId, @RequestParam int newQuantity) {
	    // Get authenticated user
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User customer = userRepository.findByEmail(email).orElse(null);

	    if (customer == null) {
	        return ResponseEntity.badRequest().body("User not found");
	    }

	    // Update cart item
	    String message = cartService.updateCartItem(customer.getId(), productId, newQuantity);
	    return ResponseEntity.ok(message);
	}
}