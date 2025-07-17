package com.localride.server.dto;

import com.localride.server.model.Role;

public class UserRegistrationRequest {
    private String username;
    private String password;
    private Role role; // Changed to Role enum
    private String name;
    private String email;
    private String phone;
    private String location; // WKT format (e.g., "POINT(10 20)")

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}