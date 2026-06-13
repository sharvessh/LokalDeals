package com.lokaldeals.LokalDeals.dto;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String userType;
    private Double latitude;
    private Double longitude;

    // Getters and Setters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}