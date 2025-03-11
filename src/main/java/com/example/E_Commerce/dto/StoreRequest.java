package com.example.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreRequest {
    private String name;
    private String description;
    private String profileImage;
    private String category;
    private String phoneNumber;
    private String address;
    private String pincode;
}
