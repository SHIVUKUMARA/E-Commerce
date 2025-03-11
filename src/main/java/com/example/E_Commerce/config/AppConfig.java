package com.example.E_Commerce.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final String adminName;
    private final String adminEmail;
    private final String adminPassword;

    public AppConfig(
        @Value("${admin.name}") String adminName,
        @Value("${admin.email}") String adminEmail,
        @Value("${admin.password}") String adminPassword) {
        
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}