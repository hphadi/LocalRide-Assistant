package com.localride.model.enums;

public enum PassengerStatus {
    INRIDE,//DRIVING,ACCEPTED,STARTING,
    NOTREQUESTED,//WAITING,
    REQUESTED,// PENDING, WAITING to accept by Driver
    ASSIGNED,// ASSIGNED to a ride, but not yet started
    CANCELLED,// RIDE CANCELLED by Passenger
    COMPLETED,// RIDE COMPLETED by Passenger
}