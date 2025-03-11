package com.example.E_Commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private int availableStock;
    private double ratings;
    private String category;

    @ElementCollection
    private List<String> reviews;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;
}
