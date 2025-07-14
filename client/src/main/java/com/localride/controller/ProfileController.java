package com.localride.controller;

import com.localride.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;

    public void setUser(User user) {
        nameLabel.setText("Name: " + user.getName());
        roleLabel.setText("Role: " + user.getRole());
    }
}
