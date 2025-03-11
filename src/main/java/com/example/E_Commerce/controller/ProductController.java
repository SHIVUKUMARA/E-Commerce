package com.example.E_Commerce.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Commerce.dto.ProductRequest;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.Role;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.security.JwtUtil;
import com.example.E_Commerce.service.ProductService;
import com.example.E_Commerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/store/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/add/{storeId}")
    public ResponseEntity<?> addProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId,
            @RequestBody ProductRequest productRequest) {

        // Extract vendor details from token
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);

        // Confirm user is a vendor
        if (user.getRole() != Role.VENDOR) {
            return ResponseEntity.status(403).body("Only vendors can add products.");
        }

        return productService.addProduct(storeId,productRequest, user);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getProductsByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }

    @GetMapping("/vendor")
    public ResponseEntity<?> getVendorProducts(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);

        if (user.getRole() != Role.VENDOR) {
            return ResponseEntity.status(403).body("Only vendors can view their products.");
        }

        return ResponseEntity.ok(productService.getProductsByVendor(user));
    }
    
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId,
            @RequestBody ProductRequest productRequest) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);

        if (user.getRole() != Role.VENDOR) {
            return ResponseEntity.status(403).body("Only vendors can update products.");
        }

        return productService.updateProduct(productId, productRequest, user);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User user = userService.findByEmail(email);

        if (user.getRole() != Role.VENDOR) {
            return ResponseEntity.status(403).body("Only vendors can delete products.");
        }

        return productService.deleteProduct(productId, user);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> products = productService.getFilteredProducts(productName, storeName, category, sort, page, size);
        return ResponseEntity.ok(products);
    }
}
