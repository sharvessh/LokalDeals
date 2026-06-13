package com.lokaldeals.LokalDeals.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lokaldeals.LokalDeals.dto.BusinessRequest;
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

    public Business createBusinessProfile(BusinessRequest request, String ownerEmail) {
        Objects.requireNonNull(request, "Business request data cannot be null");
        if (ownerEmail == null || ownerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner email session token missing");
        }

        // Check if this merchant user already set up a storefront profile
        if (businessRepository.findByOwnerEmail(ownerEmail).isPresent()) {
            throw new RuntimeException("Conflict: A business profile already exists for this account.");
        }

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Error: Associated User account not found!"));

        Business business = new Business();
        business.setBusinessName(request.getBusinessName());
        business.setContact(request.getContact());
        business.setLocation(request.getLocation());
        business.setOwner(owner);

        return businessRepository.save(business);
    }

    public Business getBusinessById(Integer businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Error: Storefront profile not found!"));
    }
}