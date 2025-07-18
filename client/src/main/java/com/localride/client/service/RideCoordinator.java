package com.localride.client.service;
// This class is currently unused and redundant with RideManager.
// If future features like ride assignment algorithms are added, it may be reintroduced.

import com.localride.common.model.Driver;
import com.localride.common.model.Passenger;
import com.localride.common.model.Ride;
import com.localride.client.util.LanguageManager;

// TODO: Remove unused Class RideCoordinator if not needed
public class RideCoordinator {
    private final RideManager rideManager;

    // TODO: Remove unused method RideCoordinator if not needed
    public RideCoordinator(RideManager rideManager ){
        this.rideManager = rideManager;
    }

    // TODO: Remove unused method coordinatrRide if not needed
    public Ride coordinatrRide(Passenger passenger, Driver driver){
        //System.out.println("Coordinate ride between passenger " + passenger.getName() +
        //        " and driver " + driver.getName());
        System.out.println(LanguageManager.get("coordinateRide",passenger.getName(),driver.getName()));
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
