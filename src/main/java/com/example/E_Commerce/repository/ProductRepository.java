package com.example.E_Commerce.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.entity.Store;
import com.example.E_Commerce.entity.User;
import org.springframework.data.domain.Page;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);
    List<Product> findByVendor(User vendor);
    
    @Query("SELECT p FROM Product p WHERE " +
            "(:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
            "(:storeName IS NULL OR LOWER(p.store.name) LIKE LOWER(CONCAT('%', :storeName, '%'))) AND " +
            "(:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%')))")
     Page<Product> searchProducts(@Param("productName") String productName, 
                                  @Param("storeName") String storeName, 
                                  @Param("category") String category, 
                                  Pageable pageable);
}
