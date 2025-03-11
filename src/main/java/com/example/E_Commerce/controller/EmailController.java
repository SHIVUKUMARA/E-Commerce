package com.example.E_Commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Commerce.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String message) {
        try {
            emailService.sendEmail(to, subject, message);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send email: " + e.getMessage());
        }
    }
}
