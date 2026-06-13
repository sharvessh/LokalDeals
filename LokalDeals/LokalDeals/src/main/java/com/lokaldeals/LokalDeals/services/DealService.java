package com.lokaldeals.LokalDeals.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lokaldeals.LokalDeals.dto.DealRequest;
import com.lokaldeals.LokalDeals.models.Business;
import com.lokaldeals.LokalDeals.models.Category;
import com.lokaldeals.LokalDeals.models.Deal;
import com.lokaldeals.LokalDeals.repositories.BusinessRepository;
import com.lokaldeals.LokalDeals.repositories.CategoryRepository;
import com.lokaldeals.LokalDeals.repositories.DealRepository;

@Service
public class DealService {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Deal createDeal(DealRequest request, String userEmail) {
        if (request == null) {
            throw new IllegalArgumentException("Request payload cannot be null");
        }
        validateDealPayload(request.getDiscountPercentage(), request.getExpiryDate());

        // IDs are integers in existing repositories
        Integer businessId = request.getBusinessId();
        Integer categoryId = request.getCategoryId();

        if (businessId == null || categoryId == null) {
            throw new IllegalArgumentException("Business ID and Category ID must not be null");
        }

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Error: Business not found!"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Error: Category not found!"));

        if (!business.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this business entity.");
        }

        Deal deal = new Deal();
        deal.setTitle(request.getTitle());
        deal.setDescription(request.getDescription());
        deal.setDiscountPercentage(request.getDiscountPercentage());
        deal.setExpiryDate(request.getExpiryDate());
        deal.setBusiness(business);
        deal.setCategory(category);

        return dealRepository.save(deal);
    }

    public Deal updateDeal(Integer dealId, DealRequest request, String userEmail) {
        if (dealId == null || request == null) {
            throw new IllegalArgumentException("Deal ID and request payload cannot be null");
        }

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Error: Deal not found!"));

        if (!deal.getBusiness().getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: You cannot modify this deal configuration.");
        }

        validateDealPayload(request.getDiscountPercentage(), request.getExpiryDate());

        Integer categoryId = request.getCategoryId();
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Error: Category not found!"));

        deal.setTitle(request.getTitle());
        deal.setDescription(request.getDescription());
        deal.setDiscountPercentage(request.getDiscountPercentage());
        deal.setExpiryDate(request.getExpiryDate());
        deal.setCategory(category);

        return dealRepository.save(deal);
    }

    public void deleteDeal(Integer dealId, String userEmail) {
        if (dealId == null) {
            throw new IllegalArgumentException("Deal ID cannot be null");
        }

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Error: Deal not found!"));

        if (!deal.getBusiness().getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: You cannot remove this deal entry.");
        }

        dealRepository.delete(deal);
    }

    private void validateDealPayload(BigDecimal discount, LocalDateTime expiry) {
        if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Validation Error: Discount must be scaled between 0.00% and 100.00%.");
        }
        if (expiry == null || expiry.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Validation Error: Target expiration timestamp must map to a future date context.");
        }
    }

    public List<Deal> getNearbyDeals(Double lat, Double lng, Double radius, Integer categoryId, BigDecimal minDiscount) {
        if (lat == null || lng == null) {
            throw new IllegalArgumentException("Validation Error: Latitude and longitude parameters are required.");
        }
        Double searchRadius = (radius != null) ? radius : 10.0;
        return dealRepository.findNearbyDeals(lat, lng, searchRadius, categoryId, minDiscount);
    }
}