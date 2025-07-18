package com.localride.client.service;

import com.localride.common.model.Passenger;
import com.localride.common.model.Driver;
import com.localride.common.model.Ride;
import com.localride.common.model.enums.DriverStatus;
import com.localride.common.model.enums.PassengerStatus;
import com.localride.common.model.enums.RideStatus;
import com.localride.client.model.filter.RideFilter;
import com.localride.client.util.LanguageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
     * @param driver    the driver assigned to the ride
     * @return the created Ride object
     */
    public Ride createRide(Passenger passenger, Driver driver) {
        if (passenger.getStatus() != PassengerStatus.REQUESTED) {
            //System.out.println("❗ Passenger " + passenger.getName() + " is not in REQUESTED status.");
            System.out.println(LanguageManager.get("passengerNotRequested",passenger.getName()));
            return null; // either return null or throw an exception
        }
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            //System.out.println("❗ Driver " + driver.getName() + " is not in AVAILABLE status.");
            System.out.println(LanguageManager.get("driverNotAvailableStatus", driver.getName()));
            return null; // either return null or throw an exception
        }
        Ride ride = new Ride(passenger, driver);

        passenger.setStatus(PassengerStatus.ASSIGNED);
        driver.setStatus(DriverStatus.ASSIGNED);

        rides.add(ride);
        //System.out.println("✅ Ride " + ride.getId() + " created for Passenger " + passenger.getName() +
        //        " and Driver " + driver.getName() + ". Status: " + ride.getStatus());
        System.out.println(LanguageManager.get("rideCreatedSuccess", ride.getId(), passenger.getName(), driver.getName(), ride.getStatus()));

        return ride;
    }

    /**
     * Ends the given ride by updating its status and end time.
     *
     * @param ride the ride to be ended
     */
    public void endRide(Ride ride) {
        if (ride == null) {
            //System.out.println("❗ Cannot end a null ride.");
            System.out.println(LanguageManager.get("cannotEndNullRide"));
            return;
        }
        if (ride.getStatus() == RideStatus.IN_PROGRESS) {
        	ride.endRide();
            ride.getPassenger().setStatus(PassengerStatus.NOTREQUESTED);
            ride.getDriver().setStatus(DriverStatus.AVAILABLE);
            //System.out.println("🏁 Ride " + ride.getId() + " completed! " + ride.getPassenger().getName() +
            //        " arrived. 🎉 Status: " + ride.getStatus());
            System.out.println(LanguageManager.get("rideCompleted", ride.getId(), ride.getPassenger().getName(), ride.getStatus()));
        } else {
            //System.out.println("❗ Cannot end ride " + ride.getId() + ". Current status: " + ride.getStatus() +
            //        ". Must be IN_PROGRESS.");
            System.out.println(LanguageManager.get("cannotEndRide", ride.getId(), ride.getStatus()));
        }
    }


    /**
     * Accept the given ride by updating its status
     *
     * @param ride the ride to accepted if possible
     */
    public void acceptRide(Ride ride) {
        if (ride == null) {
            //System.out.println("❗ Cannot accept a null ride.");
            System.out.println(LanguageManager.get("cannotAcceptNullRide"));
            return;
        }
        if ((ride.getStatus() == RideStatus.REQUESTED) || (ride.getStatus()==RideStatus.PENDING)) {
            ride.setStatus(RideStatus.ACCEPTED);
            //System.out.println("✅ Ride accepted for " + ride.getPassenger().getName());
            System.out.println(LanguageManager.get("rideAccepted", ride.getPassenger().getName()));
        } else {
            //System.out.println("❗ Cannot accept ride. Current status: " + ride.getStatus());
            System.out.println(LanguageManager.get("cannotAcceptRideStatus", ride.getStatus().toString()));
        }
    }
    /**
     * Starts the given ride if it is in ACCEPTED status.
     * Updates the ride status to IN_PROGRESS and logs the action.
     *
     * @param ride the ride to be started
     */

    public void startRide(Ride ride) {
        if (ride == null) {
            //System.out.println("❗ Cannot start a null ride.");
            System.out.println(LanguageManager.get("cannotStartNullRide"));
            return;
        }
        if (ride.getStatus() == RideStatus.ACCEPTED) {
            ride.startRide();
            //System.out.println("▶️ Ride " + ride.getId() + " started! " + ride.getDriver().getName() +
            //        " is driving " + ride.getPassenger().getName() + ". Status: " + ride.getStatus());
            System.out.println(LanguageManager.get("rideStarted", ride.getId(),
                    ride.getDriver().getName(), ride.getPassenger().getName(), ride.getStatus()));
        } else {
            //System.out.println("❗ Ride " + ride.getId() + " must be ACCEPTED before starting. Current status: " +
            //        ride.getStatus());
            System.out.println(LanguageManager.get("rideMustBeAccepted", ride.getId(), ride.getStatus()));
        }
    }

    /**
     * Returns a list of all rides.
     *
     * @return a list containing all rides
     */
    public List<Ride> getAllRides() {
        return rides;
    }

    public Ride getRandomRequestedRide() {
        int length = this.rides.size();

        if (length == 0) {
            return null;
        } else {
            //return this.rides.getLast();
            return getRandomRide(RideStatus.REQUESTED);
        }
    }

    /**
     * Returns a random ride from the list of all rides.
     * If no rides are available, returns null.
     *
     * @return a random ride or null if no rides exist
     */
    public Ride getRandomRide() {
        Ride ride;
        if (rides.isEmpty()) return null;
        //do{
        Random random = new Random();
        ride = rides.get(random.nextInt(rides.size()));
        //}while(driver.getStatus() != BussinessPersonStatus.BUSSY);
        return ride;
    }

    /**
     * Returns a random ride that matches the provided status.
     * If no rides match, returns null.
     *
     * @param status the status to filter rides by
     * @return a random ride that matches the status or null if no matches found
     */
    public Ride getRandomRide(RideStatus status) {
        RideFilter filter = new RideFilter(status);
        return getRandomRide(filter);
    }

    /**
     * Returns a random ride that matches the provided filter criteria.
     * If no rides match, returns null.
     *
     * @param filter the filter criteria to apply
     * @return a random ride that matches the filter or null if no matches found
     */
    public Ride getRandomRide(RideFilter filter) {

        List<Ride> filtered = rides.stream()
                .filter(ride -> {
                    //filter based on RideFilter criteria
                    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
                        if (!filter.getStatuses().contains(ride.getStatus())) {
                            return false;
                        }
                    }

                    // fil
                    if (filter.getPassenger() != null && !ride.getPassenger().equals(filter.getPassenger())) {
                        return false;
                    }

                    // filter based on driver
                    if (filter.getDriver() != null && !ride.getDriver().equals(filter.getDriver())) {
                        return false;
                    }

                    // filter based on status
                    if (filter.getAllowBusyReservation() != null && !filter.getAllowBusyReservation()) {
                        if (ride.getStatus() == RideStatus.IN_PROGRESS ||
                                ride.getStatus() == RideStatus.COMPLETED ||
                                ride.getStatus() == RideStatus.CANCELLED) {
                            return false;
                        }
                    }


                    return true;
                })
                .toList();

        if (filtered.isEmpty()) return null;

        return filtered.get(new Random().nextInt(filtered.size()));
    }

    /**
     * Cancels the given ride and updates its status.
     * The ride can only be cancelled if it is not already completed or cancelled.
     *
     * @param ride         the ride to be cancelled
     * @param cancelledBy  the entity that is cancelling the ride (e.g. "Passenger", "Driver", "System")
     */
    public void cancelRide(Ride ride, String cancelledBy) {
        if (ride == null || ride.getStatus() == RideStatus.CANCELLED || ride.getStatus() == RideStatus.COMPLETED
                || ride.getStatus() == RideStatus.IN_PROGRESS) {
            //System.out.println("⚠️ Cannot cancel ride. It's already completed or cancelled.");
            System.out.println(LanguageManager.get("cannotCancelRideFinal"));
            return;
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledBy(cancelledBy);
        ride.getPassenger().setStatus(PassengerStatus.NOTREQUESTED);
        ride.getDriver().setStatus(DriverStatus.AVAILABLE);

        //System.out.println("❌ Ride " + ride.getId() + " cancelled by " + cancelledBy);
        System.out.println(LanguageManager.get("rideCancelledBy", ride.getId(), cancelledBy));
    }

    /**
     * Filters rides based on the provided filter criteria and allowed statuses.
     * Returns a random ride from the filtered list or null if no rides match.
     *
     * @param filter          the filter criteria to apply
     * @param allowedStatuses the list of allowed ride statuses
     * @return a random ride that matches the filter or null if no matches found
     */
    public List<Ride> filterRides(RideFilter filter, List<RideStatus> allowedStatuses) {
        List<Ride> filtered = rides.stream()
                .filter(ride -> allowedStatuses == null || allowedStatuses.contains(ride.getStatus()))
                .filter(ride -> filter.getPassenger() == null || ride.getPassenger().equals(filter.getPassenger()))
                .filter(ride -> filter.getDriver() == null || ride.getDriver().equals(ride.getDriver()))
                .toList();

        if (filtered.isEmpty()) return null;

        return Collections.singletonList(filtered.get(new Random().nextInt(filtered.size())));
    }

}
