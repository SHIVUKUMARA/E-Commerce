package com.example.E_Commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.E_Commerce.entity.Address;
import com.example.E_Commerce.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByCustomer(User customer);

	@Query("SELECT a FROM Address a WHERE a.customer.id = :customerId ORDER BY a.id DESC LIMIT 1")
	Optional<Address> findLatestAddressByCustomerId(@Param("customerId") Long customerId);
}
