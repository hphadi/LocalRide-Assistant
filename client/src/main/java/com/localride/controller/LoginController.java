package com.localride.controller;

import com.localride.model.User;
import com.localride.service.UserManager;
import com.localride.util.LanguageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

public class LoginController {
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Label welcomeLabel;
    private UserManager userManager;
    private ResourceBundle bundle;
    private DashboardController dashboardController;

    public void initialize() {
        bundle = LanguageManager.getBundle();
        if (welcomeLabel != null) {
            welcomeLabel.setText(bundle.getString("welcome"));
        }
        if (roleChoice != null) {
            roleChoice.setItems(FXCollections.observableArrayList(
                    bundle.getString("passenger"),
                    bundle.getString("driver"),
                    bundle.getString("admin")
            ));
            roleChoice.setValue(bundle.getString("passenger"));
        }
        userManager = new UserManager();
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    private void handleLogin() {
        if (nameField == null || roleChoice == null) {
            System.out.println("Error: nameField or roleChoice is null. Check fx:id in login.fxml");
            return;
        }
        String name = nameField.getText().trim();
        String role = roleChoice.getValue();

        if (name.isEmpty()) {
            System.out.println("Please enter a name!");
            return;
        }

        User user = new User(name, role.equals(bundle.getString("passenger")) ? "Passenger" :
                role.equals(bundle.getString("driver")) ? "Driver" : "Admin");
        userManager.addUser(user);

        if (dashboardController != null) {
            dashboardController.setUser(user);
        }
    }
}