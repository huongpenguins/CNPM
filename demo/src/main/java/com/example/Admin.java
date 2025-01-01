package com.example;
import com.example.connect.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    protected static connect_mysql c_mysql = new connect_mysql();
    protected static Connection connection_admin = connect_mysql.getConnection();

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

public static boolean update1(String tableName, String[] columns, String[] types,String[] newValue,String condition) {
    if (columns.length != newValue.length) {
        throw new IllegalArgumentException("Số lượng cột và kiểu dữ liệu không khớp!");
    }

    StringBuilder query = new StringBuilder("UPDATE ");
    query.append(tableName).append(" SET ");

    // Tạo câu lệnh SQL cho các cột
    for (int i = 0; i < columns.length; i++) {
        query.append(columns[i]).append(" = ");
        if(types[i].equals("string")||types[i].equals("date")){
            query.append("'").append(newValue[i]).append("'");
        }
        else{
            query.append(newValue[i]);
        }
        if (i < columns.length - 1) {
            query.append(", ");
        }
    }
    query.append(" WHERE ").append(condition);

             try (Statement statement = connection_admin.createStatement()) {
                int rowsUpdated = statement.executeUpdate(query.toString());

                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật thành công!");
                     showAlert("Success", "Sửa dữ liệu thành công!", AlertType.INFORMATION);
                    return true;
                } else {
                  
                    System.out.println("Không có bản ghi nào được cập nhật.");
                    showAlert("Error", "Lỗi khi sửa dữ liệu: " , AlertType.ERROR);
                    return false;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        
}
    public static boolean insert1(String tableName, String[] columns,String[] types, String[] newValue) {
    if (columns.length != newValue.length) {
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

    // Tạo câu lệnh SQL cho các cột
    for (int i = 0; i < columns.length; i++) {
        if(types[i].equals("string")||types[i].equals("date")){
            query.append("'").append(newValue[i]).append("'");
        }
        else{
            query.append(newValue[i]);
        }
        if (i < columns.length - 1) {
            query.append(", ");
        }
    }
    query.append(")");
    

             try (Statement statement = connection_admin.createStatement()) {
                int rowsUpdated = statement.executeUpdate(query.toString());

                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật thành công!");
                    showAlert("Success", "Thêm dữ liệu thành công!", AlertType.INFORMATION);
                    return true;
                } else {
                    System.out.println("Không có bản ghi nào được cập nhật.");
                    showAlert("Error", "Lỗi khi thêm dữ liệu: " , AlertType.ERROR);
                    return false;
                }
              
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
}
    
// lay ma duoc tao tu dong duoc tqo tu trigger 
/**
 * 
 * @param tableName
 * @param MaMuonLay
 * @param columns cot dieu kien
 * @param types
 * @param value gia tri dieu kien
 * @return
 */
    public static String selectMaTrigger(String tableName,String MaMuonLay,String[]columns,String[] types,String[] value){
        String ma=null;
        StringBuilder query = new StringBuilder("SELECT ").append(MaMuonLay).append( " FROM ");
        query.append(tableName).append(" WHERE ");
        
        // Tạo câu lệnh SQL cho các cột
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" = ");
            if(types[i].equals("string")||types[i].equals("date")){
                query.append("'").append(value[i]).append("'");
            }
            else{
                query.append(value[i]);
            }
            if (i < columns.length - 1) {
                query.append(" AND ");
            }
        }
    
                 try (Statement statement = connection_admin.createStatement()) {
                    ResultSet r = statement.executeQuery(query.toString());

                    while(r.next()){
                        ma = r.getString(MaMuonLay);
                    }
                    

                    
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ma;
            
    }

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

        // Phần tạo câu lệnh SQL giữ nguyên
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");

        for (int i = 0; i < columns.length; i++) {
            query.append("?");
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement preparedStatement = connection_admin.prepareStatement(query.toString())) {
            System.out.println("Nhập thông tin cần thêm vào bảng " + tableName + ":");
            Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < types.length; i++) {
                System.out.print("Nhập giá trị cho cột " + columns[i] + " (" + types[i] + "): ");
                String input = scanner.nextLine();

                // Chuẩn hóa kiểu dữ liệu thành chữ thường và bỏ khoảng trắng
                String normalizedType = types[i].toLowerCase().trim();

                switch (normalizedType) {
                    case "string":
                    case "varchar":
                    case "text":
                        preparedStatement.setString(i + 1, input);
                        break;
                    case "int":
                    case "integer":
                        // Xử lý trường hợp input rỗng cho các trường có thể NULL
                        if (input.trim().isEmpty()) {
                            preparedStatement.setNull(i + 1, java.sql.Types.INTEGER);
                        } else {
                            preparedStatement.setInt(i + 1, Integer.parseInt(input));
                        }
                        break;
                    case "datetime":
                    case "timestamp":
                        if (input.trim().isEmpty()) {
                            preparedStatement.setNull(i + 1, java.sql.Types.TIMESTAMP);
                        } else {
                            // Chuyển đổi chuỗi ngày tháng sang java.sql.Timestamp
                            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(input);
                            preparedStatement.setTimestamp(i + 1, timestamp);
                        }
                        break;
                    // Thêm các kiểu dữ liệu khác nếu cần
                    default:
                        throw new IllegalArgumentException("Kiểu dữ liệu không được hỗ trợ: " + types[i] +
                                " cho cột " + columns[i]);
                }
            }

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Thêm dữ liệu thành công!");
            }
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm dữ liệu: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi kiểu dữ liệu: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            return false;
        }
    }
    public boolean insertRecord(String tableName, String[] columns, String[] values) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Số lượng cột và giá trị không khớp!");
        }

        // Tạo câu lệnh INSERT INTO tableName (col1,col2,...) VALUES (?,?,...)
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            query.append("?");
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement preparedStatement = getConnectionAdmin().prepareStatement(query.toString())) {

            // Ở đây, để đơn giản, ta set tất cả dưới dạng string
            // MySQL sẽ tự ép kiểu (int, datetime...) nếu chuỗi đúng format
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm dữ liệu: " + e.getMessage());
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
    public static void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


