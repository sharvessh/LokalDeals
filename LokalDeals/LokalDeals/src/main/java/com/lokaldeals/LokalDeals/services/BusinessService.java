package com.lokaldeals.LokalDeals.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lokaldeals.LokalDeals.models.Business;
import com.lokaldeals.LokalDeals.models.User;
import com.lokaldeals.LokalDeals.repositories.BusinessRepository;
import com.lokaldeals.LokalDeals.repositories.UserRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    public Business createBusiness(Business business, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Error: Owner account not found for email: " + ownerEmail));
        
        business.setOwner(owner);
        return businessRepository.save(business);
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public Optional<Business> getBusinessById(Integer businessId) { // Updated type to Integer
        return businessRepository.findById(businessId);
    }

    public List<Business> getBusinessesByOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Error: Account not found for email: " + ownerEmail));
        
        return businessRepository.findByOwner(owner);
    }
}