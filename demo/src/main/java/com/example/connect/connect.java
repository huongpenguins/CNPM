package com.example.connect;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class connect {
    public static void main(String[] args) throws SQLException {
        var url = "jdbc:mysql://localhost:3306/quanlychungcu";
        var user = "root";
        var password = "2003";
        try(Connection conn = DriverManager.getConnection(url,user,password);){
            System.out.println("connect the database");
        }catch (SQLException e){
            System.err.println("connect failed: "+ e.getMessage());
        }
    }
}