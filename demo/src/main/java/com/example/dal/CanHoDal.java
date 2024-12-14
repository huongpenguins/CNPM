package com.example.dal;

import com.example.Entities.CanHo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
public class CanHoDal {

    public static ArrayList<CanHo> show() throws SQLException {
        ArrayList<CanHo> list_canho = new ArrayList<>();
    try{
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quanlychungcu", // URL cơ sở dữ liệu MySQL
                "root", // Tên đăng nhập
                "2003"  // Mật khẩu
        );
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MaCanHo, MaHoKhau, TenCanHo, Tang,DienTich,MoTa FROM CANHO");
        while (resultSet.next()) {
            CanHo canHo = new CanHo();
            canHo.setMaCanHo(resultSet.getInt("MaCanHo"));
            canHo.setMaHoKhau(resultSet.getString("Ma Ho Khau"));
            canHo.setTenCanHo(resultSet.getString("TenCanHo"));
            canHo.setTang(resultSet.getInt("Tang"));
            canHo.setDienTich(resultSet.getInt("SoPhong"));
            canHo.setMoTa(resultSet.getString("MaCuDan"));
            list_canho.add(canHo);
            }
        }catch (SQLException ex) {
        ex.printStackTrace();
        return null;
        }
        return list_canho;
    }

    public static void main(String[] args) {
        
    }
}