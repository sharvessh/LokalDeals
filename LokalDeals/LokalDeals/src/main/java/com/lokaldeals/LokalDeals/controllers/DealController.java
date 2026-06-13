package com.lokaldeals.LokalDeals.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lokaldeals.LokalDeals.dto.DealRequest;
import com.lokaldeals.LokalDeals.models.Deal;
import com.lokaldeals.LokalDeals.services.DealService;

@RestController
@RequestMapping("/deals")
public class DealController {

    @Autowired
    private DealService dealService;

    // GET /deals/nearby — Fetch filtered promotions near coordinates
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyDeals(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam(value = "radius", required = false) Double radius,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "minDiscount", required = false) BigDecimal minDiscount) {
        try {
            List<Deal> nearbyDeals = dealService.getNearbyDeals(lat, lng, radius, categoryId, minDiscount);
            return ResponseEntity.ok(nearbyDeals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /deals — Fetch a comprehensive list of all stored entries
    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }

    // POST /deals — Create a brand new promotion entry (business only)
    @PostMapping
    public ResponseEntity<?> createDeal(@RequestBody DealRequest request, @AuthenticationPrincipal String userEmail) {
        try {
            Deal deal = dealService.createDeal(request, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(deal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PUT /deals/{id} — Modify an existing deal's attributes
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeal(@PathVariable Integer id, @RequestBody DealRequest request, @AuthenticationPrincipal String userEmail) {
        try {
            Deal updatedDeal = dealService.updateDeal(id, request, userEmail);
            return ResponseEntity.ok(updatedDeal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE /deals/{id} — Drop a deal from the system
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeal(@PathVariable Integer id, @AuthenticationPrincipal String userEmail) {
        try {
            dealService.deleteDeal(id, userEmail);
            return ResponseEntity.ok("Deal entry deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}