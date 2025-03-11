package com.example.E_Commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.E_Commerce.entity.Role;
import com.example.E_Commerce.entity.StoreStatus;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.security.JwtUtil;
import com.example.E_Commerce.service.StoreService;
import com.example.E_Commerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private User getUserFromToken(String token) {
        return userService.findByEmail(jwtUtil.extractEmail(token.replace("Bearer ", "")));
    }

    private ResponseEntity<String> checkUserRole(User user, Role requiredRole, String message) {
        return user.getRole() == requiredRole ? null : ResponseEntity.status(403).body(message);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createStore(
            @RequestHeader("Authorization") String token,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "profileImageUrl", required = false) String profileImageUrl,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            @RequestParam("category") String category,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("pincode") String pincode) {

        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.VENDOR, "Only vendors can create stores.");
        if (roleCheck != null) return roleCheck;

        return storeService.createStore(name, description, profileImageUrl, profileImageFile, category, phoneNumber, address, pincode, user);
    }
 
    @PostMapping("/approve-reject/{storeId}")
    public ResponseEntity<?> updateStoreStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId,
            @RequestParam boolean approve,
            @RequestParam(required = false) String rejectionReason) {

        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.ADMIN, "Only admins can update store status.");
        return roleCheck != null ? roleCheck : storeService.updateStoreStatus(storeId, approve ? StoreStatus.APPROVED : StoreStatus.REJECTED, rejectionReason);
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<?> getPendingStoreRequests(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.ADMIN, "Only admins can view pending store requests.");
        return roleCheck != null ? roleCheck : ResponseEntity.ok(storeService.getPendingStores());
    }

    @PutMapping("/reject/{storeId}")
    public ResponseEntity<?> rejectApprovedStore(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId,
            @RequestParam String rejectionReason) {

        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.ADMIN, "Only admins can reject stores.");
        return roleCheck != null ? roleCheck : storeService.rejectStore(storeId, rejectionReason);
    } 

    @GetMapping("/admin/stores")
    public ResponseEntity<?> getStoresByStatus(@RequestHeader("Authorization") String token, @RequestParam(required = false) String status) {
        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.ADMIN, "Only admins can view stores by status.");
        if (roleCheck != null) return roleCheck;

        try {
            StoreStatus storeStatus = status != null ? StoreStatus.valueOf(status.toUpperCase()) : null;
            return ResponseEntity.ok(storeService.getStoresByStatus(storeStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store status: " + status);
        }
    }

    @GetMapping("/vendor/stores")
    public ResponseEntity<?> getVendorStores(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        ResponseEntity<String> roleCheck = checkUserRole(user, Role.VENDOR, "Only vendors can view their own stores.");
        return roleCheck != null ? roleCheck : ResponseEntity.ok(storeService.getStoresByVendor(user));
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }
}
