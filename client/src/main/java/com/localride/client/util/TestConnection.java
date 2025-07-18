package com.localride.client.util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Connected to PostgreSQL!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}