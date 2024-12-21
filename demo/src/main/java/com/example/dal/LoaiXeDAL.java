package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class LoaiXeDAL extends Admin {
    public LoaiXeDAL(){
        super(); // ke thua Admin
    }

    static String tableName = "loaixetbl";

    public boolean insertLoaiXe(){
        String[] columns = {"MaLoaiXe", "LoaiXe"};
        String[] types = {" String", "String"};
        return insert(tableName, columns, types);
    }

    public static boolean updateLoaiXe(String tableName, String columnName, String newValue, String conditionColumn,String conditionValue) throws SQLException{
        return update(tableName, columnName,newValue, conditionColumn, conditionValue);
    }

//delete du lieu
    // kiem tra khoa ngoai
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

    public boolean deleteLoaiXe(String tableName, String columnName, String columnValue){
        // Kiểm tra xem MaLoaiXe có đang bị tham chiếu trong bảng Xe không
        if (checkForeignKey("Xe", "MaLoaiXe", columnValue)) {
            System.err.println("Error: Không thể xóa vì MaLoaiXe đang được tham chiếu trong bảng Xe!");
            return false; // Nếu đang bị tham chiếu, không cho phép xóa
        }

        return super.delete(tableName, columnName, columnValue);
    }

}
