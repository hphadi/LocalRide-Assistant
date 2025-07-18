package com.localride.client.controller;

import com.localride.client.service.ApiService;
import com.localride.common.model.UserResponseDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.web.WebView;

public class RideRequestsController {
    @FXML
    private WebView mapView;
    // ApiService should be injected or passed, not instantiated directly like this
    // For now, we'll keep it as a field, but ideally it's passed from DashboardController
    private ApiService apiService; // Remove 'final' and instantiate in a proper way or inject

    // Add a setter for ApiService
    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    @FXML
    private void initialize() {
        // Ensure apiService is not null before using it
        if (apiService == null) {
            System.err.println("Error: ApiService not set in RideRequestsController.");
            showAlert("Error", "Application error: API service not initialized.");
            return;
        }

        try {
            String token = apiService.getJwtToken();
            if (token == null || token.isEmpty()) { // Check for empty token as well
                showAlert("Error", "Please login first. No JWT token found.");
                loadMap(0.0, 0.0, "Login Required"); // Load default map if no token
                return;
            }

            // CORRECTED: Call getCurrentUser() without arguments
            UserResponseDTO user = apiService.getCurrentUser();

            if (user == null || user.getLongitude() == null || user.getLatitude() == null) {
                showAlert("Warning", "User location not available.");
                loadMap(0.0, 0.0, "Unknown");
                return;
            }
            double lon = user.getLongitude(); // longitude
            double lat = user.getLatitude(); // latitude
            if (Double.isNaN(lon) || Double.isNaN(lat) || Double.isInfinite(lon) || Double.isInfinite(lat)) {
                showAlert("Error", "Invalid location coordinates.");
                loadMap(0.0, 0.0, "Invalid");
                return;
            }
            loadMap(lat, lon, user.getUsername());
        } catch (Exception e) {
            showAlert("Error", "Failed to load user location: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMap(double lat, double lon, String markerName) {
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Simple Map</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    #map { height: 150px; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map = L.map('map').setView([%f, %f], 13);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '© OpenStreetMap'
                    }).addTo(map);
                    L.marker([%f, %f]).addTo(map)
                        .bindPopup('%s')
                        .openPopup();
                </script>
            </body>
            </html>
            """.formatted(lat, lon, lat, lon, markerName);
        mapView.getEngine().loadContent(htmlContent);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}