package com.lokaldeals.LokalDeals.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lokaldeals.LokalDeals.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Used by AuthController for login and duplicate checks
    Optional<User> findByEmail(String email);
}