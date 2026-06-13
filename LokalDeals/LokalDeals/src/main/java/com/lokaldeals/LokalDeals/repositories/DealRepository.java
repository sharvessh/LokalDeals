package com.lokaldeals.LokalDeals.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal, Integer> {

    // Native query utilizing the Haversine formula (6371 maps to the Earth's radius in kilometers)
    @Query(value = "SELECT d.* FROM deal d " +
                   "JOIN business b ON d.business_id = b.business_id " +
                   "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(b.latitude)) * " +
                   "cos(radians(b.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
                   "sin(radians(b.latitude)))) <= :radius " +
                   "AND (:categoryId IS NULL OR d.category_id = :categoryId) " +
                   "AND (:minDiscount IS NULL OR d.discount_percentage >= :minDiscount) " +
                   "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(b.latitude)) * " +
                   "cos(radians(b.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
                   "sin(radians(b.latitude)))) ASC", 
           nativeQuery = true)
    List<Deal> findNearbyDeals(
        @Param("lat") Double lat, 
        @Param("lng") Double lng, 
        @Param("radius") Double radius,
        @Param("categoryId") Integer categoryId,
        @Param("minDiscount") BigDecimal minDiscount
    );
}