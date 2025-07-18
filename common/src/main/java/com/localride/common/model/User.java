package com.localride.common.model;

public class User {

    private String name;
    private String role; // Passenger, Driver, Admin, support, guest

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

}
