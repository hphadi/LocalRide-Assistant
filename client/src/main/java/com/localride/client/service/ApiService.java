package com.localride.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature; // Import is correct
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Import is correct

import com.localride.common.model.Ride;
import com.localride.common.model.UserRegistrationRequest;
import com.localride.common.model.UserResponseDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class ApiService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "http://localhost:8081/api";
    private String jwtToken;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // CORRECTED: 'A' changed to 'a' in TIMESTAMPS
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void setJwtToken(String token) {
        this.jwtToken = token;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String login(String username, String password) throws Exception {
        UserRegistrationRequest requestBody = new UserRegistrationRequest();
        requestBody.setUsername(username);
        requestBody.setPassword(password);

        String json = objectMapper.writeValueAsString(requestBody);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("Login request - URL: " + baseUrl + "/auth/login");
        System.out.println("Login request - Body: " + json);

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Login response - Status: " + response.statusCode() + ", Body: " + response.body());

        if (response.statusCode() == 200) {
            JsonNode jsonNode = objectMapper.readTree(response.body());
            if (jsonNode.has("jwt-token")) {
                jwtToken = jsonNode.get("jwt-token").asText();
                System.out.println("Login successful, JWT Token: " + jwtToken);
                return jwtToken;
            } else {
                throw new RuntimeException("Login failed: JWT token not found in response.");
            }
        } else {
            throw new RuntimeException("Login failed: " + response.body());
        }
    }

    public void register(UserRegistrationRequest request) throws Exception {
        String json = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("Register request - URL: " + baseUrl + "/auth/register");
        System.out.println("Register request - Body: " + json);
        System.out.println("Registering user: " + request.getUsername());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Register response - Status: " + response.statusCode() + ", Body: " + response.body());

        if (response.statusCode() == 200) {
            System.out.println("Registration successful: " + response.body());
        } else {
            throw new RuntimeException("Registration failed: " + response.body());
        }
    }

    private HttpRequest.Builder authorizedRequestBuilder(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if (jwtToken != null && !jwtToken.isEmpty()) {
            builder.header("Authorization", "Bearer " + jwtToken);
        }
        return builder;
    }

    public UserResponseDTO getUserById(Long id) throws Exception {
        String url = baseUrl + "/users/" + id;
        HttpRequest request = authorizedRequestBuilder(url)
                .GET()
                .build();
        System.out.println("Get user by ID request - URL: " + url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get user by ID response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserResponseDTO.class);
        } else {
            throw new RuntimeException("Failed to fetch user by ID: " + response.body());
        }
    }

    public UserResponseDTO getCurrentUser() throws Exception {
        String url = baseUrl + "/users/my-role";
        HttpRequest request = authorizedRequestBuilder(url)
                .GET()
                .build();
        System.out.println("Get current user request - URL: " + url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get current user response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() == 200) {
            List<UserResponseDTO> users = Arrays.asList(objectMapper.readValue(response.body(), UserResponseDTO[].class));
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                throw new RuntimeException("No user data returned for current user from /my-role endpoint.");
            }
        } else {
            throw new RuntimeException("Failed to fetch current user: " + response.body());
        }
    }

    public List<UserResponseDTO> getAllUsers() throws Exception {
        String url = baseUrl + "/users";
        HttpRequest request = authorizedRequestBuilder(url)
                .GET()
                .build();
        System.out.println("Get all users request - URL: " + url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get all users response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() == 200) {
            return Arrays.asList(objectMapper.readValue(response.body(), UserResponseDTO[].class));
        } else {
            throw new RuntimeException("Failed to fetch all users: " + response.body());
        }
    }

    public UserResponseDTO updateUser(Long id, UserRegistrationRequest requestBody) throws Exception {
        String json = objectMapper.writeValueAsString(requestBody);
        String url = baseUrl + "/users/" + id;
        HttpRequest httpRequest = authorizedRequestBuilder(url)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        System.out.println("Update user request - URL: " + url);
        System.out.println("Update user request - Body: " + json);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Update user response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserResponseDTO.class);
        } else {
            throw new RuntimeException("Update user failed: " + response.body());
        }
    }

    public void deleteUser(Long id) throws Exception {
        String url = baseUrl + "/users/" + id;
        HttpRequest request = authorizedRequestBuilder(url)
                .DELETE()
                .build();
        System.out.println("Delete user request - URL: " + url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete user response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() != 204) {
            throw new RuntimeException("Delete user failed: " + response.body());
        }
    }

    public List<Ride> getAllRides() throws Exception {
        String url = baseUrl + "/rides";
        HttpRequest request = authorizedRequestBuilder(url)
                .GET()
                .build();
        System.out.println("Get all rides request - URL: " + url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get all rides response - Status: " + response.statusCode() + ", Body: " + response.body());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Ride.class));
        } else {
            throw new RuntimeException("Failed to fetch rides: " + response.body());
        }
    }
}