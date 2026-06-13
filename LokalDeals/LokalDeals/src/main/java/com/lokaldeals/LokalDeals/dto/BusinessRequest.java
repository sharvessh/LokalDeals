package com.lokaldeals.LokalDeals.dto;

public class BusinessRequest {
    private String name;
    private Double latitude;
    private Double longitude;
    private String category;
    private String contact;

    // --- Getters and Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}