package com.example.connect;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;

public class connect_sqpserver {

    public static void main(String[] args) {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("DESKTOP-7Q188UV\\SQLEXPRESS");
        ds.setDatabaseName("QuanLyNhaHang");
        ds.setUser("sa");
        ds.setPassword("pass");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);

        try(Connection con = ds.getConnection()) {
            System.out.println("Connected to database");
        } catch (SQLServerException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
