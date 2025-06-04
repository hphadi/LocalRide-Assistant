package com.localride.model;

import com.localride.model.enums.RideStatus;

/**
 * Represents a driver in the ride-hailing system.
 * A driver can accept a ride request.
 */
public class Driver {
    String name;
    String carModel;

    /**
     * Constructor with name and car information.
     * @param name Driver's name
     * @param carModel Driver's car model or description
     */
    public Driver(String name, String carModel) {
        this.name = name;
        this.carModel = carModel;
    }

    /**
     * Constructor with only name.
     * @param name Driver's name
     */
    public Driver(String name) {
        this.name = name;
        this.carModel = "Unknown"; // default value
    }

    /**
     * Simulates the driver accepting a ride request (general).
     */
    public void acceptRide() {
        System.out.println(name + " with car " + carModel + " accepted the Ride");
    }

    /**
     * Simulates the driver accepting a ride from a specific passenger.
     * @param passenger The passenger who requested the ride
     */
    public void acceptRide(Passenger passenger) {
        System.out.println(name + " accepted the ride for " + passenger.getName());
    }

    /**
     * Accepts a given ride and updates its status to ACCEPTED.
     * This method is called by the driver to confirm the ride.
     *
     * @param ride The ride that the driver accepts
     */
    public void acceptRide(Ride ride) {
        if (ride.getStatus() != RideStatus.REQUESTED) {
            System.out.println("❗ Cannot accept ride. Current status: " + ride.getStatus());
            return;
        }

        System.out.println(this.name + " accepted the ride for " + ride.getPassenger().getName());
        ride.setStatus(RideStatus.ACCEPTED);
    }

    // Getters (optional)
    public String getName() {
        return name;
    }

    public String getCar() {
        return carModel;
    }
}
