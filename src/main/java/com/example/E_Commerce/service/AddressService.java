package com.example.E_Commerce.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.E_Commerce.dto.AddressDTO;
import com.example.E_Commerce.entity.Address;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.exceptionHandler.AddressNotFoundException;
import com.example.E_Commerce.exceptionHandler.CustomerNotFoundException;
import com.example.E_Commerce.repository.AddressRepository;
import com.example.E_Commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional
    public Address addOrUpdateAddress(AddressDTO addressDTO) {
        User customer = getAuthenticatedUser();

        Address address = addressRepository.findByCustomer(customer)
                .orElse(new Address());

        address.setCustomer(customer);
        address.setHouseNo(addressDTO.getHouseNo());
        address.setHouseName(addressDTO.getHouseName());
        address.setStreet(addressDTO.getStreet());
        address.setArea(addressDTO.getArea());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setNation(addressDTO.getNation());
        address.setPincode(addressDTO.getPincode());
        address.setPhoneNo(addressDTO.getPhoneNo());

        return addressRepository.save(address);
    }

    public Address getCustomerAddress() {
        User customer = getAuthenticatedUser();
        return addressRepository.findByCustomer(customer)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));
    }
}
