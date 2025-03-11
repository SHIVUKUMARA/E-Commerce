package com.example.E_Commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
}
