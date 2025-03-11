package com.example.E_Commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Commerce.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId);
}
