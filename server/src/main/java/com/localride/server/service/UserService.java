package com.localride.server.service;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.model.Role; // این را اضافه کنید
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
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // اصلاح کلیدی: تبدیل String Role به Role enum
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole().toUpperCase())); // تبدیل String به Enum
        } else {
            // می توانید یک Role پیش فرض بگذارید یا خطا پرتاب کنید
            user.setRole(Role.PASSENGER); // مثال: Role پیش فرض
        }

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

                    // اصلاح کلیدی: تبدیل String Role به Role enum
                    if (request.getRole() != null) {
                        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
                    }

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
                        user.setLocation(null);
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

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> findNearbyUsers(Point currentLocation, String targetRole, double radiusInMeters) {
        if (currentLocation == null) {
            throw new IllegalArgumentException("Current location cannot be null for nearby user search.");
        }
        return userRepository.findNearbyUsers(currentLocation, targetRole, radiusInMeters);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}