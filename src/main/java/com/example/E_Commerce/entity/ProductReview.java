package com.example.E_Commerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private double rating;
    
    @Column(columnDefinition = "TEXT")
    private String review;
}
