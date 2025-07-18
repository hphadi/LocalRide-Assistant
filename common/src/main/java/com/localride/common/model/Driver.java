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
package com.localride.common.model;

import com.localride.common.model.enums.DriverStatus;
import com.localride.common.model.enums.PassengerStatus;
import com.localride.common.model.enums.RideStatus;

/**
 * Represents a driver in the ride-hailing system.
 * A driver can accept a ride request.
 */
public class Driver {
    private static int idCounter = 1;
    private final int id;
    String name;
    String carModel;
    DriverStatus status;
    Integer rate;


    /**
     * Constructor with name and car information.
     *
     * @param name     Driver's name
     * @param carModel Driver's car model or description
     */
    public Driver(String name, String carModel) {
        this.name = name;
        this.carModel = carModel;
        this.id = idCounter;
        this.status = DriverStatus.AVAILABLE;
        this.rate = 0;
        idCounter++;
    }

    /**
     * Constructor with only name.
     *
     * @param name Driver's name
     */
    public Driver(String name) {
        this.name = name;
        this.carModel = "Unknown"; // default value
        this.id = idCounter;
        this.status = DriverStatus.AVAILABLE;
        this.rate = 0;
        idCounter++;
    }

    /**
     * Simulates the driver accepting a ride request (general).
     */
//    public boolean acceptRide() {
//        if (this.status != DriverStatus.AVAILABLE) {
//            System.out.println("Driver " + name + " with id : " + id + " is not available now;");
//            //System.out.println(LanguageManager.get("driverNotAvailable", name, id));
//            return false;
//        }
//        this.status = DriverStatus.DRIVING;
//        System.out.println(name + " with car " + carModel + " and id: " + id + " accepted the Ride");
//        //System.out.println(LanguageManager.get("driverAcceptedRide", name, carModel, id));
//        return true; // Indicate success
//    }

    /**
     * Simulates the driver accepting a ride from a specific passenger.
     *
     * @param passenger The passenger who requested the ride
     */
    public void acceptRide(Passenger passenger) {
        if (this.status != DriverStatus.AVAILABLE) {
            System.out.println("Driver " + name + " with id : " + id + " is not available now;");
            //System.out.println(LanguageManager.get("driverNotAvailable", name, id));
            return;
        }
        System.out.println(name + " accepted the ride for " + passenger.getName());
        //System.out.println(LanguageManager.get("driverAcceptedRideSimple", this.name, passenger.getName()));
    }

    /**
     * Accepts a given ride and updates its status to ACCEPTED.
     * This method is called by the driver to confirm the ride.
     *
     * @param ride The ride that the driver accepts
     */
    // Corrected Driver.java (focusing on acceptRide(Ride ride))
    public boolean acceptRide(Ride ride) { // Changed return type to boolean
        if (this.status != DriverStatus.AVAILABLE) {
            System.out.println("Cannot accept ride. Driver " + name + " is not available now; Status: " + this.status);
            return false; // Indicate failure
        }
        if (ride.getStatus() != RideStatus.REQUESTED) {
            System.out.println("Cannot accept ride. Current ride status: " + ride.getStatus());
            return false; // Indicate failure
        }

        System.out.println(this.name + " accepted the ride for " + ride.getPassenger().getName());
        ride.setStatus(RideStatus.ACCEPTED);
        this.status = DriverStatus.DRIVING; // Driver is now driving
        ride.getPassenger().setStatus(PassengerStatus.INRIDE); // Passenger is now in ride

        return true; // Indicate success
    }

    // Getters (optional)
    public String getName() {
        return name;
    }

    // TODO: Remove unused method getCar if not needed
    public String getCarModel() {
        return carModel;
    }

    public int getId() {
        return id;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public Integer getRating() {
        return rate;
    }

    public void cancelRide(){
        System.out.println(name + " ride Cancel.");
        //System.out.println(LanguageManager.get("rideCancelled", name));

    }

    public void setStatus(DriverStatus driverStatus) {
        status = driverStatus;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", carModel='" + carModel + '\'' +
                ", status=" + status +
                ", rate=" + rate +
                '}';
    }


    /**
     * Simulates the cancel of the ride.
     */
//    public void cancelRide(CancelRole cancelRole) {
//        if (status == RideStatus.REQUESTED || status == RideStatus.ACCEPTED) {
//            if(status == RideStatus.ACCEPTED){
//                if(driver.getStatus()==DriverStatus.DRIVING){
//                    driver.status=DriverStatus.AVAILABLE;
//                }
//                if(passenger.status== PassengerStatus.INRIDE){
//                    passenger.status = PassengerStatus.NOTREQUESTED;
//                }
//            }
//
//            this.status = RideStatus.CANCELLED;
//            this.cancelRole = cancelRole;
//
//            System.out.println("Ride has been cancelled ❌");
//        } else {
//            System.out.println("Cannot cancel ride. Current status: " + status);
//        }
//    }
}
