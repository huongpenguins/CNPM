package com.example.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect_mysql {
    private static final String URL = "jdbc:mysql://localhost:3306/quanlychungcu";
    private static final String USER = "root";
    private static final String PASSWORD = "2003";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Kết nối MySQL thất bại: " + e.getMessage());
            return null;
        }
    }
}
