package com.localride.model.filter;

import com.localride.model.enums.PassengerStatus;

public class PassengerFilter {
    private PassengerStatus status;
    private Integer minRating;
    private Boolean allowBusyReservation;

    // Constructors
    public PassengerFilter(PassengerStatus status) {
        this.status = status;
    }

    public PassengerFilter(PassengerStatus status, Integer minRating) {
        this.status = status;
        this.minRating = minRating;
    }

    public PassengerFilter(PassengerStatus status, Integer minRating, Boolean allowBusyReservation) {
        this.status = status;
        this.minRating = minRating;
        this.allowBusyReservation = allowBusyReservation;
    }


    // Getters and setters
    public PassengerStatus getStatus() {
        return status;
    }

    public void setStatus(PassengerStatus status) {
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
