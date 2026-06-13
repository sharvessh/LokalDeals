package com.lokaldeals.LokalDeals.dto;

public class BusinessRequest {
    private String businessName;
    private String contact;
    private String location;

    // Getters and Setters
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}