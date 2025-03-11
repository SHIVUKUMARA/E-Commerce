package com.example.E_Commerce.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}

