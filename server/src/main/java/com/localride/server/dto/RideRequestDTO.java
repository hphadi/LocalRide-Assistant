package com.localride.server.dto;

public class RideRequestDTO {
    private Long passengerId;
    private String statLocation;
    private String endLocation;

    // Getters and Setters
    public Long getPassengerId() {
        return passengerId;
    }
    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }
    public String getStatLocation() {
        return statLocation;
    }
    public void setStatLocation(String statLocation) {
        this.statLocation = statLocation;
    }
    public String getEndLocation() {
        return endLocation;
    }
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartLocation() {
        return statLocation;
    }
}
