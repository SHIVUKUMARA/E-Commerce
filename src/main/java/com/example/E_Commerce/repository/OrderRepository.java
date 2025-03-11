package com.example.E_Commerce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Commerce.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);    
    List<Order> findByStatusAndOtpExpiryBefore(String status, LocalDateTime expiryDate);
}
