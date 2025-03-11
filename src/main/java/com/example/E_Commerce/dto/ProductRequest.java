package com.example.E_Commerce.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private String name;
	private String description;
	private String image;
	private BigDecimal price;
	private int availableStock;
	private String category;
	private Long storeId;
}
