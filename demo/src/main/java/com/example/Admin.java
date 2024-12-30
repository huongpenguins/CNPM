package com.example;
import com.example.connect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    private static Connection connection_admin;
    protected connect_mysql c_mysql;

    // phương thức lấy kết nối
    public Connection getConnectionAdmin(){
        return connection_admin;
    }

    public Admin() {
        c_mysql = new connect_mysql(); // Kết nối MySQL
        connection_admin = c_mysql.getConnection();
    }
//    public boolean isAuthenticated() {
//        // Xác thực tài khoản admin (dùng giá trị cứng)
//        return "admin".equals(username) && "admin123".equals(password);
//    }


    /**
     * Hàm insert chung, nhập dữ liệu trực tiếp từ bàn phím
     *
     * @param tableName Tên bảng cần chèn dữ liệu
     * @param columns   Danh sách các cột cần chèn
     * @param types     Kiểu dữ liệu tương ứng của các cột ("String", "int", "float", "long", "boolean")
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
    /**
     * Vùng 2: Chức năng sửa dữ liệu
     *
     * @param tableName       Tên bảng cần sửa dữ liệu
     * @param columnName      Tên cột cần sua
     * @param newValue        Giá trị mới nhập từ bàn phím
     * @param conditionColumn Ten cot thay the
     * @param conditionValue  dieu kien where
     * @return true nếu sửa thành công, false nếu thất bại
     */
    // Kiểm tra quyền admin

    // Hàm cập nhật chung
    public static boolean update(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) {
        // Kiểm tra quyền trước khi thực hiện
//        if (!isAuthenticated()) {
//            System.out.println("Bạn không có quyền thực hiện thao tác này.");
//            return false;
//        }

        // Tạo câu lệnh SQL động
        String query = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + conditionColumn + " = ?";

        try (PreparedStatement preparedStatement = connection_admin.prepareStatement(query)) {
            // Gán giá trị tham số vào câu lệnh SQL
            preparedStatement.setString(1, newValue);  // Thay thế giá trị cột
            preparedStatement.setString(2, conditionValue); // Điều kiện WHERE

            // Thực thi câu lệnh UPDATE
            int rowsAffected = preparedStatement.executeUpdate();

            // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Xử lý ngoại lệ trong quá trình cập nhật
            System.err.println("Lỗi khi cập nhật dữ liệu: " + e.getMessage());
            return false;
        }
    }

    // method xoa du lieu theo hang
    public static boolean delete(String tableName, String conditionColumn, String ColumnValue) {
        String query = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";
        try (PreparedStatement preparedStatement = connection_admin.prepareStatement(query)) {
            preparedStatement.setString(1, ColumnValue);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa dữ liệu: " + e.getMessage());
            return false;
        }
    }
    /**
     * Hàm tìm kiếm chung cho admin.
     *
     * @param tableName Tên bảng cần tìm kiếm dữ liệu
     * @param columnName Tên cột làm điều kiện tìm kiếm
     * @param searchValue Giá trị cần tìm kiếm
     * @return ArrayList<Object[]> chứa kết quả tìm kiếm (mỗi dòng là Object[])
     */
    public ArrayList<Object[]> search(String tableName, String columnName, String searchValue) {
        ArrayList<Object[]> resultList = new ArrayList<>();

        // Nếu cột hoặc giá trị bị null => lấy all
        if (columnName == null || searchValue == null) {
            String sqlAll = "SELECT * FROM " + tableName;
            try (PreparedStatement preStatement = connection_admin.prepareStatement(sqlAll);
                 ResultSet resultSet = preStatement.executeQuery()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    resultList.add(row);
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi load ALL dữ liệu: " + e.getMessage());
            }
            return resultList;
        }

        // Ngược lại, search = cột = value
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement preStatement = connection_admin.prepareStatement(sql)) {
            preStatement.setString(1, searchValue);
            ResultSet resultSet = preStatement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm dữ liệu: " + e.getMessage());
        }
        return resultList;
    }


}


