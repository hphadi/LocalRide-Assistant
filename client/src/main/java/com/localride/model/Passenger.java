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
 * Represents a passenger in the ride-hailing system.
 * A passenger can request a ride.
 */
public class Passenger {
    private static int idCounter = 1;
    private final int id;
    String name;
    String phoneNumber;
    PassengerStatus status;
    Integer rate;

    /**
     * Constructor with name and phone number.
     *
     * @param name        Passenger's name
     * @param phoneNumber Passenger's contact number
     */
    public Passenger(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = idCounter;
        this.status = PassengerStatus.NOTREQUESTED;
        this.rate = 0;
        idCounter++;
    }

    /**
     * Constructor with only name.
     *
     * @param name Passenger's name
     */
    public Passenger(String name) {
        this.name = name;
        this.phoneNumber = "000-000000"; // default value
        this.id = idCounter;
        this.status = PassengerStatus.NOTREQUESTED;
        this.rate = 0;
        idCounter++;
    }

    /**
     * Simulates the passenger requesting a ride.
     */
    public void requestRide() {
        System.out.println(name + " ride requested.");
        status = PassengerStatus.REQUESTED;
    }

    // Getters (optional for future use)
    public String getName() {
        return name;
    }

    // TODO: Remove unused method getPhoneNumber if not needed
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }

    public PassengerStatus getStatus() {
        return status;
    }

    public Integer getRating() {
        return rate;
    }

    public void cancelRide(){
        System.out.println(name + " ride Cancel.");

    }

    public void setStatus(PassengerStatus passengerStatus) {
        status = passengerStatus;
    }
    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
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
//                if(driver.getStatus()== DriverStatus.DRIVING){
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
