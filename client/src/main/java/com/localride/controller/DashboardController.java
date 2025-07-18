package com.localride.controller;

import com.localride.client.controller.ProfileController;
import com.localride.client.util.LanguageManager;
import com.localride.common.model.User;
import com.localride.common.model.UserResponseDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ResourceBundle;

public class DashboardController {
    @FXML private VBox menuBox;
    @FXML private Pane contentPane;
    @FXML private Button loginButton;
    @FXML private Button addPassengerButton;
    @FXML private Button listPassengersButton;
    @FXML private Button addDriverButton;
    @FXML private Button listDriversButton;
    @FXML private Button listRidesButton;
    @FXML private Button rideRequestsButton;
    @FXML private Button activeRidesButton;
    @FXML private Button profileButton;

    private UserResponseDTO currentUser;
    private ResourceBundle bundle;
    private Button selectedButton;

    public void initialize() {
        try {
            bundle = LanguageManager.getBundle();
            if (contentPane == null) {
                System.out.println("Error: contentPane is null. Check fx:id in dashboard.fxml");
                return;
            }
            updateButtonText();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading resource bundle: " + e.getMessage());
        }
    }

    public void setUser(UserResponseDTO user) {
        this.currentUser = user;
        if (loginButton != null) {
            loginButton.setText(bundle.getString("logout"));
            loginButton.setOnAction(event -> logout());
        }
        if (profileButton != null) {
            profileButton.setVisible(true);
        }
        if (addPassengerButton != null) {
            addPassengerButton.setDisable(user.getRole().equals("Driver"));
        }
        if (addDriverButton != null) {
            addDriverButton.setDisable(user.getRole().equals("Passenger"));
        }
        contentPane.getChildren().setAll(new Label("Welcome back, " + user.getName() + "!"));
    }

    private void updateButtonText() {
        if (loginButton != null) loginButton.setText(bundle.getString("login"));
        if (addPassengerButton != null) addPassengerButton.setText(bundle.getString("add_passenger"));
        if (listPassengersButton != null) listPassengersButton.setText(bundle.getString("list_passengers"));
        if (addDriverButton != null) addDriverButton.setText(bundle.getString("add_driver"));
        if (listDriversButton != null) listDriversButton.setText(bundle.getString("list_drivers"));
        if (listRidesButton != null) listRidesButton.setText(bundle.getString("list_rides"));
        if (rideRequestsButton != null) rideRequestsButton.setText(bundle.getString("ride_requests"));
        if (activeRidesButton != null) activeRidesButton.setText(bundle.getString("active_rides"));
        if (profileButton != null) profileButton.setText(bundle.getString("profile"));
    }

    private void setSelectedButton(Button button) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("selected");
        }
        selectedButton = button;
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("selected");
        }
    }

    @FXML private void showLogin() throws IOException {
        if (contentPane == null) {
            System.out.println("Error: contentPane is null in showLogin");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), bundle);
        if (loader.getLocation() == null) {
            System.out.println("Error: login.fxml not found in /fxml/");
            contentPane.getChildren().setAll(new Label("Error: Login form not available."));
            return;
        }
        Pane newContent = loader.load();
        LoginController loginController = loader.getController();
        loginController.setDashboardController(this);
        contentPane.getChildren().setAll(newContent);
        setSelectedButton(loginButton);
    }

    @FXML private void showAddPassenger() throws IOException {
        loadContent("add_passenger.fxml");
        setSelectedButton(addPassengerButton);
    }

    @FXML private void showListPassengers() throws IOException {
        loadContent("list_passengers.fxml");
        setSelectedButton(listPassengersButton);
    }

    @FXML private void showAddDriver() throws IOException {
        loadContent("add_driver.fxml");
        setSelectedButton(addDriverButton);
    }

    @FXML private void showListDrivers() throws IOException {
        loadContent("list_drivers.fxml");
        setSelectedButton(listDriversButton);
    }

    @FXML private void showListRides() throws IOException {
        loadContent("list_rides.fxml");
        setSelectedButton(listRidesButton);
    }

    @FXML private void showRideRequests() throws IOException {
        loadContent("ride_requests.fxml");
        setSelectedButton(rideRequestsButton);
    }

    @FXML private void showActiveRides() throws IOException {
        loadContent("active_rides.fxml");
        setSelectedButton(activeRidesButton);
    }

    @FXML private void showProfile() throws IOException {
        loadContent("profile.fxml");
        setSelectedButton(profileButton);
    }

    private void loadContent(String fxmlFile) throws IOException {
        if (contentPane == null) {
            System.out.println("Error: contentPane is null in loadContent");
            return;
        }
        if (currentUser == null && !fxmlFile.equals("list_passengers.fxml") &&
                !fxmlFile.equals("list_drivers.fxml") && !fxmlFile.equals("list_rides.fxml") &&
                !fxmlFile.equals("ride_requests.fxml") && !fxmlFile.equals("active_rides.fxml") &&
                !fxmlFile.equals("login.fxml")) {
            System.out.println("Please login to access this feature!");
            contentPane.getChildren().setAll(new Label("Please login to access this feature!"));
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile), bundle);
        if (loader.getLocation() == null) {
            System.err.println("Error: " + fxmlFile + " not found in /fxml/");
            contentPane.getChildren().setAll(new Label("Error: Feature not available."));
            return;
        }
        Pane newContent = loader.load();
        if (fxmlFile.equals("profile.fxml")) {
            ProfileController controller = loader.getController();
            controller.setUser(currentUser);
        }
        contentPane.getChildren().setAll(newContent);
    }

    private void logout() {
        if (contentPane == null) {
            System.out.println("Error: contentPane is null in logout");
            return;
        }
        currentUser = null;
        if (loginButton != null) {
            loginButton.setText(bundle.getString("login"));
            loginButton.setOnAction(event -> {
                try {
                    showLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        if (profileButton != null) {
            profileButton.setVisible(false);
        }
        if (addPassengerButton != null) {
            addPassengerButton.setDisable(true);
        }
        if (addDriverButton != null) {
            addDriverButton.setDisable(true);
        }
        contentPane.getChildren().setAll(new Label("Welcome to LocalRideAssistant Dashboard"));
    }
}