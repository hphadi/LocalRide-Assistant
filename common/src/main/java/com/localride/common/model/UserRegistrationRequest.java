package com.localride.common.model;

import com.localride.common.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private Role role; // Role as Enum for backend processing
    // No location field here, as it's usually set by the server or a separate update
    private String location;
}