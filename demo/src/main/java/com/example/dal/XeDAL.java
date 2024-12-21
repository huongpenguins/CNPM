package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

public class XeDAL extends Admin {

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

    static String tableName = "xetbl";

    //insert du lieu
    public boolean insertXe(String[] tableName, String[] columns, String[] types) {
        int MaLoaiXeIndex = -1;
        for (int i =0; i< columns.length; i++){
            if("MaLoaiXe".equalsIgnoreCase(columns[i])){
                MaLoaiXeIndex = i;
                break;
            }
        }
        if (MaLoaiXeIndex == -1){
            System.err.println("Error: Cột MaLoaiXe bắt buộc phải có!");
            return false;
        }
        // kiểm tra khóa ngoại
        String MaLoaiXe = columns[MaLoaiXeIndex];
        if(!checkForeignKey("LoaiXe", "MaLoaiXe", MaLoaiXe)){
            System.err.println("Error: MaLoaiXe không tồn tại trong bảng LoaiXe");
            return false;
        }
        return super.insert("Xe", columns, types);
    }

    //update du lieu
    public boolean updateXe(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException{
        //kiem tra khoa ngoai neu cap nhat cot MaLoaiXe
        if ("MaLoaiXe".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("LoaiXe", "MaLoaiXe", newValue)) {
                System.err.println("Error: MaLoaiXe mới không tồn tại trong bảng LoaiXe!");
                return false;
            }
        }
        //gọi phuơng thức update từ lớp cha Admin
        return super.update("Xe", columnName, newValue, conditionColumn, conditionValue);
    }

    //delete du lieu

    public boolean deleteXe(String tableName, String columnName, String columnValue){
        // Kiểm tra xem MaXe có đang bị tham chiếu trong bảng HoGiaDinh không
        if (checkForeignKey("HoGiaDinh", "MaXe", columnValue)) {
            System.err.println("Error: Không thể xóa vì MaXe đang được tham chiếu trong bảng HoGiaDinh!");
            return false; // Nếu đang bị tham chiếu, không cho phép xóa
        }

        return super.delete(tableName, columnName, columnValue);
    }

    //search du lieu
    public ArrayList<Object[]> searchXe(String tableName, String columnName, String searchValue) {
        return super.search(tableName, columnName, searchValue);
    }
}
