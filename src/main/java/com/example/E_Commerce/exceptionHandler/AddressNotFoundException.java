package com.example.E_Commerce.exceptionHandler;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}

