package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class NhanKhauDAL extends Admin {


    //kiểm tra khóa ngoại chung
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

       static String tableName = "nhankhautbl";

// Insert dữ liệu
      public boolean insertNhanKhau(String[] tableName, String[] columns, String[] types) {
          int MaHoIndex = -1;
               for (int i =0; i< columns.length; i++){
                   if("MaHoGiadinh".equalsIgnoreCase(columns[i])){
                       MaHoIndex = i;
                       break;
               }
           }
           if (MaHoIndex == -1){
               System.err.println("Error: Cột MaHoGiaDinh bắt buộc phải có!");
               return false;
           }
           // kiểm tra khóa ngoại
          String MaHoGiaDinh = columns[MaHoIndex];
          if (!checkForeignKey("hogiadinhtbl", "MaHoGiaDinh", MaHoGiaDinh)) {
              System.err.println("Error: MaHoGiaDinh không tồn tại trong bảng hogiadinhtbl");
               return false;
          }
                       return super.insert("nhankhautbl", columns, types);
       }

// Update dữ liệu
    public boolean updateNhanKhau(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException{
           //kiem tra khoa ngoai neu cap nhat cot MaHoGiaDinh
            if ("MaHoGiaDinh".equalsIgnoreCase(columnName)) {
                if (!checkForeignKey("hogiadinhtbl", "MaHoGiaDinh", newValue)) {
                    System.err.println("Error: MaHoGiaDinh mới không tồn tại trong bảng hogiadinhtbl!");
                    return false;
                }
            }
           //gọi phuơng thức update từ lớp cha Admin
           return super.update("nhakhautbl", columnName, newValue, conditionColumn, conditionValue);
    }

// delete dữ liệu

    // Kiểm tra khóa chính MaNhanKhau có bị tham chiếu trong các bảng khac không
    private boolean isReferencedInOtherTables(String tableName, String columnName, String value){
           if(checkForeignKey("tamvangtbl", columnName, value)){
               return true;
           }
           if(checkForeignKey("tamvangtbl", columnName, value)){
               return true;
           }
           return false;
    }

    public boolean deleteNhanKhau(String conditionColumn, String conditionValue) {
        // Kiểm tra xem MaNhanKhau có đang bị tham chiếu ở các bảng TamTru và TamVang không
        if ("MaNhanKhau".equalsIgnoreCase(conditionColumn)) {
            if (isReferencedInOtherTables("tamtrutbl", conditionColumn, conditionValue) ||
                    isReferencedInOtherTables("tamvangtbl", conditionColumn, conditionValue)) {
                System.err.println("Error: Không thể xóa vì MaNhanKhau đang được tham chiếu trong bảng tamtrutbl hoặc tamvangtbl!");
                return false; // Không xóa nếu có tham chiếu
            }
        }
        // Thực hiện delete nếu không có tham chiếu
        return super.delete("nhankhautbl", conditionColumn, conditionValue);
    }


//search dữ liệu
    public ArrayList<Object[]> searchNhanKhau(String tableName, String columnName, String searchValue){
      /*  if ("MaHoGiaDinh".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey(searchValue)) {
                System.err.println("Error: Mã hộ gia đình không tồn tại trong bảng HoGiaDinh!");
                return null;
            }
        }
       */
        //khong kiem tra khoa ngoai vi tim kiem chi dua ra danh sach khong anh huong den du lieu
        return super.search("nhankhautbl", columnName, searchValue);
    }

}
