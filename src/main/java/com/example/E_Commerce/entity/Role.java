package com.example.E_Commerce.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN,
    VENDOR,
    CUSTOMER;

    @JsonCreator
    public static Role fromString(String role) {
        return Role.valueOf(role.trim().toUpperCase());
    }

    @JsonValue
    public String toString() {
        return name();
    }
}
