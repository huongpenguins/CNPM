package com.example.dal;
import com.example.Admin;
import com.example.Resident;
import com.example.Entities.NhanKhau;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class NhanKhauDAL extends Admin {

    public static Resident loadData(String condition){
        
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT * FROM NhanKhauTBL WHERE MaNhanKhau = ");
        query.append("'").append(condition).append("'");
        Statement statement;

        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String id = resultSet.getString("MaNhanKhau");
                String householdId = resultSet.getString("MaHoGiaDinh");
                String name = resultSet.getString("HoTen");
                String identityCard = resultSet.getString("CCCD");
                LocalDate dob = resultSet.getDate("NgaySinh").toLocalDate(); 
                String placeOfBirth = resultSet.getString("NoiSinh");
                String ethnicity = resultSet.getString("DanToc");
                String occupation = resultSet.getString("NgheNghiep");
                String relationship = resultSet.getString("QuanHe");
                Resident resident = new Resident(id, householdId, name, identityCard, dob.toString(), placeOfBirth, ethnicity, occupation, relationship);
                return resident;
            }
            return null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;
    }
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
