package com.example.dal;
import com.example.Admin;

import java.util.ArrayList;
import java.sql.SQLException;


public class ChiTietKhoanThuDAL extends Admin {
    /*
    // kiem tra khoa ngoai chung
    public boolean checkForeignKey(String tableName, String columnName, String value) {
        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";
        try(Connection conn = getConnectionAdmin();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,value);
            try(ResultSet rs = stmt.executeQuery()){
                return rs.next(); // trả về true nếu khóa ngoại tồn tại
            }
        } catch(SQLException e){
            System.err.println("Lỗi khi kiểm tra khóa ngoại : " +e.getMessage());
            return false;
        }
    }

// kiem tra khoa ngoai cho tung cot
    private boolean validateForeignKeys(String columnName, String value) {
        if ("MaKhoanThu".equalsIgnoreCase(columnName)) {
            return checkForeignKey("KhoanThu", "MaKhoanThu", value);
        } else if ("MaHoGiaDinh".equalsIgnoreCase(columnName)) {
            return checkForeignKey("HoGiaDinh", "MaHoGiaDinh", value);
        }
        return true; // Nếu không phải khóa ngoại cần kiểm tra
    }

    //insert du lieu

    public boolean insertChiTietKhoanThu( String tableName, String[] columns, String[] values) {

        for (int i = 0; i < columns.length; i++) {
            if (!validateForeignKeys(columns[i], values[i])) {
                System.err.println("Error: Giá trị" + values[i] + "không tồn tại trong bảng liên quan!");
                return false;
            }
        }
        return super.insert("ChiTietKhoanThu", columns, values);
    }

    //update du lieu
    public boolean updateChiTietKhoanThu(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) {
        // Kiểm tra khóa ngoại cho MaKhoanThu và MaHoGiaDinh

        if ("MaKhoanThu".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("KhoanThu", "MaKhoanThu", newValue)) {
                System.err.println("Error: MaKhoanThu không tồn tại trong bảng KhoanThu!");
                return false;
            }
        }else if("MaHoGiaDinh".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("HoGiaDinh", "MaHoGiaDinh", newValue)) {
                System.err.println("Error: MaHoGiaDinh không tồn tại trong bảng HoGiaDinh!");
                return false;
            }
        }
        return super.update("ChiT", columnName, newValue, conditionColumn, conditionValue);
    } ChiTietKhoanThu chi co tinh nang TimKiem
    */

    //search du lieu
    public ArrayList<Object[]> searchChiTietKhoanThu(String tableName, String columnName, String searchValue) {
        return super.search(tableName, columnName, searchValue);
    }






}
