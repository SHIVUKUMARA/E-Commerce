package com.example.E_Commerce.controller;

import com.example.E_Commerce.dto.AddressDTO;
import com.example.E_Commerce.entity.Address;
import com.example.E_Commerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<String> addOrUpdateAddress(@RequestBody AddressDTO addressDTO) {
        addressService.addOrUpdateAddress(addressDTO);
        return ResponseEntity.ok("Address saved successfully!");
    }

    @GetMapping("/get")
    public ResponseEntity<Address> getAddress() {
        Address address = addressService.getCustomerAddress();
        return ResponseEntity.ok(address);
    }
}
