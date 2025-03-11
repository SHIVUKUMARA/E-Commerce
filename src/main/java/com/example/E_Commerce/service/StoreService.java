package com.example.E_Commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.E_Commerce.dto.StoreResponse;
import com.example.E_Commerce.entity.Store;
import com.example.E_Commerce.entity.StoreStatus;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CloudinaryService cloudinaryService; 
    private final EmailService emailService;

    private StoreResponse mapToStoreResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getProfileImage(),
                store.getCategory(),
                store.getPhoneNumber(),
                store.getAddress(),
                store.getPincode(),
                store.getStatus(),
                store.getRejectionReason(),
                store.getVendor().getName()
        );
    }

    @Transactional
    public ResponseEntity<?> createStore(String name, String description, String profileImageUrl, MultipartFile profileImageFile,
                                         String category, String phoneNumber, String address, String pincode, User vendor) {
        if (storeRepository.findByName(name).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Store with this name already exists!");
        }

        if (storeRepository.findByVendorAndAddressAndPincode(vendor, address, pincode).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already have a store at this address and pincode.");
        }

        String profileImage = null;
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                System.out.println("Uploading file: " + profileImageFile.getOriginalFilename()); // Debug
                profileImage = cloudinaryService.uploadImage(profileImageFile);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading profile image.");
            }
        } else if (profileImageUrl != null && !profileImageUrl.trim().isEmpty()) {
            profileImage = profileImageUrl;
        } else {
            profileImage = name.substring(0, 1).toUpperCase();
        }

        Store store = Store.builder()
                .name(name)
                .description(description)
                .profileImage(profileImage)
                .category(category)
                .phoneNumber(phoneNumber)
                .address(address)
                .pincode(pincode)
                .status(StoreStatus.PENDING)
                .vendor(vendor)
                .build();

        Store savedStore = storeRepository.save(store);
        sendStoreCreationEmail(vendor, savedStore);
        return ResponseEntity.ok(mapToStoreResponse(savedStore));
    }

    private void sendStoreCreationEmail(User vendor, Store store) {
        String subject = "Store Created Successfully!";
        String message = "Hello " + vendor.getName() + ",\n\n" +
                "Your store has been successfully created and is now pending approval.\n\n" +
                "Store Name: " + store.getName() + "\n" +
                "Category: " + store.getCategory() + "\n" +
                "Phone: " + store.getPhoneNumber() + "\n" +
                "Address: " + store.getAddress() + "\n" +
                "Pincode: " + store.getPincode() + "\n\n" +
                "We will review your store and notify you once it's approved.\n\n" +
                "Best regards,\nE-Commerce Team";

        emailService.sendEmail(vendor.getEmail(), subject, message);
    }
    
    @Transactional
    public ResponseEntity<?> updateStoreStatus(Long storeId, StoreStatus status, String rejectionReason) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));

        if (status == StoreStatus.REJECTED && (rejectionReason == null || rejectionReason.isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rejection reason is required when rejecting a store.");
        }

        store.setStatus(status);
        store.setRejectionReason(status == StoreStatus.REJECTED ? rejectionReason : null);
        storeRepository.save(store);

        return ResponseEntity.ok("Store status updated successfully to " + status);
    }

    @Transactional
    public ResponseEntity<?> rejectStore(Long storeId, String rejectionReason) {
        return updateStoreStatus(storeId, StoreStatus.REJECTED, rejectionReason);
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getPendingStores() {
        return storeRepository.findByStatus(StoreStatus.PENDING).stream()
                .map(this::mapToStoreResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByStatus(StoreStatus status) {
        List<Store> stores = (status != null) ? storeRepository.findByStatus(status) : storeRepository.findAll();
        return stores.stream().map(this::mapToStoreResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByVendor(User vendor) {
        return storeRepository.findByVendor(vendor).stream()
                .map(this::mapToStoreResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getAllStores() {
        return storeRepository.findByStatus(StoreStatus.APPROVED)
                .stream()
                .map(this::mapToStoreResponse)
                .collect(Collectors.toList());
    }
}
