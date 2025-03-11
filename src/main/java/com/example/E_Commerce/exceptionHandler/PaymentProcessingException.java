package com.example.E_Commerce.exceptionHandler;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message) {
        super(message);
    }
}
