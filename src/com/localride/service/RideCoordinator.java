package com.localride.service;

import com.localride.model.Driver;
import com.localride.model.Passenger;
import com.localride.model.Ride;

public class RideCoordinator {
    private final RideManager rideManager;

    public RideCoordinator(RideManager rideManager ){
        this.rideManager = rideManager;
    }

    public Ride coordinatrRide(Passenger passenger, Driver driver){
        System.out.println("Coordinate ride between passenger " + passenger.getName() +
                " and driver " + driver.getName());
        Ride ride = new Ride(passenger, driver) ;
        ride.startRide();
        rideManager.getAllRides().add(ride) ;
        return ride;
    }

    public void completeRide(Ride ride){
        ride.endRide();
    }
}
