package com.example.dal;

import com.example.Admin;
import com.example.Entities.CanHo;
import com.example.connect.connect_mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class CanHoDAL extends Admin {

    private static Connection connection_admin = connect_mysql.getConnection();
    // Dùng cho tạm vắng controller
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
                float area = resultSet.getFloat("DienTich");
                String description = resultSet.getString("MoTa");

                CanHo canHo = new CanHo(id, name, floor, area, description, householdId);
                return canHo;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<CanHo> loadAllData() {
        String query = "SELECT c.MaCanHo, c.TenCanHo, c.Tang, c.DienTich, c.MoTa, h.MaHoGiaDinh " +
                "FROM canhotbl c " +
                "LEFT JOIN hogiadinhtbl h ON h.MaCanHo = c.MaCanHo";

        ArrayList<CanHo> canHoList = new ArrayList<>();
        try (PreparedStatement stmt = connection_admin.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String id          = rs.getString("MaCanHo");
                String name        = rs.getString("TenCanHo");
                int floor          = rs.getInt("Tang");
                float area         = rs.getFloat("DienTich");
                String description = rs.getString("MoTa");
                String maHoGiaDinh = rs.getString("MaHoGiaDinh"); // có thể null

                // Dùng constructor 6 tham số
                CanHo canHo = new CanHo(id, name, floor, area, description, maHoGiaDinh);
                canHoList.add(canHo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canHoList;
    }


    // Thêm căn hộ (chỉ 5 cột: MaCanHo, TenCanHo, Tang, DienTich, MoTa)
    public boolean insertCanHo(String[] columns, String[] values) {
        if (connection_admin == null) {
            System.err.println("Kết nối CSDL không thành công.");
            return false;
        }
        // columns: [MaCanHo, TenCanHo, Tang, DienTich, MoTa]
        String tableName = "canhotbl";
        String columnNames = String.join(",", columns);
        // placeholders: "?,?,?,?,?"
        String placeholders = String.join(",", Collections.nCopies(values.length, "?"));
        String insertQuery = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";

        try (PreparedStatement ps = connection_admin.prepareStatement(insertQuery)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            int rowsAffected = ps.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm (MaCanHo / TenCanHo)
    public ArrayList<CanHo> searchCanHo(String keyword) {
        ArrayList<CanHo> result = new ArrayList<>();
        String sql = "SELECT c.MaCanHo, c.TenCanHo, c.Tang, c.DienTich, c.MoTa, h.MaHoGiaDinh " +
                "FROM canhotbl c " +
                "LEFT JOIN hogiadinhtbl h ON h.MaCanHo = c.MaCanHo " +
                "WHERE c.MaCanHo LIKE ? OR c.TenCanHo LIKE ?";

        try (PreparedStatement statement = connection_admin.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id          = rs.getString("MaCanHo");
                    String name        = rs.getString("TenCanHo");
                    int floor          = rs.getInt("Tang");
                    float area         = rs.getFloat("DienTich");
                    String description = rs.getString("MoTa");
                    String maHoGiaDinh = rs.getString("MaHoGiaDinh");

                    // Tạo đối tượng CanHo và thêm vào danh sách kết quả
                    CanHo ch = new CanHo(id, name, floor, area, description, maHoGiaDinh);
                    result.add(ch);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm: " + e.getMessage());
        }

        return result;
    }


    // Update 1 cột
    public boolean updateCanHo(String columnName, String newValue, String conditionValue) {
        // Tức update canhotbl set <column>=? where MaCanHo=?
        String sql = "UPDATE canhotbl SET " + columnName + " = ? WHERE MaCanHo = ?";
        try (PreparedStatement ps = connection_admin.prepareStatement(sql)) {
            ps.setString(1, newValue);
            ps.setString(2, conditionValue);  // conditionValue là MaCanHo
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xoá căn hộ
    public boolean deleteCanHo(String maCanHo) {
        // Kiểm tra khoá ngoại, nếu hogiadinhtbl đang tham chiếu MaCanHo
        // (Nếu ON DELETE SET NULL, xoá vẫn được, tuỳ logic)
        String checkFk = "SELECT 1 FROM hogiadinhtbl WHERE MaCanHo = ?";
        try (PreparedStatement checkStmt = connection_admin.prepareStatement(checkFk)) {
            checkStmt.setString(1, maCanHo);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Tức là có hộ gia đình tham chiếu -> Tùy logic
                    System.err.println("Cảnh báo: MaCanHo đang được sử dụng trong hogiadinhtbl!");
                    // Nếu ON DELETE SET NULL thì vẫn xoá được
                    // Ta có thể cho phép xoá, hoặc từ chối
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Thực thi xoá
        String sql = "DELETE FROM canhotbl WHERE MaCanHo = ?";
        try (PreparedStatement ps = connection_admin.prepareStatement(sql)) {
            ps.setString(1, maCanHo);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
