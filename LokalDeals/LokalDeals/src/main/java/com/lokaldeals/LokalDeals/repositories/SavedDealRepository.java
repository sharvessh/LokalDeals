package com.lokaldeals.LokalDeals.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.SavedDeal;

@Repository
public interface SavedDealRepository extends JpaRepository<SavedDeal, Integer> {
    // Utility method to quickly fetch bookmarked items by user ID
    List<SavedDeal> findByUser_UserId(Integer userId);
}