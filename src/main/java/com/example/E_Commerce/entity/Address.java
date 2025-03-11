package com.example.E_Commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseNo;
    private String houseName;
    private String street;
    private String area;
    private String city;
    private String state;
    private String nation;
    private String pincode;
    private String phoneNo;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private User customer;
}
