package com.localride.model.enums;

public enum DriverStatus{
    DRIVING,//ACCEPTED,STARTING,
    AVAILABLE,//PENDING,
    ACCEPTED, //RIDE ACCEPTED by driver
    ASSIGNED,//ASSIGNED to a ride, but not yet started
    OFFLINE,//Driver is offline
    CANCELLED,//Ride cancelled by driver
    COMPLETED//Ride completed
}
