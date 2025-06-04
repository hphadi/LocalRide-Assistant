package com.localride.model;

import com.localride.model.enums.RideStatus;
/**
 * Represents a ride between a passenger and a driver.
 */
public class Ride {
    Passenger passenger;
    Driver driver;
    boolean isRideActive;
    private RideStatus status;
    private long startTime;
    private long endTime;


    /**
     * Constructor to create a ride instance.
     * @param passenger The passenger who is taking the ride
     * @param driver The driver who is providing the ride
     */
    public Ride(Passenger passenger, Driver driver) {
        this.passenger = passenger;
        this.driver = driver ;
        this.isRideActive = false;
        this.status = RideStatus.REQUESTED;
    }

    /**
     * Simulates the start of the ride.
     */
    public void startRide(){
        if (status == RideStatus.ACCEPTED || status == RideStatus.REQUESTED) {
            isRideActive = true;
            startTime = System.currentTimeMillis();
            status = RideStatus.IN_PROGRESS;
            System.out.println("🚗 Starting ride for " + passenger.name + " with driver " + driver.name + " in " + driver.carModel + " ride status : " + status);
        }
        else {
            System.out.println("Cannot start ride. Current status: " + status);
        }
    }

    /**
     * Simulates the end of the ride.
     */
    public void endRide() {
        if (isRideActive || status == RideStatus.IN_PROGRESS) {
            isRideActive = false;
            status = RideStatus.COMPLETED ;
            endTime = System.currentTimeMillis();
            double fare = calculateFare();
            System.out.println("🏁 Ride ended with status " + status + " . Fare for " + passenger.name + " is: $" + fare);
        } else {
            System.out.println("❗ Ride is not active. Or Cannot start ride. Current status: " + status);
        }
    }

    /**
     * Simulates the cancel of the ride.
     */
    public void cancelRide() {
        if (status == RideStatus.REQUESTED || status == RideStatus.ACCEPTED) {
            this.status = RideStatus.CANCELLED;
            System.out.println("Ride has been cancelled ❌");
        } else {
            System.out.println("Cannot cancel ride. Current status: " + status);
        }
    }

    /**
     * Calculates a fare depend on duration of ride.
     * In future versions, this can be updated based on distance, time, etc.
     * @return timeable fare as a double value
     */
    private double calculateFare() {
        long durationInSeconds = (endTime - startTime) / 1000;
        double fare = 10.0 + (durationInSeconds * 0.5);
        return Math.round(fare * 100.0) / 100.0; // Round to 2 decimal places
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }
}
