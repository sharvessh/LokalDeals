package com.lokaldeals.LokalDeals.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lokaldeals.LokalDeals.dto.BusinessRequest;
import com.lokaldeals.LokalDeals.models.Business;
import com.lokaldeals.LokalDeals.services.BusinessService;

@RestController
@RequestMapping("/businesses")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    // POST /businesses — Register/Set up a new storefront profile
    @PostMapping
    public ResponseEntity<?> setupBusinessProfile(@RequestBody BusinessRequest request, @AuthenticationPrincipal String userEmail) {
        try {
            Business business = businessService.createBusinessProfile(request, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(business);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /businesses/{id} — Fetch public profile details for consumers or cards
    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessDetails(@PathVariable Integer id) {
        try {
            Business business = businessService.getBusinessById(id);
            return ResponseEntity.ok(business);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}