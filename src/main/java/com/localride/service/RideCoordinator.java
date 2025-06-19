package com.localride.service;
// This class is currently unused and redundant with RideManager.
// If future features like ride assignment algorithms are added, it may be reintroduced.

import com.localride.model.Driver;
import com.localride.model.Passenger;
import com.localride.model.Ride;
// TODO: Remove unused Class RideCoordinator if not needed
public class RideCoordinator {
    private final RideManager rideManager;

    // TODO: Remove unused method RideCoordinator if not needed
    public RideCoordinator(RideManager rideManager ){
        this.rideManager = rideManager;
    }

    // TODO: Remove unused method coordinatrRide if not needed
    public Ride coordinatrRide(Passenger passenger, Driver driver){
        System.out.println("Coordinate ride between passenger " + passenger.getName() +
                " and driver " + driver.getName());
        Ride ride = new Ride(passenger, driver) ;
        ride.startRide();
        rideManager.getAllRides().add(ride) ;
        return ride;
    }

    // TODO: Remove unused method completeRide if not needed
    public void completeRide(Ride ride){
        ride.endRide();
    }
}
