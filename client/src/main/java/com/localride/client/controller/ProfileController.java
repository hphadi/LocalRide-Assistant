package com.localride.client.controller;

import com.localride.common.model.User;
import com.localride.common.model.UserResponseDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private Label emailLabel; // Ensure fx:id="emailLabel" in profile.fxml
    @FXML private Label phoneLabel; // Ensure fx:id="phoneLabel" in profile.fxml

    /**
     * Sets the user data to be displayed in the profile view.
     * @param user The UserResponseDTO object whose profile is to be displayed.
     */
    public void setUser(UserResponseDTO user) { // Correct parameter type
        if (user != null) {
            nameLabel.setText("Name: " + user.getName());
            roleLabel.setText("Role: " + user.getRole());
            if (emailLabel != null) emailLabel.setText("Email: " + user.getEmail());
            if (phoneLabel != null) phoneLabel.setText("Phone: " + user.getPhone());
        } else {
            nameLabel.setText("User data not available.");
            roleLabel.setText("");
            if (emailLabel != null) emailLabel.setText("");
            if (phoneLabel != null) phoneLabel.setText("");
        }
    }
}