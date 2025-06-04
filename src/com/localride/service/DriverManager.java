package com.localride.service;

import com.localride.model.Driver;

import java.util.ArrayList;
import java.util.List;

public class DriverManager {
    private final List<Driver> drivers = new ArrayList<>();

    public void addDriver(Driver driver){
        drivers.add(driver);
    }

    public List<Driver> getAllDrivers(){
        return drivers;
    }
}
