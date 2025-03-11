package com.example.E_Commerce.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.E_Commerce.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String profileImageUrl;
    private MultipartFile profileImageFile;
    private String password;
    private Role role;
}
