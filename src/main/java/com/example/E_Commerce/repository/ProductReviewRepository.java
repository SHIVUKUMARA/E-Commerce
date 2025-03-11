package com.example.E_Commerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.E_Commerce.entity.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductId(Long productId);
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
}
