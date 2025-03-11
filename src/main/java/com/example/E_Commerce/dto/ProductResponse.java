package com.example.E_Commerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private int availableStock;
    private double ratings;
    private String category;
    private List<String> reviews;
    private String storeName;
    private String vendorName;
}
