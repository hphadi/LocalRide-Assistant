package com.localride.service;

import com.localride.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
