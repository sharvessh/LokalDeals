package com.lokaldeals.LokalDeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
}