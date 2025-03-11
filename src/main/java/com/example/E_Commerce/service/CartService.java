package com.example.E_Commerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.E_Commerce.dto.CartItemResponse;
import com.example.E_Commerce.dto.CartResponse;
import com.example.E_Commerce.entity.Cart;
import com.example.E_Commerce.entity.CartItem;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.Role;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.CartItemRepository;
import com.example.E_Commerce.repository.CartRepository;
import com.example.E_Commerce.repository.ProductRepository;
import com.example.E_Commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CartResponse getCustomerCart(Long customerId) {
        User customer = userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return new CartResponse(new ArrayList<>(), BigDecimal.ZERO);
        }

        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> 
            new CartItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getDescription(),
                item.getProduct().getImage(),
                item.getProduct().getPrice(),
                item.getProduct().getAvailableStock(),
                item.getProduct().getCategory(),
                item.getProduct().getVendor() != null ? item.getProduct().getVendor().getId() : null,
                item.getProduct().getStore() != null ? item.getProduct().getStore().getId() : null,
                item.getQuantity(),
                item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            )
        ).collect(Collectors.toList());

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(itemResponses, totalAmount);
    }

    @Transactional
    public String addItemToCart(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) {
            return "Quantity must be greater than zero";
        }

        User customer = userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return "Customer not found";
        }

        if (!customer.getRole().equals(Role.CUSTOMER)) {
            return "Only customers can add items to cart";
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return "Product not found";
        }

        if (product.getAvailableStock() < quantity) {
            return "Not enough stock available";
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setItems(new ArrayList<>());
            cart.setTotalAmount(BigDecimal.ZERO);
        }

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
        }

        cart.updateTotalAmount();
        cartRepository.save(cart);

        return "Item added to cart successfully";
    }
    
    @Transactional
    public String removeItemFromCart(Long customerId, Long productId) {
        User customer = userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return "Customer not found";
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            return "Cart is empty";
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            return "Item not found in cart";
        }

       cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cart.updateTotalAmount();
        cartRepository.save(cart);

        return "Item removed from cart successfully";
    }

    @Transactional
    public String updateCartItem(Long customerId, Long productId, int newQuantity) {
        if (newQuantity < 0) {
            return "Quantity cannot be negative";
        }

        User customer = userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return "Customer not found";
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            return "Cart is empty";
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            return "Item not found in cart";
        }

        if (newQuantity == 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            if (cartItem.getProduct().getAvailableStock() < newQuantity) {
                return "Not enough stock available";
            }

            cartItem.setQuantity(newQuantity);
        }

        // Update total amount
        cart.updateTotalAmount();
        cartRepository.save(cart);

        return "Cart item updated successfully";
    }
}
