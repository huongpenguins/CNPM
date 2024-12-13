package com.example.dal;

import com.example.Entities.NhanKhau;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;


public class ConnectSQL{

    // Phương thức này sẽ trả về một đối tượng Connection đến cơ sở dữ liệu
    public static Connection connect() {
        try {
            String url = "mysql://localhost:3306/quanlychungcu";
            String username = "root";
            String password = "2003";
            // Tạo kết nối tới cơ sở dữ liệu
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}


public class NhanKhauDal {

    public static List<NhanKhau> ListNhanKhau(String query) throws SQLException {
        List<NhanKhau> list_nhankhau = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quanlychungcu", // URL cơ sở dữ liệu MySQL
                    "root", // Tên đăng nhập
                    "2003"  // Mật khẩu
            );
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select MaNhanKhau, MaHoGiaDinh, HoTen, CCCD, NgaySinh, NoiSinh, DanToc, NgheNghiep From NhanKhau");
            while (rs.next()){
                NhanKhau nhankhau = new NhanKhau();
                nhankhau.setMaNhanKhau(rs.getString("MaNhanKhau"));
                nhankhau.setMaHoGiaDinh(rs.getString("MaHoGiaDinh"));
                nhankhau.setHoTen(rs.getString("HoTen"));
                nhankhau.setCCCD(rs.getInt("CCCD"));
                nhankhau.setNgaySinh(rs.getString("NgaySinh"));
                nhankhau.setNoiSinh(rs.getString("NoiSinh"));
                nhankhau.setDanToc(rs.getString("DanToc"));
                nhankhau.setNgheNghiep(rs.getString("NgheNghiep"));
                list_nhankhau.add(nhankhau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list_nhankhau;
    }

    public static List<NhanKhau> list_nhankhau() throws SQLException {
        String query = "Select * From NhanKhau";
        return ListNhanKhau(query);
    }

// Update thông tin nhân khẩu
    public static void updateNhanKhau(NhanKhau nhankhau){
        Connection  connection = null;
        PreparedStatement pstmt = null;

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quanlychungcu", // URL cơ sở dữ liệu MySQL
                    "root", // Tên đăng nhập
                    "2003"  // Mật khẩu
            );
            String sql = "Update NhanKhau Set" + "MaHoGiaDinh = ?, HoTen = ?, CCCD = ?, NgaySinh = ?, NoiSinh = ?, DanToc = ?, NgheNghiep = ?"
                    + "Where MaNhanKhau = ?";

        } catch (SQLException e){
            System.out.println("Lỗi khi cập nhật thông tin nhân khẩu: " + e.getMessage());
        }
    }

// Tìm kiếm nhân khẩu
    public static List<NhanKhau> TimKiemNhanKhau_Dal(String info) throws SQLException{
        String query = "Select * From NhanKhau Where MaNhanKhau like '%"+info+"%' or HoTen like N'%"+info+"%' or MaHoGiaDinh like '%"+info+"%' or CCCD like '%"+info+"%' or NgaySinh like '%"+info+"%' or NoiSinh like N'%"+info+"%' or Dantoc like N'%"+info+"%' or NgheNghiep like N'%"+info+"%'";
        return ListNhanKhau(query);
    }

// Xóa nhân khẩu
    public static int XoaNhanKhau(String MaNhanKhau){
        try {
            String sql = "delete From NhanKhau Where MaNhanKhau = ?";
            PreparedStatement preStatement= ConnectSQL.connect().prepareStatement(sql);
            preStatement.setString(1,MaNhanKhau);
            preStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

// kiểm tra bản ghi có tồn tại
    public static boolean kiemtraTonTai(String MaNhanKhau) {
        try {
            String sql = "Select * From NhanKhau Where MaNhanKhau = ?";
            PreparedStatement preStatement = ConnectSQL.connect().prepareStatement(sql);
            preStatement.setString(1, MaNhanKhau);
            ResultSet result = preStatement.executeQuery();
            return result.next();
        } catch (Exception e) {

        }
            return false;
    }

// thêm nhân khẩu
    public int themNhanKhau(NhanKhau nhankhau){
        try{
            String sql = "insert into NhanKhau values(?,?,?,?,?,?,?,?)";
            PreparedStatement preStatement=ConnectSQL.connect().prepareStatement(sql);
            preStatement.setString(1, nhankhau.getMaNhanKhau());
            preStatement.setString(2, nhankhau.getMaHoGiaDinh());
            preStatement.setString(3, nhankhau.getHoTen());
            preStatement.setInt(4, nhankhau.getCCCD());
            preStatement.setString(5, nhankhau.getNgaySinh());
            preStatement.setString(6, nhankhau.getNoiSinh());
            preStatement.setString(7, nhankhau.getDanToc());
            preStatement.setString(8, nhankhau.getNgheNghiep());
            return preStatement.executeUpdate();
        } catch (SQLException e2){
            System.err.println("Dữ liệu không được để trống.");
        } catch (NullPointerException e2){
            System.err.println("Dữ liệu không được để trống.");
        } catch (NumberFormatException e2){
            System.err.println("Dữ liệu không chính xác, mời nhập lại.");
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

// Update danh sách nhân khẩu
    public int updateNhanKhau(NhanKhau nhankhau){
        try {
            String sql="update NhanKhau Set MaHoGiaDinh=?,Hoten=?,CCCD=?,NgaySinh=?,NoiSinh=?,DanToc=?, NgheNghiep=? where MaNhanKhau=?";
            PreparedStatement preStatement=ConnectSQL.connect().prepareStatement(sql);
            preStatement.setString(1, nhankhau.getMaNhanKhau());
            preStatement.setString(2, nhankhau.getMaHoGiaDinh());
            preStatement.setString(3, nhankhau.getHoTen());
            preStatement.setInt(4, nhankhau.getCCCD());
            preStatement.setString(5, nhankhau.getNgaySinh());
            preStatement.setString(6, nhankhau.getNoiSinh());
            preStatement.setString(7, nhankhau.getDanToc());
            preStatement.setString(8,nhankhau.getNgheNghiep());
            return preStatement.executeUpdate();
        } catch (SQLException e2){
            System.err.println("Dữ liệu không được để trống.");
        } catch (NullPointerException e2){
            System.err.println("Dữ liệu không được để trống.");
        } catch (NumberFormatException e2){
            System.err.println("Dữ liệu không chính xác, mời nhập lại.");
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

// truy xuat danh sach mã nhân khẩu
    public static List<NhanKhau> layMaNhanKhau(){
        List<NhanKhau> listNhanKhau= new ArrayList<>();
        try {
            String sql="select MaNhanKhau from NhanKhau ";
            Statement statement=ConnectSQL.connect().createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                NhanKhau nhankhau = new NhanKhau();
                nhankhau.setMaNhanKhau(resultSet.getString(1));
                listNhanKhau.add(nhankhau);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listNhanKhau;
    }

// Xử lý tìm kiếm nhân khẩu theo tên
  public ArrayList<NhanKhau> xuLyTimKiem(String HoTen) {
       ArrayList<NhanKhau> list_NhanKhauTimKiem = new ArrayList<>();
       try {
           String sql = "Select * From NhanKhau Where HoTen=?";
           PreparedStatement preStatement = ConnectSQL.connect().prepareStatement(sql);
           preStatement.setString(1, HoTen);
           ResultSet rs = preStatement.executeQuery();
           while (rs.next()) {
               NhanKhau nhankhau = new NhanKhau();
               nhankhau.setMaNhanKhau(rs.getString(1));
               nhankhau.setMaHoGiaDinh(rs.getString(2));
               nhankhau.setHoTen(rs.getString(3));
               nhankhau.setCCCD(rs.getInt(4));
               nhankhau.setNgaySinh(rs.getString(5));
               nhankhau.setNoiSinh(rs.getString(6));
               nhankhau.setDanToc(rs.getString(7));
               nhankhau.setNgheNghiep(rs.getString(8));
               list_NhanKhauTimKiem.add(nhankhau);
           }
       } catch (Exception e) {
        e.printStackTrace();
       }
    return list_NhanKhauTimKiem;
  }
}
