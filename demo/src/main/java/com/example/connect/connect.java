package com.example.connect;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class connect {
    public static void main(String[] args) throws SQLException {
        var url1 = "jdbc:mysql://localhost:3306/quanlychungcu";
        var user1 = "root";
        var password1 = "2003";
        try(Connection conn = DriverManager.getConnection(url1,user1,password1);){
            System.out.println("connect the database");
        }catch (SQLException e){
            System.err.println("connect failed: "+ e.getMessage());
        }


    }
}