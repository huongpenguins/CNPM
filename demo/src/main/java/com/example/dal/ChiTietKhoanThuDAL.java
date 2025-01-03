package com.example.dal;
import com.example.Admin;
import com.example.Entities.CTKhoanThu;
import com.example.Entities.CTQuyenGop;
import com.example.Entities.KhoanThu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


public class ChiTietKhoanThuDAL extends Admin {

     public static ObservableList<CTKhoanThu> loadData1(String maKT,String completed,String Loai){
        ObservableList<CTKhoanThu> data = FXCollections.observableArrayList();
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT kt.MaKhoanThu,kt.TenKhoanThu,gd.MaHoGiaDinh,ch.TenCanHo,nk.HoTen, MAX(hd.ThoiDiemNop) as NgayNop, SUM(hd.SoTienDaNop) as SoTienNop, ")
        .append("CASE ")
        .append("    WHEN kt.DonVi = 'Số điên/nước' THEN kt.SoTien * (SELECT so FROM SoDienNuoctbl where MaKhoanthu =  '").append(maKT).append("' AND MaHoGiaDinh = gd.MaHoGiaDinh) ")  // Trường hợp Loai = "Dien"
        .append("    WHEN kt.DonVi = 'Diện tích' THEN kt.SoTien * ch.DienTich ")  
        .append("    WHEN kt.DonVi = 'Xe' THEN kt.SoTien * (SELECT SUM(MaXe) from xetbl where MaHoGiaDinh = gd.MaHoGiaDinh ) ")  
        .append("    ELSE kt.SoTien ")  
        .append("END as SoTienPhaiNop ")
        .append( " FROM KhoanThutbl kt CROSS JOIN hogiadinhtbl gd ")
        .append(" left join HoaDonTBL hd on ( kt.MaKhoanThu = hd.MaKhoanThu and hd.MaHoGiaDinh = gd.MaHoGiaDinh ) ")
        .append(" left join canhotbl ch on ch.MaCanHo = gd.MaCanHo ")
        .append(" left join NhanKhautbl nk on nk.MaNhanKhau = gd.MaNhanKhau ")
        .append(" WHERE kt.completed = ").append(completed).append(" AND kt.Loai = ").append("'").append(Loai).append("' ")
        .append(" AND kt.MaKhoanThu = '").append(maKT).append("' ")
        .append(" GROUP BY kt.MaKhoanThu,kt.TenKhoanThu,gd.MaHoGiaDinh, nk.HoTen,ch.TenCanHo,gd.MaHoGiaDinh ");
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String maKhoanThu = resultSet.getString("MaKhoanThu");
                String tenKhoanThu = resultSet.getString("TenKhoanThu");
                String tenChuHo = resultSet.getString("HoTen");
                String maHoGiaDinh =resultSet.getString("MaHoGiaDinh");
                String tenCanHo = resultSet.getString("TenCanHo");
                int soTienDaNop =resultSet.getInt("SoTienNop");
                LocalDate ngayNop = null;
                Date sqlDate = resultSet.getDate("NgayNop");  
                if (sqlDate != null) {
                    ngayNop = sqlDate.toLocalDate(); 
                } else {
                    ngayNop = null; 
                }
                int soTienPhaiNop = resultSet.getInt("SoTienPhaiNop");

                CTKhoanThu ctKhoanThu= new CTKhoanThu(maKhoanThu,tenKhoanThu,maHoGiaDinh,tenCanHo,tenChuHo,ngayNop,soTienPhaiNop,soTienDaNop);
                data.add(ctKhoanThu); // Thêm đối tượng KhoanThu vào ObservableList
            }
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;

    }

 
    public static ObservableList<CTKhoanThu> loadDienNuoc(String maKT,String completed,String Loai){
        ObservableList<CTKhoanThu> data = FXCollections.observableArrayList();
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT kt.MaKhoanThu,kt.TenKhoanThu,ch.MaCanHo,ch.TenCanHo,nk.HoTen, MAX(hd.ThoiDiemNop) as NgayNop, SUM(hd.SoTienDaNop) as SoTienNop, ")
        .append(" kt.SoTien * (SELECT so FROM SoDienNuoctbl where MaKhoanthu =  '").append(maKT).append("' AND MaHoGiaDinh = gd.MaHoGiaDinh) as SoTienPhaiNop ")
        .append( " FROM KhoanThutbl kt CROSS JOIN hogiadinhtbl gd ")
        .append(" left join HoaDonTBL hd on ( kt.MaKhoanThu = hd.MaKhoanThu and hd.MaHoGiaDinh = gd.MaHoGiaDinh ) ")
        .append(" left join canhotbl ch on ch.MaCanHo = gd.MaCanHo ")
        .append(" left join NhanKhautbl nk on nk.MaNhanKhau = gd.MaNhanKhau ")
        
        .append(" WHERE kt.completed = ").append(completed).append(" AND kt.Loai = ").append("'").append(Loai).append("' ")
        .append(" AND kt.MaKhoanThu = '").append(maKT).append("' ")
        .append(" GROUP BY kt.MaKhoanThu,kt.TenKhoanThu,ch.MaCanHo, nk.HoTen,ch.TenCanHo,gd.MaHoGiaDinh ");
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String maKhoanThu = resultSet.getString("MaKhoanThu");
                String tenKhoanThu = resultSet.getString("TenKhoanThu");
                String tenChuHo = resultSet.getString("HoTen");
                String maCanHo =resultSet.getString("MaCanHo");
                String tenCanHo = resultSet.getString("TenCanHo");
                int soTienPhaiNop =resultSet.getInt("SoTienPhaiNop");
                LocalDate ngayNop = null;
                Date sqlDate = resultSet.getDate("NgayNop");  
                if (sqlDate != null) {
                    ngayNop = sqlDate.toLocalDate(); 
                } else {
                    ngayNop = null; 
                }
                int soTienDaNop = resultSet.getInt("SoTienNop");

                CTKhoanThu ctKhoanThu= new CTKhoanThu(maKhoanThu,tenKhoanThu,maCanHo,tenCanHo,tenChuHo,ngayNop,soTienPhaiNop,soTienDaNop);
                data.add(ctKhoanThu); // Thêm đối tượng KhoanThu vào ObservableList
            }
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;

    }
    public static ObservableList<CTQuyenGop> loadDataQuyenGop(String maKT,String completed,String Loai){
        ObservableList<CTQuyenGop> data = FXCollections.observableArrayList();
        ResultSet resultSet=null;
        StringBuffer query = new StringBuffer("SELECT kt.MaKhoanThu,kt.TenKhoanThu,gd.MaHoGiaDinh,ch.TenCanHo,nk.HoTen,kt.SoTien, MAX(hd.ThoiDiemNop) as NgayNop, SUM(hd.SoTienDaNop) as SoTienNop ")
        .append(" FROM KhoanThutbl kt join HoaDonTBL hd on kt.MaKhoanThu = hd.MaKhoanThu ")
        .append(" join HoGiaDinhtbl gd on gd.MaHoGiaDinh = hd.MaHoGiaDinh ")
        .append(" join CanHotbl ch on ch.MaCanHo = gd.MaCanHo ")
        .append(" join NhanKhautbl nk on nk.MaNhanKhau = gd.MaNhanKhau ")
        .append(" WHERE kt.completed = ").append(completed).append(" AND kt.Loai = ").append("'").append(Loai).append("' ")
        .append(" AND kt.MaKhoanThu = ").append("'").append(maKT).append("' ")
        .append(" GROUP BY kt.MaKhoanThu,kt.TenKhoanThu,gd.MaHoGiaDinh, nk.HoTen,ch.TenCanHo , kt.SoTien");
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            resultSet=statement.executeQuery(query.toString());
            while (resultSet.next()) {
                String maKhoanThu = resultSet.getString("MaKhoanThu");
                String tenKhoanThu = resultSet.getString("TenKhoanThu");
                String tenChuHo = resultSet.getString("HoTen");
                String maHo =resultSet.getString("MaHoGiaDinh");
                String tenCanHo = resultSet.getString("TenCanHo");
                LocalDate ngayNop = resultSet.getDate("NgayNop").toLocalDate();
                int soTienDaNop = resultSet.getInt("SoTienNop");

                CTQuyenGop cTQuyenGop= new CTQuyenGop(maKhoanThu,tenKhoanThu,maHo,tenCanHo,tenChuHo,ngayNop,soTienDaNop);
                data.add(cTQuyenGop); // Thêm đối tượng KhoanThu vào ObservableList
            }
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;

    }
    
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
    static String tableName = "chitietkhoanthutbl";

    //search du lieu
    public ArrayList<Object[]> searchChiTietKhoanThu(String tableName, String columnName, String searchValue) {
        return super.search(tableName, columnName, searchValue);
    }

}
