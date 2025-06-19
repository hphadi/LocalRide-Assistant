/*
 * Copyright 2025 Hadi Pirhadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.localride.model;

import com.localride.model.enums.*;

/**
 * Represents a ride between a passenger and a driver.
 */
public class Ride {
    private static int idCounter = 1;
    private final int id;
    Passenger passenger;
    Driver driver;
    private boolean rideActiveated;
    private RideStatus status;
    private long startTime;
    private long endTime;
    CancelRole cancelRole;
    private CancelReason cancelReason;
    private String cancelledBy;

    /**
     * Constructor to create a ride instance.
     *
     * @param passenger The passenger who is taking the ride
     * @param driver    The driver who is providing the ride
     */
    public Ride(Passenger passenger, Driver driver) {
        this.passenger = passenger;
        this.driver = driver;
        this.rideActiveated = false;
        this.status = RideStatus.PENDING;
        this.id = idCounter;
        this.cancelRole = null;
        this.startTime = 0;
        this.endTime = 0;
        idCounter++;
    }

    /**
     * Simulates the start of the ride.
     */
    public void startRide() {
        if (status == RideStatus.ACCEPTED || status == RideStatus.REQUESTED) {
            rideActiveated = true;
            startTime = System.currentTimeMillis();
            status = RideStatus.IN_PROGRESS;
            passenger.status=PassengerStatus.INRIDE;
            driver.status=DriverStatus.DRIVING;
            System.out.println("🚗 Starting ride for " + passenger.name + " with driver " + driver.name +
                    " in " + driver.carModel + " ride status : " + status);
        } else {
            System.out.println("Cannot start ride. Current status: " + status);
        }
    }

    /**
     * Simulates the end of the ride.
     */
    public void endRide() {
        if (isRideActive() || status == RideStatus.IN_PROGRESS) {
            rideActiveated = false;
            status = RideStatus.COMPLETED;
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
    public void cancelRide(CancelRole cancelRole) {
        if (status == RideStatus.REQUESTED || status == RideStatus.ACCEPTED) {
            if(status == RideStatus.ACCEPTED){
                if(driver.getStatus()==DriverStatus.DRIVING){
                    driver.status=DriverStatus.AVAILABLE;
                }
                if(passenger.status==PassengerStatus.INRIDE){
                    passenger.status = PassengerStatus.NOTREQUESTED;
                }
            }

            this.status = RideStatus.CANCELLED;
            this.cancelRole = cancelRole;

            System.out.println("Ride with id " + this.id + " has been cancelled ❌");
        } else {
            System.out.println("Cannot cancel ride with id " + this.id + " . Current status: " + status);
        }
    }

    /**
     * Calculates a fare depend on duration of ride.
     * In future versions, this can be updated based on distance, time, etc.
     *
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

    public Driver getDriver() {
        return this.driver;
    }

    public int getId() {
        return id;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public boolean isRideActive() {
        return rideActiveated;
    }

    public CancelRole getCancelRole() {
        return cancelRole;
    }
    public void setCancelRole(CancelRole cancelRole) {
        this.cancelRole = cancelRole;
    }
    public CancelReason getCancelReason() {
        return cancelReason;
    }
    public void setCancelReason(CancelReason cancelReason) {
        this.cancelReason = cancelReason;
    }
    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", passenger=" + passenger +
                ", driver=" + driver +
                ", rideActiveated=" + rideActiveated +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", cancelRole=" + cancelRole +
                ", cancelReason=" + cancelReason +
                ", cancelledBy='" + cancelledBy + '\'' +
                '}';
    }
}
