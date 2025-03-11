package com.example.E_Commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Commerce.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
} 