package com.example.E_Commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Commerce.entity.Store;
import com.example.E_Commerce.entity.StoreStatus;
import com.example.E_Commerce.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByName(String name);

    Optional<Store> findByVendorAndAddressAndPincode(User vendor, String address, String pincode);
    
    List<Store> findByStatus(StoreStatus status);
    
    List<Store> findByVendor(User vendor);

}
