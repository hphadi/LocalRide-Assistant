package com.localride.service;

import com.localride.model.Passenger;

import java.util.ArrayList;
import java.util.List;

public class PassengerManager {
    private final List<Passenger> passengers = new ArrayList<>();

    public void addPassenger(Passenger passenger){
        passengers.add(passenger);
    }

    public List<Passenger> getAllPassengers(){
        return passengers;
    }
}
