package com.lokaldeals.LokalDeals.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DealRequest {
    private String title;
    private String description;
    private BigDecimal discountPercentage;
    private LocalDateTime expiryDate;
    private Integer businessId;
    private Integer categoryId;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public Integer getBusinessId() { return businessId; }
    public void setBusinessId(Integer businessId) { this.businessId = businessId; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}