package com.localride.server.service;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.model.Role; // Keep this import
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) { // Inject passwordEncoder
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Use injected passwordEncoder
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // --- CRITICAL CHANGE HERE FOR ROLE HANDLING ---
        if (request.getRole() != null) {
            // Directly assign the Role enum from request, no conversion needed
            user.setRole(request.getRole());
        } else {
            // You can set a default Role or throw an exception if role is mandatory
            user.setRole(Role.PASSENGER); // Example: Default Role
        }
        // --- END CRITICAL CHANGE ---

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            try {
                WKTReader wktReader = new WKTReader();
                Point location = (Point) wktReader.read(request.getLocation());
                user.setLocation(location);
            } catch (Exception e) {
                throw new RuntimeException("Invalid location format: " + e.getMessage());
            }
        }

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> updateUser(Long id, UserRegistrationRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(request.getUsername());
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                    }

                    // --- CRITICAL CHANGE HERE FOR ROLE HANDLING ---
                    if (request.getRole() != null) {
                        // Directly assign the Role enum from request, no conversion needed
                        user.setRole(request.getRole());
                    }
                    // --- END CRITICAL CHANGE ---

                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    user.setPhone(request.getPhone());

                    if (request.getLocation() != null && !request.getLocation().isEmpty()) {
                        try {
                            WKTReader wktReader = new WKTReader();
                            Point location = (Point) wktReader.read(request.getLocation());
                            user.setLocation(location);
                        } catch (Exception e) {
                            throw new RuntimeException("Invalid location format: " + e.getMessage());
                        }
                    } else {
                        user.setLocation(null); // Clear location if null/empty string provided
                    }

                    return userRepository.save(user);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // IMPORTANT: This method's parameter `role` is a String.
    // Ensure your UserRepository's findByRole method expects a String or update this logic if needed.
    // For a consistent approach, it's often better if this also accepts Role enum.
    // Example: public List<User> getUsersByRole(Role role) { return userRepository.findByRole(role); }
    // If your UserRepository.findByRole still expects String, make sure it does the conversion.
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> findNearbyUsers(Point currentLocation, String targetRole, double radiusInMeters) {
        if (currentLocation == null) {
            throw new IllegalArgumentException("Current location cannot be null for nearby user search.");
        }
        // Assuming userRepository.findNearbyUsers expects String for targetRole
        return userRepository.findNearbyUsers(currentLocation, targetRole, radiusInMeters);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}