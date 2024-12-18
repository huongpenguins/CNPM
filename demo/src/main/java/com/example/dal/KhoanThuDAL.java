package com.example.dal;
import com.example.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhoanThuDAL extends Admin {
    public KhoanThuDAL(){
        super(); // ke thua Admin
    }

    static String tableName = "khoanthutbl";

    public boolean insertKhoanThu() {
        String[] columns = {"MaKhoanThu", "TenKhoanThu", "ThoiGianBatDau", "ThoiGiaKetThuc", "LoaiKhoanThu", "ChiTiet", "Ghichu"};
        String[] types = {"int", "String", "LocalDateTime", "LocalDateTime", "String", "String", "String"};
        return insert(tableName, columns, types);
    }

    public static boolean updateKhoanThu(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {
        return update(tableName, columnName, newValue, conditionColumn, conditionValue);
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

    public boolean deleteKhoanThu(String tableName, String columnName, String columnValue) {
        if (checkForeignKey("ChiTietKhoanThu", "MaKhoanThu", columnValue)) {
            System.err.println("Error: Không thể xóa vì MaKhoanThu đang được tham chiếu trong bảng ChiTietKhoanThu!");
            return false; // Nếu đang bị tham chiếu, không cho phép xóa
        }
        return delete(tableName, columnName, columnValue);
    }

    public ArrayList<Object[]> searchKhoanThu(String tableName, String columnName, String searchValue) {
        return search(tableName, columnName, searchValue);
    }
}
