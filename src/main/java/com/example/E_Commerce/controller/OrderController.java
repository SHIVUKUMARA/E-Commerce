package com.example.E_Commerce.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.E_Commerce.entity.Order;
import com.example.E_Commerce.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Order> orders = orderService.getAllOrdersForCustomer(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Order> orders = orderService.getOrdersByStatus(email, status);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId, @RequestParam String otp) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId, otp));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId, @RequestParam String otp) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, otp));
    }
}
