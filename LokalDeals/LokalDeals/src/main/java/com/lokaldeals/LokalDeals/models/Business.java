package com.lokaldeals.LokalDeals.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "BUSINESS")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private Integer businessId; // Changed from Long to Integer

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; 

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    public Business() {}

    public Business(String name, Double latitude, Double longitude, String category, String contact, User owner) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.contact = contact;
        this.owner = owner;
    }

    public Integer getBusinessId() { return businessId; } // Changed to Integer
    public void setBusinessId(Integer businessId) { this.businessId = businessId; }

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

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}