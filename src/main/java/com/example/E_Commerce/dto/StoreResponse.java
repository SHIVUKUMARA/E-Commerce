package com.example.E_Commerce.dto;

import com.example.E_Commerce.entity.StoreStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    private Long id;
    private String name;
    private String description;
    private String profileImage;
    private String category;
    private String phoneNumber;
    private String address;
    private String pincode;
    private StoreStatus status;
    private String rejectionReason;
    private String vendorName;
}
