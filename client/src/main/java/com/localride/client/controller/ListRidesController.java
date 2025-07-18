package com.localride.client.controller;

import com.localride.client.service.ApiService;
import com.localride.common.model.Ride;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;

public class ListRidesController implements ApiServiceConsumer { // Implement ApiServiceConsumer
    @FXML
    private TableView<Ride> ridesTable;
    @FXML
    private TableColumn<Ride, Integer> idColumn;
    @FXML
    private TableColumn<Ride, String> passengerColumn;
    @FXML
    private TableColumn<Ride, String> driverColumn;
    @FXML
    private TableColumn<Ride, String> statusColumn;
    @FXML
    private TableColumn<Ride, String> locationColumn;

    private ApiService apiService; // Remove 'final'

    @Override // Implement setApiService from ApiServiceConsumer interface
    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Ensure Ride model has getPassenger() and getDriver() returning objects with getName()
        passengerColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPassenger() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassenger().getName());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        driverColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDriver() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDriver().getName());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location")); // Assumes Ride has getLocation() that returns a String or has a toString() for Point
        loadRides();
    }

    private void loadRides() {
        if (apiService == null) {
            System.err.println("Error: ApiService not set in ListRidesController.");
            showAlert("Error", "Application error: API service not initialized.");
            return;
        }

        try {
            String token = apiService.getJwtToken();
            if (token == null || token.isEmpty()) {
                showAlert("Error", "Please login first to view rides.");
                return;
            }

            // Call getAllRides() without arguments, as ApiService now holds the token
            ridesTable.getItems().setAll(apiService.getAllRides());
        } catch (Exception e) {
            showAlert("Error", "Failed to load rides: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}