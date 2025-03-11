package com.example.E_Commerce.service;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.E_Commerce.config.AppConfig;
import com.example.E_Commerce.dto.UpdateUserRequest;
import com.example.E_Commerce.dto.UserDto;
import com.example.E_Commerce.entity.Role;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.exceptionHandler.UserNotFoundException;
import com.example.E_Commerce.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AppConfig appConfig;
	private final EmailService emailService;
	private final CloudinaryService cloudinaryService;

	public User getUserFromPrincipal(Principal principal) {
		return userRepository.findByEmail(principal.getName())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User registerUser(String name, String email, String password, Role role, MultipartFile profileImageFile,
			String profileImageUrl) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("User already exists with this email.");
		}

		String profileImage;

        // If file is provided, upload it
		if (profileImageFile != null && !profileImageFile.isEmpty()) {
			profileImage = cloudinaryService.uploadImage(profileImageFile);
		}
        // If image URL is provided, store it
		else if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
			profileImage = profileImageUrl; // Store the URL directly
		}
        // Default profile image (First letter of name)
		else {
			profileImage = String.valueOf(Character.toUpperCase(name.charAt(0)));
		}

		User user = User.builder().name(name).email(email).password(passwordEncoder.encode(password)).role(role)
				.profileImage(profileImage).build();

		User savedUser = userRepository.save(user);

		sendEmailSafely(email, "Welcome to Our Platform!",
				"<h1>Welcome " + name + "!</h1> <p>Your account has been created successfully.</p>");

		return savedUser;
	}

	public User authenticateUser(String email, String password) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Invalid email or password."));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Invalid email or password.");
		}

		sendEmailSafely(user.getEmail(), "Login Alert!", "<h1>Hello " + user.getName()
				+ "!</h1> <p>You have logged in successfully. If this wasn't you, please change your password immediately.</p>");

		return user;
	}

	@PostConstruct
	public void createAdminUser() {
		if (userRepository.findByEmail(appConfig.getAdminEmail()).isEmpty()) {
			String profileImage = String.valueOf(Character.toUpperCase(appConfig.getAdminName().charAt(0)));

			User admin = User.builder().name(appConfig.getAdminName()).email(appConfig.getAdminEmail())
					.password(passwordEncoder.encode(appConfig.getAdminPassword())).role(Role.ADMIN)
					.profileImage(profileImage).build();

			userRepository.save(admin);
		}
	}

	@Transactional
	public User updateUser(String email, UpdateUserRequest updateUserRequest) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));

		if (updateUserRequest.getName() != null) {
			user.setName(updateUserRequest.getName());
		}
		if (updateUserRequest.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
		}
		if (updateUserRequest.getRole() != null) {
			user.setRole(updateUserRequest.getRole());
		}

		// Profile Image Replacement Logic
		if (updateUserRequest.getProfileImageFile() != null && !updateUserRequest.getProfileImageFile().isEmpty()) {
			// Upload new image and replace the old one
			String uploadedImageUrl = cloudinaryService.uploadImage(updateUserRequest.getProfileImageFile());
			user.setProfileImage(uploadedImageUrl);
		} else if (updateUserRequest.getProfileImageUrl() != null
				&& !updateUserRequest.getProfileImageUrl().isEmpty()) {
			// If a URL is provided, replace the old image
			user.setProfileImage(updateUserRequest.getProfileImageUrl());
		}

		return userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	}

	private void sendEmailSafely(String toEmail, String subject, String message) {
		try {
			emailService.sendEmail(toEmail, subject, message);
		} catch (Exception e) {
			System.err.println("Failed to send email: " + e.getMessage());
		}
	}

	private String getProfileImage(MultipartFile file, String imageUrl, String name) {
		if (file != null && !file.isEmpty()) {
			return cloudinaryService.uploadImage(file); // Upload file
		} else if (imageUrl != null && !imageUrl.isEmpty()) {
			return imageUrl;
		} else {
			return String.valueOf(Character.toUpperCase(name.charAt(0))); // Default profile image
		}
	}
}
