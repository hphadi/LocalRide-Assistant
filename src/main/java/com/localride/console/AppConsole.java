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
package com.localride.console;

import com.localride.model.Driver;
import com.localride.model.Passenger;
import com.localride.model.Ride;
import com.localride.model.enums.DriverStatus;
import com.localride.model.enums.PassengerStatus;
import com.localride.model.enums.RideStatus;
import com.localride.model.filter.DriverFilter;
import com.localride.model.filter.RideFilter;
import com.localride.service.DriverManager;
import com.localride.service.PassengerManager;
import com.localride.service.RideManager;
import com.localride.model.filter.PassengerFilter;

import java.util.List;
import java.util.Scanner;

/**
 * <pre>
 *     Console UI
 *     start a simple UI to make LocalRideAssistant interactive
 *     There are almost all methods need to complete
 *
 *     -addPassenger
 *     -addDriver
 *     -createRide
 *     -acceptRide
 *     -startRide
 *     -endRide
 *     -listRides
 * </pre>
 */
public class AppConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final DriverManager driverManager = new DriverManager();
    private final PassengerManager passengerManager = new PassengerManager();
    private final RideManager rideManager = new RideManager();

    /**
     * <pre>
     *     run Methode shows the core available structure in
     *     LocalRideAssistant
     *
     *     User can chose what he wants to do with our system LocalRideAssistant
     *
     * </pre>
     */
    public void run(){
        while (true){
            showMenu();
            int choice = getUserInput();
            switch (choice){
                case 1:
                    addPassenger();
                    break;
                case 2:
                    addDriver();
                    break;
                case 3:
                    passengerRequested();
                    break;
                case 4:
                    createRide();
                    break;
                case 5 :
                    acceptRide();
                    break;
                case 6 :
                    startRide();
                    break;
                case 7 :
                    cancelRide();
                    break;
                case 8 :
                    endRide();
                    break;
                case 9 :
                    listPassengers();
                    break;
                case 10 :
                    listDrivers();
                    break;
                case 11 :
                    listRides();
                    break;
                case 0 :
                    System.out.println("Thank you to use our Ride application.");
                    return;
                default:
                    System.out.println("Please select a valid number, try again ...");
                    break;
            }
        }
    }

    /**
     * <pre>
     *     Three entities
     * </pre>
     */
    private void cancelRide() {
        List<RideStatus> rideStatuses = List.of(RideStatus.REQUESTED, RideStatus.PENDING, RideStatus.ACCEPTED);
        RideFilter filterForCancellation = new RideFilter(rideStatuses);
        Ride r = rideManager.getRandomRide(filterForCancellation);
        if (r == null) {
            System.out.println("❗ No REQUESTED ride available to cancel.");
            return;
        }

        System.out.println("Who wants to cancel the ride? (passenger / driver / system)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("passenger") || input.equals("driver") || input.equals("system")) {
            rideManager.cancelRide(r, input);
        } else {
            System.out.println("Invalid option.");
        }
    }


    /**
     * <pre>
     *     First simple Menu
     *
     *     User can see and chose the available function till now
     *     with console app
     * </pre>
     */
    private void showMenu() {
        System.out.println("\n========== Local Ride Assistant ==========");
        System.out.println("1. Add Passenger");
        System.out.println("2. Add Driver");
        System.out.println("3. Request Ride");
        System.out.println("4. Create Ride");
        System.out.println("5. Accept Ride");
        System.out.println("6. Start Ride");
        System.out.println("7. Cancel Ride");
        System.out.println("8. End Ride");
        System.out.println("9. List Passengers");
        System.out.println("10. List Drivers");
        System.out.println("11. List Rides");
        System.out.println("0. Exit");
        System.out.print("➤ Ple1ase select your choice: ");
    }

    /**
     * <pre>
     *     User select the number from ShowMenu
     *     if he could not have the right number then system return a default value (-1)
     * </pre>
     *
     * @return Integer number : the value from a numbers appear in showMenu
     */
    private int getUserInput(){
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear input buffer
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    /**
     * <pre>
     * Adds a passenger to the system.
     * Prompts the user for the passenger's name and creates a new Passenger object.
     * The passenger is then added to the PassengerManager.
     * </pre>
     */
    private void addPassenger() {
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine();
        Passenger p = new Passenger(name);
        passengerManager.addPassenger(p);
        System.out.println("✅ Passenger added: " + name + " with id: " + p.getId());
    }

    /**
     * <pre>
     * Adds a driver to the system.
     * Prompts the user for the driver's name and creates a new Driver object.
     * The driver is then added to the DriverManager.
     * </pre>
     */
    private void addDriver() {
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();
        Driver d = new Driver(name);
        driverManager.addDriver(d);
        System.out.println("✅ Driver added: " + name + " with id: " + d.getId());
    }
    /**
     * <pre>
     * Requests a ride for a random passenger with NOTREQUESTED status.
     * If no such passenger exists, it informs the user.
     * </pre>
     */
    private void passengerRequested() {
        PassengerFilter notrequestedPassengerFilter = new PassengerFilter(PassengerStatus.NOTREQUESTED);
        Passenger p = passengerManager.getRandomPassenger(notrequestedPassengerFilter);
        if (p == null) {
            System.out.println("❗ There are no Passenger with NOTREQUESTED status.");
            return;
        }
        p.requestRide();
        System.out.println("Passenger with id " + p.getId() + " and name " + p.getName()
                +" with rate "+p.getRating()+" a Ride requested.");
    }


    /**
     * <pre>
     * Creates a ride by selecting a random passenger with REQUESTED status
     * and a random driver with AVAILABLE status.
     * If either is not available, it informs the user.
     * </pre>
     */
    private void createRide() {
        PassengerFilter requestedPassengerFilter = new PassengerFilter(PassengerStatus.REQUESTED);
        Passenger p = passengerManager.getRandomPassenger(requestedPassengerFilter);
        if (p == null) {
            System.out.println("❗ There are no Passenger with REQUESTED status.");
            return;
        }
        DriverFilter availableDriverFilter = new DriverFilter(DriverStatus.AVAILABLE);
        Driver d = driverManager.getRandomDriver(availableDriverFilter);
        if( d == null) {
            System.out.println("❗ There are no Driver with AVAILABLE status.");
            return;
        }
        if (p == null || d == null) {
            System.out.println("❗ There are not enough Passenger or Driver .");
            return;
        }
        Ride r = rideManager.createRide(p, d);
        System.out.println("🚗 Ride created: " + r.getId() + " | " + p.getName() + " → " + d.getName());
    }

    /**
     * <pre>
     * Accepts a ride if it is in PENDING or REQUESTED status.
     * If a ride is accepted, it prints the details of the ride.
     * If no ride is available, it informs the user.
     * </pre>
     */
    private void acceptRide() {
        List<RideStatus> rideStatuses = List.of(RideStatus.PENDING, RideStatus.REQUESTED);
        RideFilter filterForAcceptance  = new RideFilter(rideStatuses);

        Ride r = rideManager.getRandomRide(filterForAcceptance);
        if (r != null && ((r.getStatus()== RideStatus.REQUESTED)|| (r.getStatus()== RideStatus.PENDING))) {
            rideManager.acceptRide(r);
            System.out.println("🚕 Ride build between Passenger: " + r.getPassenger().getName() + " and Driver: "
                    + r.getDriver().getName() + " (Ride ID: " + r.getId() + ")");
        } else {
            System.out.println("No Ride available ❗");
        }
    }

    /**
     * <pre>
     *
     * </pre>
     */
    private void startRide() {
        RideFilter acceptedRideFilter = new RideFilter(RideStatus.ACCEPTED);
        Ride r = rideManager.getRandomRide(acceptedRideFilter);
        if (r != null) {
            rideManager.startRide(r);
        } else {
            System.out.println("No Ride available ❗");
        }
    }

    /**
     * <pre>
     * Ends a ride if it is in progress.
     * If a ride is ended, it prints the details of the ride.
     * If no ride is available, it informs the user.
     * </pre>
     */
    private void endRide() {
        RideFilter rideFilter = new RideFilter(RideStatus.IN_PROGRESS, true);
        Ride ride = rideManager.getRandomRide(rideFilter);
        //Ride r = rideManager.getLastRide();
        if (ride != null) {
            rideManager.endRide(ride);
        } else {
            System.out.println("No Ride available ❗");
        }
    }

    /**
     * <pre>
     * Lists all rides in the system.
     * If no rides are available, it informs the user.
     * </pre>
     */
    private void listRides() {
        for (Ride r : rideManager.getAllRides()) {
            System.out.println("🛣️ " + r);
        }
    }

    /**
     * <pre>
     * Lists all drivers in the system.
     * If no drivers are available, it informs the user.
     * </pre>
     */
    private void listDrivers() {
        for (Driver d : driverManager.getAllDrivers()) {
            System.out.println("🚗 " + d);
        }
    }

    /**
     * <pre>
     * Lists all passengers in the system.
     * If no passengers are available, it informs the user.
     * </pre>
     */
    private void listPassengers() {
        for (Passenger p : passengerManager.getAllPassengers()) {
            System.out.println("👤 " + p);
        }
    }
}
