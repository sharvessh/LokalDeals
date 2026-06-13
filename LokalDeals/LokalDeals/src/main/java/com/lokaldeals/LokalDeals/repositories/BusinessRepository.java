package com.lokaldeals.LokalDeals.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.Business;
import com.lokaldeals.LokalDeals.models.User;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> { // Changed signature context type to Integer
    List<Business> findByOwner(User owner);
}