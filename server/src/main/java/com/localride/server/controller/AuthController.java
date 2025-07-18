package com.localride.server.controller;

import com.localride.common.model.UserRegistrationRequest;
import com.localride.common.model.UserResponseDTO;
import com.localride.server.mapper.UserMapper;
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import com.localride.server.service.UserService;
import com.localride.server.utils.JwtUtil;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired // <<-- UserMapper رو اینجا تزریق کن
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRegistrationRequest loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());
        try {
            // Spring Security authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtUtil.generateToken(userDetails);
                logger.info("Login successful for user: {}", userDetails.getUsername());
                // Generate JWT token
//                String jwtToken = jwtUtil.generateToken(authentication.getName());
//                logger.debug("Generated JWT token for user: {}", loginRequest.getUsername());
//                return ResponseEntity.ok(jwtToken);
                return ResponseEntity.ok(Collections.singletonMap("jwt-token", token).toString());
            } else {
                logger.warn("Authentication failed for user: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
//            return userRepository.findByUsername(loginRequest.getUsername())
//                    .map(user -> {
//                        logger.debug("Found user: {}, checking password", user.getUsername());
//                        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
//                            logger.info("Login successful for user: {}", user.getUsername());
//                            return ResponseEntity.ok("Login successful");
//                        } else {
//                            logger.warn("Invalid password for user: {}", user.getUsername());
//                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//                        }
//                    })
//                    .orElseGet(() -> {
//                        logger.warn("User not found: {}", loginRequest.getUsername());
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//                    });
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed for user{}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid username or password");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during login for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registerRequest) {
        logger.info("Received registration request for username: {}", registerRequest.getUsername());
        try {
            User user = userService.registerUser(registerRequest);
            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException ex) {
            logger.error("User registration failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + ex.getMessage());
        } catch (Exception e) {
            logger.error("User registration failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

//    @PostMapping("/login-placeholder")
//    public ResponseEntity<String> loginPlaceholder(@RequestBody UserRegistrationRequest loginRequest) {
//        logger.info("Received login-placeholder request for username: {}", loginRequest.getUsername());
//        return userRepository.findByUsername(loginRequest.getUsername())
//                .map(user -> {
//                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
//                        logger.info("Login-placeholder successful for user: {}", user.getUsername());
//                        return ResponseEntity.ok("Login successful for " + user.getUsername());
//                    } else {
//                        logger.warn("Invalid password for user: {}", user.getUsername());
//                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//                   // }
//                })
//                .orElseGet(() -> {
//                    logger.warn("User not found: {}", loginRequest.getUsername());
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//                });
//    }

    @GetMapping("/{id}") // معمولاً با این مسیر برای دریافت کاربر خاص استفاده می‌شود
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT') or @securityService.isOwner(#id)") // مثال دسترسی
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Received request to get user with id: {}", id);
        // UserService اکنون Optional<User> برمی‌گرداند، بنابراین باید آن را به DTO تبدیل کنیم
        return userService.getUserById(id)
                .map(userMapper::toDto) // تبدیل User به UserResponseDTO
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }
}