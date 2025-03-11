package com.example.E_Commerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String profileImage;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String pincode;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;
    
    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;
}
