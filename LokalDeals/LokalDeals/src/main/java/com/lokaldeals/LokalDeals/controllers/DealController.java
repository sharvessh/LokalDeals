package com.lokaldeals.LokalDeals.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "*") // Allows Raj's frontend code to safely make API calls to your server
@RestController
@RequestMapping("/deals")
public class DealController {

    @Autowired
    private DealService dealService;

    /**
     * GET /deals — Fetch every active promotion across the system.
     */
    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }

    /**
     * POST /deals — Broadcasting entry point for a merchant to register a flash promotion.
     */
    @PostMapping
    public ResponseEntity<?> createNewDeal(@RequestBody DealRequest request, @AuthenticationPrincipal String userEmail) {
        try {
            Deal savedDeal = dealService.createDeal(request, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDeal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * PUT /deals/{id} — Allows a merchant to modify title, descriptions, or discount scale.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyDeal(@PathVariable Integer id, @RequestBody DealRequest request, @AuthenticationPrincipal String userEmail) {
        try {
            Deal updatedDeal = dealService.updateDeal(id, request, userEmail);
            return ResponseEntity.ok(updatedDeal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * DELETE /deals/{id} — Instantly un-publishes a flash deal from circulation feeds.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeDeal(@PathVariable Integer id, @AuthenticationPrincipal String userEmail) {
        try {
            dealService.deleteDeal(id, userEmail);
            return ResponseEntity.ok("Success: Promotion removed cleanly from tracking engine.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * GET /deals/nearby — Core engine endpoint powering the Consumer landing screen feed maps.
     * Takes consumer coordinates and searches through Zul's spatial tables within a localized boundary limit.
     */
    @GetMapping("/nearby")
    public ResponseEntity<?> findLocalPromotions(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minDiscount) {
        try {
            List<Deal> nearbyDeals = dealService.getNearbyDeals(latitude, longitude, radius, categoryId, minDiscount);
            return ResponseEntity.ok(nearbyDeals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}