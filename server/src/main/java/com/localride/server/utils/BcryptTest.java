package com.localride.server.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hashedPassword = encoder.encode(password);
        System.out.println(hashedPassword);
    }
}
