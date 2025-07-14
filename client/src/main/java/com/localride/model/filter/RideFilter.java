package com.localride.model.filter;

import com.localride.model.Driver;
import com.localride.model.Passenger;
import com.localride.model.enums.RideStatus;

import java.util.Collections;
import java.util.List;

public class RideFilter {
    private RideStatus status;
    private Integer minRating;
    private Boolean allowBusyReservation;
    private Passenger passenger;
    private Driver driver;
    private List<RideStatus> statuses;// List of statuses to filter rides by
    // Constructors
    public RideFilter(RideStatus status) {
        this.status = status;
        this.statuses = Collections.singletonList(status);
        this.allowBusyReservation = false;
    }

    public RideFilter(List<RideStatus> statuses, Boolean allowBusyReservation) {
        this.statuses = statuses;
        this.allowBusyReservation = allowBusyReservation;
    }

    public RideFilter(List<RideStatus> statuses) {
        this.statuses = statuses;
        this.allowBusyReservation = false;
    }

    public RideFilter(RideStatus status, Boolean allowBusyReservation) {
        this.status = status;
        this.allowBusyReservation = allowBusyReservation;
        this.statuses = Collections.singletonList(status);
    }

    public RideFilter(RideStatus status, Integer minRating) {
        this.status = status;
        this.statuses = Collections.singletonList(status);
        this.minRating = minRating;
    }

    public RideFilter(List<RideStatus> statuses, Passenger passenger, Driver driver) {
        this.statuses = statuses;
        this.passenger = passenger;
        this.driver = driver;
    }

    public RideFilter(RideStatus status, Integer minRating, Boolean allowBusyReservation) {
        this.status = status;
        this.statuses = Collections.singletonList(status);
        this.minRating = minRating;
        this.allowBusyReservation = allowBusyReservation;
    }

    // Getters and setters
    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
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
    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<RideStatus> getStatuses() {
        return statuses;
    }
    public void setStatuses(List<RideStatus> statuses) {
        this.statuses = statuses;
    }

}
