package com.localride.common.model.enums;

public enum Role {
    ADMIN,
    PASSENGER,
    DRIVER,
    SUPPORT,
    GUEST,
    USER,
    CASHIER;

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}