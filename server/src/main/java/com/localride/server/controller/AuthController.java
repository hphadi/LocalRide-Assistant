package com.localride.server.controller;

import com.localride.server.dto.LoginRequest;
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * AuthController handles authentication-related requests.
 * It is currently a placeholder and does not contain any methods.
 * RestController annotations means it will handle HTTP requests
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * UserRepository is injected to interact with user data.
     * It can be used to perform operations like finding users by username,
     * saving new users, etc.
     */
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Placeholder for login logic
        // This method should handle user authentication
        // and return a response indicating success or failure
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (user.getPasswordHash().equals(loginRequest.getPassword())) {
                        return ResponseEntity.ok("Login successful");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
    @PostMapping("/login-placeholder")
    public ResponseEntity<String> loginPlaceholder(@RequestBody LoginRequest loginRequest) {
            return userRepository.findByUsername(loginRequest.getUsername())
                    .map(user -> {
                        // TODO: Implement password verification (e.g., BCrypt)
                        if ("password123".equals(loginRequest.getPassword())) { // Placeholder
                            return ResponseEntity.ok("Login successful for " + user.getUsername());
                        } else {
                            return ResponseEntity.status(401).body("Invalid credentials");
                        }
                    })
                    .orElseGet(() -> ResponseEntity.status(404).body("User not found"));

    }
}
