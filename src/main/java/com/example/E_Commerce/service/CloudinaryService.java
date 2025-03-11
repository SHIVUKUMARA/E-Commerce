package com.example.E_Commerce.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Upload through Multipart File
    public String uploadImage(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    // Upload through Image URL
    public String uploadImageFromUrl(String imageUrl) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Image upload from URL failed", e);
        }
    }
}
