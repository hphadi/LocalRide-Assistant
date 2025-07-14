package com.localride.service;

import com.localride.model.Passenger;
import com.localride.model.enums.PassengerStatus;
import com.localride.model.filter.PassengerFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassengerManager {
    private final List<Passenger> passengers = new ArrayList<>();

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    // TODO: Remove unused method getAllPassengers if not needed
    public List<Passenger> getAllPassengers() {
        return passengers;
    }

    public Passenger getRandomPassenger() {
        Passenger passenger;
        if (passengers.isEmpty()) {
            return null;
        } else {
            //do {
                Random random = new Random();
                passenger = passengers.get(random.nextInt(passengers.size()));
            //} while (passenger.getStatus() != status);
            return passenger;
        }
    }

    public Passenger getRandomPassenger(PassengerFilter filter) {

//        List<Passenger> filtered = passengers.stream()
//                .filter(Passenger -> {
//                    if (filter.getStatus() != null && Passenger.getStatus() != filter.getStatus())
//                        return false;
//                    if (filter.getMinRating() != null && Passenger.getRating() < filter.getMinRating())
//                        return false;
//                    return filter.getAllowBusyReservation() || Passenger.getStatus() == PassengerStatus.NOTREQUESTED
//                            || Passenger.getStatus() == PassengerStatus.REQUESTED
//                            || Passenger.getStatus() == PassengerStatus.INRIDE;
//                })
//                .toList();
        List<Passenger> filtered = passengers.stream()
                .filter(passenger -> {
                    // (status)
                    if (filter.getStatus() != null && passenger.getStatus() != filter.getStatus()) {
                        return false; // if status does not match, reject
                    }

                    // (minRating)
                    if (filter.getMinRating() != null && passenger.getRating() < filter.getMinRating()) {
                        return false; // if rating is below minimum, reject
                    }

                    // (allowBusyReservation)
                    if (!filter.getAllowBusyReservation() && (passenger.getStatus() == PassengerStatus.INRIDE
                            /*|| passenger.getStatus() == PassengerStatus.CANCELLED*/)) {
                        return false; // if not allowing busy reservations and passenger is in ride, reject
                    }

                    // passenger available for reservation
                    return true;
                })
                .toList();

        if (filtered.isEmpty()) return null;

        return filtered.get(new Random().nextInt(filtered.size()));
    }

}
