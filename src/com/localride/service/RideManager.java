package com.localride.service;

import com.localride.model.Passenger;
import com.localride.model.Driver;
import com.localride.model.Ride;
import com.localride.model.enums.RideStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * RideManager handles the lifecycle of a ride:
 * creation, acceptance, start, and completion.
 *
 * Responsibilities:
 * - Creating rides between passengers and drivers
 * - Transitioning ride status (e.g. accept, start, end)
 * - Logging actions and enforcing rules
 *
 * Dependencies:
 * - Ride
 * - Passenger
 * - Driver
 * </pre>
 */
public class RideManager {

    /**
     * Internal list of all rides that have been created.
     */
    private final List<Ride> rides = new ArrayList<>();

    /**
     * Creates a new ride with the given passenger and driver,
     * adds it to the ride list, and returns the created ride.
     *
     * @param passenger the passenger who requests the ride
     * @param driver the driver assigned to the ride
     * @return the created Ride object
     */
    public Ride createRide(Passenger passenger, Driver driver) {
        Ride ride = new Ride(passenger, driver);
        rides.add(ride);
        return ride;
    }

    /**
     * Ends the given ride by updating its status and end time.
     *
     * @param ride the ride to be ended
     */
    public void endRide(Ride ride) {
        ride.endRide();
    }


    /**
     * Accept the given ride by updating its status
     *
     * @param ride the ride to accepted if possible
     */
    public void acceptRide(Ride ride) {
        if (ride.getStatus() == RideStatus.REQUESTED) {
            ride.setStatus(RideStatus.ACCEPTED);
            System.out.println("✅ Ride accepted for " + ride.getPassenger().getName());
        } else {
            System.out.println("❗ Cannot accept ride. Current status: " + ride.getStatus());
        }
    }

    /**
     * Start accepted ride and updating status in Ride class
     *
     * @param ride the ride starting
     */

    public void startRide(Ride ride) {
        if (ride.getStatus() == RideStatus.ACCEPTED) {
            ride.startRide();
        } else {
            System.out.println("❗ Ride must be accepted before starting.");
        }
    }

    /**
     * Retrieves all ride instances created so far.
     *
     * @return list of all rides
     */
    public List<Ride> getAllRides() {
        return rides;
    }
}
