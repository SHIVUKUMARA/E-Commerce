package com.example.E_Commerce.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private int availableStock;
    private String category;
    private Long vendorId;
    private Long storeId;
    private int quantity;
    private BigDecimal totalPrice;
}
