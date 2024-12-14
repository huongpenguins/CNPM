package com.example;
import com.example.connect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {
    private Connection connection_admin;
    protected connect_mysql c_mysql;

    public Admin() {
        c_mysql = new connect_mysql(); // Kết nối MySQL
        connection_admin = c_mysql.getConnection();
    }

    /**
     * Hàm insert chung, nhập dữ liệu trực tiếp từ bàn phím
     * @param tableName Tên bảng cần chèn dữ liệu
     * @param columns Danh sách các cột cần chèn
     * @param types Kiểu dữ liệu tương ứng của các cột ("String", "int", "float", "long", "boolean")
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean insert(String tableName, String[] columns, String[] types) {
        if (columns.length != types.length) {
            throw new IllegalArgumentException("Số lượng cột và kiểu dữ liệu không khớp!");
        }

        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        // Tạo câu lệnh SQL cho các cột
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");

        // Tạo câu lệnh SQL cho các giá trị (?)
        for (int i = 0; i < columns.length; i++) {
            query.append("?");
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (Scanner scanner = new Scanner(System.in);
             PreparedStatement preparedStatement = connection_admin.prepareStatement(query.toString())) {

            System.out.println("Nhập thông tin cần thêm vào bảng " + tableName + ":");

            for (int i = 0; i < types.length; i++) {
                System.out.print("Nhập giá trị cho cột " + columns[i] + " (" + types[i] + "): ");
                String input = scanner.nextLine();

                switch (types[i].toLowerCase()) {
                    case "string":
                        preparedStatement.setString(i + 1, input);
                        break;
                    case "int":
                        preparedStatement.setInt(i + 1, Integer.parseInt(input));
                        break;
                    case "float":
                        preparedStatement.setFloat(i + 1, Float.parseFloat(input));
                        break;
                    case "long":
                        preparedStatement.setLong(i + 1, Long.parseLong(input));
                        break;
                    case "boolean":
                        preparedStatement.setBoolean(i + 1, Boolean.parseBoolean(input));
                        break;
                    default:
                        throw new IllegalArgumentException("Kiểu dữ liệu không được hỗ trợ: " + types[i]);
                }
            }

            // Thực hiện truy vấn
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm dữ liệu: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi nhập dữ liệu: " + e.getMessage());
            return false;
        }
    }
}
