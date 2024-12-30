package com.example.dal;
import com.example.Admin;
import com.example.Entities.CanHo;
import com.example.connect.connect_mysql;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class CanHoDAL extends Admin {

        static Connection connection_admin = connect_mysql.getConnection();
    // Constructor should not have a return type and should match the class name
    public CanHoDAL() {
        super(); // Call the constructor of the parent class (Admin

    }
     public static CanHo loadData(String condition){
        
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT * FROM CanHoTBL WHERE MaHoGiaDinh = ");
        query.append("'").append(condition).append("'");
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String id = resultSet.getString("MaCanHo");
                String householdId = resultSet.getString("MaHoGiaDinh");
                String name = resultSet.getString("TenCanHo");
                int floor = resultSet.getInt("Tang");
                Float area = resultSet.getFloat("DienTich");
                String description = resultSet.getString("MoTa");
                
                CanHo canHo = new CanHo(id, householdId, name, floor, area, description);
                return canHo;
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;
    }
    /**
     * Hàm thêm dữ liệu vào bảng CanHo
     * @return true nếu thêm thành công, false nếu thất bại
     */
    static String tableName = "canhotbl";
    public boolean insertCanHo() {
        String[] columns = {"MaCanHo","MaHoKhau","TenCanHo","Tang","DienTich","MoTa"};
        String[] values = {"macanho, mahokhau, tencanho, tang, dientich, mota"};

        return insert(tableName, columns, values); // Gọi hàm insert từ Admin
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
