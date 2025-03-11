package com.example.E_Commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.E_Commerce.dto.LoginRequest;
import com.example.E_Commerce.dto.LoginResponse;
import com.example.E_Commerce.dto.UpdateUserRequest;
import com.example.E_Commerce.entity.Role;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.security.JwtUtil;
import com.example.E_Commerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<String> registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role,
            @RequestParam(required = false) MultipartFile profileImageFile,
            @RequestParam(required = false) String profileImageUrl) {
        
        User user = userService.registerUser(name, email, password, role, profileImageFile, profileImageUrl);
        return ResponseEntity.ok(user.getRole() + " registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new LoginResponse("Login successful!", token));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) MultipartFile profileImageFile,
            @RequestParam(required = false) String profileImageUrl) {
        
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName(name);
        updateUserRequest.setPassword(password);
        updateUserRequest.setRole(role);
        updateUserRequest.setProfileImageFile(profileImageFile);
        updateUserRequest.setProfileImageUrl(profileImageUrl);

        User updatedUser = userService.updateUser(email, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
