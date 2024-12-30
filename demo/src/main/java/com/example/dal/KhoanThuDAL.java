package com.example.dal;
import com.example.Admin;
import com.example.Entities.KhoanThu;
import com.example.connect.connect_mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

//import static com.example.Admin.connection_admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhoanThuDAL extends Admin {
    
    static Connection connection_admin = connect_mysql.getConnection();
    public KhoanThuDAL(){
        super(); // ke thua Admin
    }
    public static ObservableList<KhoanThu> loadData(String condition){
        ObservableList<KhoanThu> data = FXCollections.observableArrayList();
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT * FROM KHOANTHUTBL WHERE completed = ");
        query.append(condition);
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String maKhoanThu = resultSet.getString("MaKhoanThu");
                String tenKhoanThu = resultSet.getString("TenKhoanThu");
                String loai = resultSet.getString("Loai");
                LocalDate batdau = resultSet.getDate("ThoiGianBatDau").toLocalDate();
                LocalDate hannop = resultSet.getDate("ThoiGianKetThuc").toLocalDate();
                int dongia = resultSet.getInt("SoTien");
                String donvi = resultSet.getString("DonVi");


                KhoanThu khoanThu = new KhoanThu(maKhoanThu, tenKhoanThu,loai,batdau,hannop,dongia,donvi);
                data.add(khoanThu); // Thêm đối tượng KhoanThu vào ObservableList
            }
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;

    }
    
    
    static String tableName = "khoanthutbl";

    public boolean insertKhoanThu() {
        String[] columns = {"MaKhoanThu", "TenKhoanThu", "ThoiGianBatDau", "ThoiGiaKetThuc", "LoaiKhoanThu", "SoTien", "Donvi"};
        String[] types = {"int", "String", "LocalDateTime", "LocalDateTime", "String", "String", "String"};
        return insert(tableName, columns, types);
    }
    // public static boolean update(String[] column, String[] newValue,String condition){
    //   return update1("khoanthutbl",column,newValue,condition);

    // }
    public static boolean updateKhoanThu(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {
        return update(tableName, columnName, newValue, conditionColumn, conditionValue);
    }
    public static boolean update1( String[] columns,String[]types, String[] newValue,String condition){
       return update1("khoanthutbl",columns,types,newValue,condition);
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
        if (checkForeignKey("chitietkhoanthutbl", "MaKhoanThu", columnValue)) {
            System.err.println("Error: Không thể xóa vì MaKhoanThu đang được tham chiếu trong bảng chitietkhoanthutbl!");
            return false; // Nếu đang bị tham chiếu, không cho phép xóa
        }
        return delete(tableName, columnName, columnValue);
    }

    public ArrayList<Object[]> searchKhoanThu(String tableName, String columnName, String searchValue) {
        return search(tableName, columnName, searchValue);
    }
}
