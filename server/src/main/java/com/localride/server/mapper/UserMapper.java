package com.localride.server.mapper; // You might need to create a 'mapper' package

import com.localride.server.model.User;
import com.localride.common.model.UserResponseDTO;
import org.locationtech.jts.geom.Point; // Import for Point

import org.springframework.stereotype.Component; // Make it a Spring component

import java.time.LocalDateTime; // Import for LocalDateTime
import java.util.List;
import java.util.stream.Collectors;

@Component // This makes it a Spring-managed bean, so you can @Autowired it
public class UserMapper {

    public UserResponseDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null); // Convert enum to String
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        // Convert Point to latitude and longitude
        if (user.getLocation() != null) {
            dto.setLatitude(user.getLocation().getY());
            dto.setLongitude(user.getLocation().getX());
        } else {
            dto.setLatitude(null);
            dto.setLongitude(null);
        }

        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    // You can also add a method to convert a list of Users to a list of DTOs
    public List<UserResponseDTO> toDtoList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // If you need to convert DTO back to User (e.g., for updates), you'd add methods here too.
    // For example:
    // public User toEntity(UserResponseDTO dto) { ... }
}