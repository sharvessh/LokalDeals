package com.lokaldeals.LokalDeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Basic CRUD inherited from JpaRepository allows finding categories by ID
}