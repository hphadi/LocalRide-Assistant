package com.localride.common.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String role; // Changed to Role enum
    private String name;
    private String email;
    private String phone;
//    private String location; // WKT format (e.g., "POINT(10 20)")
    private Double latitude; // For location's latitude
    private Double longitude; // For location's longitude
    private LocalDateTime createdAt;



//    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//    public Role getRole() { return role; }
//    public void setRole(Role role) { this.role = role; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
//    public String getLocation() { return location; }
//    public void setLocation(String location) { this.location = location; }
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public void setLatitude(double y) {
//        this.latitude = y;
//    }
//    public Double getLatitude() {
//        return latitude;
//    }
//    public void setLongitude(double x) {
//        this.longitude = x;
//    }
//    public Double getLongitude() {
//        return longitude;
//    }
}