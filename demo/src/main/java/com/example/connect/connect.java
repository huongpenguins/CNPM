package com.example.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanlychungcu"; // Thay đổi nếu cần
        String username = "root";  // Tên người dùng
        String password = "2103";  // Mật khẩu người dùng

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối MySQL thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối MySQL thất bại!");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
}
