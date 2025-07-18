package com.localride.common.model.enums;

public enum DriverStatus{
    DRIVING,//ACCEPTED,STARTING,
    AVAILABLE,//PENDING,
    ACCEPTED, //RIDE ACCEPTED by driver
    ASSIGNED,//ASSIGNED to a ride, but not yet started
    AWAY,//Driver is away from the app or not available for rides
    OFFLINE,//Driver is offline
    CANCELLED,//Ride cancelled by driver
    COMPLETED//Ride completed
}
