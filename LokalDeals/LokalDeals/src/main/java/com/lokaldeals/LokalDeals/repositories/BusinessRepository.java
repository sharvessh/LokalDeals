package com.lokaldeals.LokalDeals.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> {
    // Look up a storefront using the logged-in merchant's account email
    Optional<Business> findByOwnerEmail(String email);
}