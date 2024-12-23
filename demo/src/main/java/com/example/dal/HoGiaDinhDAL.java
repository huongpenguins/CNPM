package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class HoGiaDinhDAL extends Admin{

    static String tableName = "hogiadinhtbl";

    // Kiểm tra khóa ngoại chung
    public boolean checkForeignKey(String tableName, String columnName, String value) {
        String query = "SELECT 1 FROM " + tableName+ " WHERE " + columnName + " = ?";
        try(Connection conn = getConnectionAdmin();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,value);
            try(ResultSet rs = stmt.executeQuery()){
                return rs.next(); // trả về true nếu khóa ngoại tồn tại
            }
        } catch(SQLException e){
            System.err.println("Lỗi khi kiểm tra khóa ngoại: " +e.getMessage());
            return false;
        }
    }

// Kiểm tra khóa ngoại cho từng cột
    private boolean validateForeignKeys(String columnName, String value) {
        if ("MaXe".equalsIgnoreCase(columnName)) {
            return checkForeignKey("xetbl", "MaXe", value);
        } else if ("MaCanHo".equalsIgnoreCase(columnName)) {
            return checkForeignKey("canhotbl", "MaCanHo", value);
        }
        return true; // Nếu không phải khóa ngoại cần kiểm tra
    }

// Insert dữ liệu

    public boolean insertHoGiaDinh( String tableName, String[] columns, String[] values) {

        for (int i = 0; i < columns.length; i++) {
            if (!validateForeignKeys(columns[i], values[i])) {
                System.err.println("Error: Giá trị" + values[i] + "không tồn tại trong bảng liên quan!");
                return false;
            }
        }
        return super.insert("hogiadinhtbl", columns, values);
    }

// Update dữ liệu
    public boolean updateHoGiaDinh(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) {
        // Kiểm tra khóa ngoại cho MaCanHo và MaXe

        if ("MaCanHo".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("canhotbl", "MaCanHo", newValue)) {
                System.err.println("Error: MaCanHo không tồn tại trong bảng canhotbl!");
                return false;
            }
        }else if("MaXe".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("xetbl", "MaXe", newValue)) {
                System.err.println("Error: MaXe không tồn tại trong bảng xetbl!");
                return false;
            }
        }
        return super.update("nhankhautbl", columnName, newValue, conditionColumn, conditionValue);
    }

// Delete dữ liệu

    // kiem tra tham chieu trong bang NhanKhau va ChiTietKhoaThu
     private boolean isReferencedInOtherTables(String columnName, String conditionColumn, String conditionValue) {
          // Kiểm tra tham chiếu trong bảng 'NhanKhau'
          if (checkForeignKey("nhankhautbl", "MaHoGiaDinh", conditionValue)) {
              return true; // Bảng NhanKhau tham chiếu đến MaHoGiaDinh
          }
          // Kiểm tra tham chiếu trong bảng 'ChiTietKhoanThu'
          if (checkForeignKey("chitietkhoanthutbl", "MaHoGiaDinh", conditionValue)) {
              return true; // Bảng ChiTietKhoanThu tham chiếu đến MaHoGiaDinh
          }
          return false; // Không bị tham chiếu ở bảng nào khác
     }

     public boolean deleteHoGiaDinh(String tableName, String conditionColumn, String conditionValue) {

         // Kiểm tra xem MaHoGiaDinh có đang bị tham chiếu ở các bảng NhanKhau và chiTietKhoanThu không
         if ("MaHoGiaDinh".equalsIgnoreCase(conditionColumn)) {
             if (isReferencedInOtherTables("nhankhautbl", conditionColumn, conditionValue) ||
                     isReferencedInOtherTables("chitietkhoanthutbl", conditionColumn, conditionValue)) {
                 System.err.println("Error: Không thể xóa vì MaHoGiaDinh đang được tham chiếu trong bảng nhanhkhautbl hoặc chitietkhoanthutbl!");
                 return false; // Không xóa nếu có tham chiếu
             }
         }
         return super.delete("hogiadinhtbl", conditionColumn, conditionValue);
     }

     //Search  dữ liệu
     public ArrayList<Object[]> searchHoGiaDinh(String tableName, String columnName, String searchValue) {
         return super.search(tableName, columnName, searchValue);
     }

}
