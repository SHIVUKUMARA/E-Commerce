package com.example.E_Commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.E_Commerce.service.CloudinaryService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // Upload through Multipart File
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    // Upload through Image URL
    @PostMapping("/upload-url")
    public ResponseEntity<String> uploadImageFromUrl(@RequestParam("imageUrl") String imageUrl) {
        String uploadedImageUrl = cloudinaryService.uploadImageFromUrl(imageUrl);
        return ResponseEntity.ok(uploadedImageUrl);
    }
}
