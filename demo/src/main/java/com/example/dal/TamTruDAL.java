package com.example.dal;
import com.example.Admin;
import com.example.Entities.CanHo;
import com.example.Entities.TamTru;
import com.example.Resident;
import com.example.connect.connect_mysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TamTruDAL extends Admin {
    static Connection connection_admin = connect_mysql.getConnection();
    public static ObservableList<TamTru> loadData(String condition){
        ObservableList<TamTru> data = FXCollections.observableArrayList();
        TamTru tamtru;
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT * FROM TamTruTBL WHERE completed = ");
        query.append(condition);
        Statement statement;

        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String maNhanKhau = resultSet.getString("MaNhanKhau");
                Resident r =NhanKhauDAL.loadData(maNhanKhau);
                if(r!=null){
                    CanHo c = CanHoDAL.loadData(r.getHouseholdId());
                    String maTamTru = resultSet.getString("MaTamTru");
                    String dctamtru = resultSet.getString("DcTamTru");
                    LocalDate ngaybdtamtru = resultSet.getDate("Ngaybdtamtru").toLocalDate();
                    tamtru = new TamTru(r.getName(),r.getIdentityCard(),c.getTenCanHo(),dctamtru,ngaybdtamtru);
                    data.add(tamtru);
                }
            }
            return data;
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

    static String tableName = "tamtrutbl";

// insert du lieu
    public boolean insertTamTru(String[] tableName, String[] columns, String[] values) {
        int MaNhanKhauIndex = -1;

        //tim chi so cot MaNhanKhau
        for (int i = 0; i < columns.length; i++) {
             if ("MaNhanKhau".equalsIgnoreCase(columns[i])) {
                  MaNhanKhauIndex = i;
                  break;
             }
        }
        if (MaNhanKhauIndex == -1) {
             System.err.println("Error: Cột MaNhanKhau bắt buộc phải có!");
             return false;
        }
        // kiểm tra khóa ngoại
        String MaNhanKhau = values[MaNhanKhauIndex];
        if (!checkForeignKey("nhankhautbl", "MaNhanKhau", MaNhanKhau)) {
            System.err.println("Error: MaNhanKhau không tồn tại trong bảng nhankhautbl");
            return false;
        }
        return super.insert("tamtrutbl", columns, values);
    }

    // Update dữ liệu
    public boolean updateTamTru(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException{
        //kiem tra khoa ngoai neu cap nhat cot MaNhanKhau
        if ("MaNhanKhau".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("nhankhautbl", "NhanKhau", newValue)) {
                System.err.println("Error: MaNhanKhau mới không tồn tại trong bảng nhankhautbl!");
                return false;
            }
        }
        //gọi phuơng thức update từ lớp cha Admin
        return super.update("tamtrutbl", columnName, newValue, conditionColumn, conditionValue);
    }

//delete du lieu
     public boolean deleteTamTru(String tableName, String conditionColumn, String columnValue) {
         // Không cần kiểm tra khóa ngoại
         return super.delete("tamtrutbl", conditionColumn, columnValue);
     }

// search du lieu
     public ArrayList<Object[]> searchTamTru(String tableName, String columnName, String searchValue) {
         return super.search(tableName, columnName, searchValue);
     }
}
