package com.lokaldeals.LokalDeals.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lokaldeals.LokalDeals.dto.AuthResponse;
import com.lokaldeals.LokalDeals.dto.LoginRequest;
import com.lokaldeals.LokalDeals.dto.RegisterRequest;
import com.lokaldeals.LokalDeals.models.User;
import com.lokaldeals.LokalDeals.repositories.UserRepository;
import com.lokaldeals.LokalDeals.security.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Email is already taken!");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserType(registerRequest.getUserType());
        user.setLatitude(registerRequest.getLatitude());
        user.setLongitude(registerRequest.getLongitude());

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            String token = jwtUtils.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, loginRequest.getEmail()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid email or password!");
    }

    @GetMapping("/auth/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("🚀 LokalDeals Backend Server is Online and Connected to MySQL!");
}
}
