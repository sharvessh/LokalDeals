package com.lokaldeals.LokalDeals.dto;

import com.lokaldeals.LokalDeals.models.UserType;
import java.math.BigDecimal;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserType userType;
    private BigDecimal latitude;
    private BigDecimal longitude;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
}