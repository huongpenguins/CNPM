package com.example.dal;
import com.example.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CanHoDAL extends Admin {


    // Constructor should not have a return type and should match the class name
    public CanHoDAL() {
        super(); // Call the constructor of the parent class (Admin

    }

    /**
     * Hàm thêm dữ liệu vào bảng CanHo
     * @return true nếu thêm thành công, false nếu thất bại
     */
    static String tableName = "canhotbl";
    public boolean insertCanHo() {
        String[] columns = {"MaCanHo","MaHoKhau","TenCanHo","Tang","DienTich","MoTa"};
        String[] types = {"String","String","String","int","float","String"};

        return insert(tableName, columns, types); // Gọi hàm insert từ Admin
    }
    public static boolean updateCanHo(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {
        return update(tableName, columnName, newValue, conditionColumn,conditionValue);
    }

 //delete du lieu
    //kiem tra khoa ngoai
    public boolean checkForeignKey(String tableName, String columnName, String value) {
        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";

        try (PreparedStatement stmt = getConnectionAdmin().prepareStatement(query)) {
             stmt.setString(1, value); // Gán giá trị vào câu lệnh SQL
             try (ResultSet rs = stmt.executeQuery()) {
                 return rs.next(); // Trả về true nếu giá trị có tồn tại
             }
        } catch (SQLException e) {
             System.err.println("Lỗi khi kiểm tra khóa ngoại: " + e.getMessage());
             return false;
        }
    }

    public boolean deleteCanHo(String tableName,String columnName,String columnValue){
        if (checkForeignKey("hogiadinhtbl", "MaCanHo", columnValue)) {
            System.err.println("Error: Không thể xóa vì MaCanHo đang được tham chiếu trong bảng hogiadinhtbl!");
            return false; // Nếu đang bị tham chiếu, không cho phép xóa
        }
        return delete(tableName,columnName,columnValue);
    }

    public ArrayList<Object[]> searchCanHo(String tableName, String columnName, String searchValue) {
        return search(tableName, columnName, searchValue);
    }
}
