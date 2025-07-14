package com.localride.service;

import com.localride.model.Driver;
import com.localride.model.enums.DriverStatus;
import com.localride.model.filter.DriverFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DriverManager {
    private final List<Driver> drivers = new ArrayList<>();

    public void addDriver(Driver driver){
        drivers.add(driver);
    }

    // TODO: Remove unused method getAllDrivers if not needed
    public List<Driver> getAllDrivers(){
        return drivers;
    }

    public Driver getRandomDriver() {
        Driver driver;
        if (drivers.isEmpty()) return null;
        //do{
        Random random = new Random();
        driver = drivers.get(random.nextInt(drivers.size()));
        //}while(driver.getStatus() != BussinessPersonStatus.BUSSY);
        return driver;
    }
    public Driver getRandomDriver(DriverStatus status) {
        Driver driver;
        if (drivers.isEmpty()) return null;
        do{
            Random random = new Random();
            driver = drivers.get(random.nextInt(drivers.size()));
        }while(driver.getStatus() != status);
        return driver;
    }

    public Driver getRandomDriver(DriverFilter filter) {

        List<Driver> filtered = drivers.stream()
                .filter(driver -> {
                    if (filter.getStatus() != null && driver.getStatus() != filter.getStatus())
                        return false;
                    if (filter.getMinRating() != null && driver.getRating() < filter.getMinRating())
                        return false;
                    if (!filter.getAllowBusyReservation() && driver.getStatus() != DriverStatus.AVAILABLE)
                        return false;
                    return true;
                })
                .toList();

        if (filtered.isEmpty()) return null;

        return filtered.get(new Random().nextInt(filtered.size()));
    }
}
