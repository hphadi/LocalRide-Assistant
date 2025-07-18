package com.localride.client.model.filter;

import com.localride.common.model.enums.DriverStatus;

public class DriverFilter {
    private DriverStatus status;
    private Integer minRating;
    private Boolean allowBusyReservation;

    /**
     * Default constructor for DriverFilter.
     * Initializes with default values.
     */
    public DriverFilter(DriverStatus status) {
        this.status = status;
    }

    public DriverFilter(DriverStatus status, Integer minRating) {
        this.status = status;
        this.minRating = minRating;
    }
    public DriverFilter(DriverStatus status, Integer minRating, Boolean allowBusyReservation) {
        this.status = status;
        this.minRating = minRating;
        this.allowBusyReservation = allowBusyReservation;
    }

    // Getters and setters
    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public Boolean getAllowBusyReservation() {
        return allowBusyReservation != null ? allowBusyReservation : false;
    }

    public void setAllowBusyReservation(Boolean allowBusyReservation) {
        this.allowBusyReservation = allowBusyReservation;
    }
}
