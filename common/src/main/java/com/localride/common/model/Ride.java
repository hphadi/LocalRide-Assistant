package com.localride.common.model;

import com.localride.common.model.enums.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ride {
    private static int idCounter = 1;
    private final int id;
    private Passenger passenger;
    private Driver driver;
    private boolean rideActivated;
    private RideStatus status;
    private long startTime;
    private long endTime;
    private CancelRole cancelRole;
    private CancelReason cancelReason;
    private String cancelledBy;
    private String location; // Added for POINT(x y)

    public Ride(Passenger passenger, Driver driver) {
        this.passenger = passenger;
        this.driver = driver;
        this.rideActivated = false;
        this.status = RideStatus.PENDING;
        this.id = idCounter++;
        this.cancelRole = null;
        this.startTime = 0;
        this.endTime = 0;
        this.location = "POINT(0 0)"; // Default location
    }

    public boolean startRide() {
        if (status == RideStatus.ACCEPTED || status == RideStatus.REQUESTED) {
            rideActivated = true;
            startTime = System.currentTimeMillis();
            status = RideStatus.IN_PROGRESS;
            passenger.setStatus(PassengerStatus.INRIDE);
            driver.setStatus(DriverStatus.DRIVING);
            System.out.println("Ride starting with passenger: " + passenger.getName() +
                    ", driver: " + driver.getName() + ", car model: " + driver.getCarModel() +
                    ", status: " + status);
            return true;
        } else {
            System.out.println("Cannot start ride. Current status: " + status);
            return false;
        }
    }

    public boolean endRide() {
        if (status == RideStatus.IN_PROGRESS) {
            rideActivated = false;
            status = RideStatus.COMPLETED;
            endTime = System.currentTimeMillis();
            double fare = calculateFare();
            System.out.println("Ride ended for " + passenger.getName() + ". Fare: $" + fare);
            return true;
        } else {
            System.out.println("Cannot end ride. Current status: " + status);
            return false;
        }
    }

    public boolean cancelRide(CancelRole cancelRole) {
        if (status == RideStatus.REQUESTED || status == RideStatus.ACCEPTED) {
            if (status == RideStatus.ACCEPTED) {
                if (driver.getStatus() == DriverStatus.DRIVING) {
                    driver.setStatus(DriverStatus.AVAILABLE);
                }
                if (passenger.getStatus() == PassengerStatus.INRIDE) {
                    passenger.setStatus(PassengerStatus.NOTREQUESTED);
                }
            }
            this.status = RideStatus.CANCELLED;
            this.cancelRole = cancelRole;
            System.out.println("Ride with id " + this.id + " has been cancelled ❌");
            return true;
        } else {
            System.out.println("Cannot cancel ride with id " + this.id + " in status: " + status);
            return false;
        }
    }

    private double calculateFare() {
        long durationInSeconds = (endTime - startTime) / 1000;
        double fare = 10.0 + (durationInSeconds * 0.5);
        return Math.round(fare * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Ride{id=" + id + ", passenger=" + passenger + ", driver=" + driver +
                ", rideActivated=" + rideActivated + ", status=" + status +
                ", startTime=" + startTime + ", endTime=" + endTime +
                ", cancelRole=" + cancelRole + ", cancelReason=" + cancelReason +
                ", cancelledBy='" + cancelledBy + "', location='" + location + "'}";
    }

    public boolean isRideActive() {
        return rideActivated && (status == RideStatus.IN_PROGRESS || status == RideStatus.ACCEPTED);
    }
}