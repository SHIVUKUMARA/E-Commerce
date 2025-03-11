package com.example.E_Commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.E_Commerce.dto.ProductRequest;
import com.example.E_Commerce.dto.ProductResponse;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.Store;
import com.example.E_Commerce.entity.StoreStatus;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.ProductRepository;
import com.example.E_Commerce.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;

	private ProductResponse mapToProductResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getImage(),
				product.getPrice(), product.getAvailableStock(), product.getRatings(), product.getCategory(),
				product.getReviews() != null && !product.getReviews().isEmpty() ? product.getReviews()
						: List.of("No reviews to display"),
				product.getStore().getName(), product.getVendor().getName());
	}

	@Transactional
	public ResponseEntity<?> addProduct(Long storeId, ProductRequest productRequest, User vendor) {
		Store store = storeRepository.findById(storeId).orElse(null);

		if (store == null || !store.getVendor().getId().equals(vendor.getId())) {
			return ResponseEntity.badRequest().body("Invalid store or you don't have permission to add products.");
		}

		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.image(productRequest.getImage()).price(productRequest.getPrice())
				.availableStock(productRequest.getAvailableStock()).ratings(0.0) // Default ratings
				.category(productRequest.getCategory()).store(store).vendor(vendor).build();

		Product savedProduct = productRepository.save(product);
		return ResponseEntity.ok(mapToProductResponse(savedProduct));
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByStore(Long storeId) {
		Store store = storeRepository.findById(storeId).orElse(null);
		if (store == null) {
			return List.of();
		}

		return productRepository.findByStore(store).stream().map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByVendor(User vendor) {
		return productRepository.findByVendor(vendor).stream().map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public ResponseEntity<?> updateProduct(Long productId, ProductRequest productRequest, User vendor) {
		Product product = productRepository.findById(productId).orElse(null);

		if (product == null) {
			return ResponseEntity.badRequest().body("Product not found.");
		}

		// Ensure the product belongs to the vendor
		if (!product.getVendor().getId().equals(vendor.getId())) {
			return ResponseEntity.status(403).body("You are not authorized to update this product.");
		}

		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setImage(productRequest.getImage());
		product.setPrice(productRequest.getPrice());
		product.setAvailableStock(productRequest.getAvailableStock());
		product.setCategory(productRequest.getCategory());

		Product updatedProduct = productRepository.save(product);
		return ResponseEntity.ok(mapToProductResponse(updatedProduct));
	}

	@Transactional
	public ResponseEntity<?> deleteProduct(Long productId, User vendor) {
		Product product = productRepository.findById(productId).orElse(null);

		if (product == null) {
			return ResponseEntity.badRequest().body("Product not found.");
		}

		if (!product.getVendor().getId().equals(vendor.getId())) {
			return ResponseEntity.status(403).body("You are not authorized to delete this product.");
		}

		productRepository.delete(product);
		return ResponseEntity.ok("Product deleted successfully.");
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getAvailableProducts() {
		return productRepository.findAll().stream().filter(product -> product.getAvailableStock() > 0) // ✅ Only
																										// available
																										// stock
				.filter(product -> product.getStore().getStatus() == StoreStatus.APPROVED) // ✅ Only from approved
																							// stores
				.map(this::mapToProductResponse).collect(Collectors.toList());
	}

	public Page<Product> getFilteredProducts(String productName, String storeName, String category, String sort,
			int page, int size) {

		Pageable pageable = PageRequest.of(page, size,
				"desc".equalsIgnoreCase(sort) ? Sort.by("price").descending() : Sort.by("price").ascending());

		return productRepository.searchProducts(productName, storeName, category, pageable);
	}
}
