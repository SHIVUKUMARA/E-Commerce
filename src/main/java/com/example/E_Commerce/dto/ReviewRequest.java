package com.example.E_Commerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private double rating; 
    private String reviewText;
}
