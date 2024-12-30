package com.example.dal;

import com.example.Admin;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;

public class HoGiaDinhDAL extends Admin {

    static String tableName = "hogiadinhtbl";

    // Kiểm tra khóa ngoại chung
    public boolean checkForeignKey(String tableName, String columnName, String value) {
        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";
        try (Connection conn = getConnectionAdmin();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // trả về true nếu tìm thấy
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra khóa ngoại: " + e.getMessage());
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
    public boolean insertHoGiaDinh(String tableName, String[] columns, String[] values) {
        // Tùy bạn có muốn check foreign key trước khi insert
        // for (int i = 0; i < columns.length; i++) {
        //     if (!validateForeignKeys(columns[i], values[i])) {
        //         System.err.println("Error: Giá trị " + values[i] + " không tồn tại trong bảng liên quan!");
        //         return false;
        //     }
        // }
        // Sử dụng super.insert(...) với bảng "hogiadinhtbl"
        return super.insert("hogiadinhtbl", columns, values);
    }

    // Update dữ liệu
    public boolean updateHoGiaDinh(String tableName, String columnName, String newValue,
                                   String conditionColumn, String conditionValue) {
        // Kiểm tra khóa ngoại cho MaCanHo, MaXe
        if ("MaCanHo".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("canhotbl", "MaCanHo", newValue)) {
                System.err.println("Error: MaCanHo không tồn tại trong bảng canhotbl!");
                return false;
            }
        } else if ("MaXe".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("xetbl", "MaXe", newValue)) {
                System.err.println("Error: MaXe không tồn tại trong bảng xetbl!");
                return false;
            }
        }

        // Trước đây bị sai, update sang "nhankhautbl"
        // Giờ sửa lại để update sang chính "hogiadinhtbl"
        return super.update("hogiadinhtbl", columnName, newValue, conditionColumn, conditionValue);
    }

    // Delete dữ liệu
    private boolean isReferencedInOtherTables(String conditionValue) {
        // Kiểm tra tham chiếu trong nhankhautbl
        if (checkForeignKey("nhankhautbl", "MaHoGiaDinh", conditionValue)) {
            return true;
        }
        // Kiểm tra tham chiếu trong chitietkhoanthutbl (nếu có)
        if (checkForeignKey("chitietkhoanthutbl", "MaHoGiaDinh", conditionValue)) {
            return true;
        }
        return false;
    }

    public boolean deleteHoGiaDinh(String tableName, String conditionColumn, String conditionValue) {
        if ("MaHoGiaDinh".equalsIgnoreCase(conditionColumn)) {
            if (isReferencedInOtherTables(conditionValue)) {
                System.err.println("Error: Không thể xóa vì MaHoGiaDinh đang được tham chiếu!");
                return false;
            }
        }
        return super.delete("hogiadinhtbl", conditionColumn, conditionValue);
    }

    // Search dữ liệu
    public ArrayList<Object[]> searchHoGiaDinh(String tableName, String columnName, String searchValue) {
        // Gọi super.search với đúng bảng "hogiadinhtbl"
        return super.search("hogiadinhtbl", columnName, searchValue);
    }
}
