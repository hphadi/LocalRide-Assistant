package com.localride.server.controller;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.dto.UserResponseDTO;
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import com.localride.server.service.UserService;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRegistrationRequest loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    logger.debug("Found user: {}, checking password", user.getUsername());
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                        logger.info("Login successful for user: {}", user.getUsername());
                        return ResponseEntity.ok("Login successful");
                    } else {
                        logger.warn("Invalid password for user: {}", user.getUsername());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElseGet(() -> {
                    logger.warn("User not found: {}", loginRequest.getUsername());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                });
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registerRequest) {
        logger.info("Received registration request for username: {}", registerRequest.getUsername());
        try {
            User user = userService.registerUser(registerRequest);
            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            logger.error("User registration failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login-placeholder")
    public ResponseEntity<String> loginPlaceholder(@RequestBody UserRegistrationRequest loginRequest) {
        logger.info("Received login-placeholder request for username: {}", loginRequest.getUsername());
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                        logger.info("Login-placeholder successful for user: {}", user.getUsername());
                        return ResponseEntity.ok("Login successful for " + user.getUsername());
                    } else {
                        logger.warn("Invalid password for user: {}", user.getUsername());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElseGet(() -> {
                    logger.warn("User not found: {}", loginRequest.getUsername());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}