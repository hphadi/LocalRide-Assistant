package com.localride.server.service;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.dto.UserResponseDTO;
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRole(request.getRole());
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

    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setPhone(user.getPhone());
                    dto.setCreatedAt(user.getCreatedAt());
                    if (user.getLocation() != null) {
                        dto.setLocation("POINT(" + user.getLocation().getX() + " " + user.getLocation().getY() + ")");
                    }
                    return dto;
                });
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setPhone(user.getPhone());
                    dto.setCreatedAt(user.getCreatedAt());
                    if (user.getLocation() != null) {
                        dto.setLocation("POINT(" + user.getLocation().getX() + " " + user.getLocation().getY() + ")");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserRegistrationRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(request.getUsername());
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
                    }
                    user.setRole(request.getRole());
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

                    User updatedUser = userRepository.save(user);
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(updatedUser.getId());
                    dto.setUsername(updatedUser.getUsername());
                    dto.setRole(updatedUser.getRole());
                    dto.setName(updatedUser.getName());
                    dto.setEmail(updatedUser.getEmail());
                    dto.setPhone(updatedUser.getPhone());
                    dto.setCreatedAt(updatedUser.getCreatedAt());
                    if (updatedUser.getLocation() != null) {
                        dto.setLocation("POINT(" + updatedUser.getLocation().getX() + " " + updatedUser.getLocation().getY() + ")");
                    }
                    return dto;
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}