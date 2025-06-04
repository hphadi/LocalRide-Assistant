package com.localride.model;

/**
 * Represents a passenger in the ride-hailing system.
 * A passenger can request a ride.
 */
public class Passenger {
    String name;
    String phoneNumber;

    /**
     * Constructor with name and phone number.
     * @param name Passenger's name
     * @param phoneNumber Passenger's contact number
     */
    public Passenger(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Constructor with only name.
     * @param name Passenger's name
     */
    public Passenger(String name) {
        this.name = name;
        this.phoneNumber = "000-000000"; // default value
    }

    /**
     * Simulates the passenger requesting a ride.
     */
    public void requestRide() {

        System.out.println(name + " ride requested.");
    }

    // Getters (optional for future use)
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
