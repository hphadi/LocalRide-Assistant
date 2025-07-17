package com.localride.server.model;

public enum Role {
    ADMIN,
    PASSENGER,
    DRIVER,
    SUPPORT,
    GUEST,
    CASHIER;

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}