package com.example.dal;

import com.example.Admin;
import com.example.Resident;
// import com.example.Entities.NhanKhau; // Nếu dùng

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Lớp DAL quản lý Nhân Khẩu
 * - Kế thừa Admin => dùng connection_admin để CRUD
 * - Chứa hàm insertNhanKhau, updateNhanKhau, deleteNhanKhau, searchNhanKhau
 */
public class NhanKhauDAL extends Admin {
    // Kiểm tra trạng thái kết nối
    private boolean isConnectionClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra trạng thái kết nối: " + e.getMessage());
            return true;
        }
    }
    //region Hàm loadData (demo)
    public static Resident loadData(String condition) {
        // Lấy 1 Resident có MaNhanKhau = condition
        // Demo, hiếm khi dùng
        String query = "SELECT * FROM nhankhautbl WHERE MaNhanKhau = '" + condition + "'";

        try (Statement statement = connection_admin.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                // Lấy dữ liệu cột
                String id           = rs.getString("MaNhanKhau");
                String householdId  = rs.getString("MaHoGiaDinh");
                String name         = rs.getString("HoTen");
                String identityCard = rs.getString("CCCD");
                LocalDate dob       = rs.getDate("NgaySinh").toLocalDate();
                String placeOfBirth = rs.getString("NoiSinh");
                String ethnicity    = rs.getString("DanToc");
                String occupation   = rs.getString("NgheNghiep");
                String relationship = rs.getString("QuanHe");

                return new Resident(
                        id, householdId, name, identityCard,
                        dob.toString(), placeOfBirth, ethnicity, occupation, relationship
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Hàm checkForeignKey
    /**
     * Kiểm tra 1 giá trị (value) có tồn tại trong tableName.columnName hay không.
     * - Thường dùng kiểm tra MaHoGiaDinh trước khi insert/update
     */
    public boolean checkForeignKey(String tableName, String columnName, String value) {
        Connection conn = getConnectionAdmin();
        if (conn == null || isConnectionClosed(conn)) {
            System.err.println("Lỗi: Kết nối không khả dụng trong checkForeignKey.");
            return false;
        }
        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // trả về true nếu tìm thấy
            }
        } catch(SQLException e) {
            System.err.println("Lỗi khi kiểm tra khóa ngoại: " + e.getMessage());
            return false;
        }
    }
    //endregion

    //region insertNhanKhau
    /**
     * Insert 1 bản ghi vào nhankhautbl.
     * - Yêu cầu: cột MaHoGiaDinh phải tồn tại ở hogiadinhtbl
     */
    public boolean insertNhanKhau(String tableName, String[] columns, String[] types) {
        // (logic cũ, console-based?)
        // Hoặc nếu code JavaFX, bạn có insert1(...)? Tùy.
        // Nên cẩn thận: MaHoGiaDinh -> check hogiadinhtbl

        /*
        int maHoIndex = -1;
        for (int i = 0; i < columns.length; i++) {
            if ("MaHoGiaDinh".equalsIgnoreCase(columns[i])) {
                maHoIndex = i;
                break;
            }
        }
        if (maHoIndex == -1) {
            System.err.println("Error: Cột MaHoGiaDinh bắt buộc phải có!");
            return false;
        }
        // Kiểm tra khóa ngoại
        String maHoGiaDinh = columns[maHoIndex];
        if (!checkForeignKey("hogiadinhtbl", "MaHoGiaDinh", maHoGiaDinh)) {
            System.err.println("Error: MaHoGiaDinh không tồn tại trong bảng hogiadinhtbl");
            return false;
        }
        */
        // Gọi super.insert(...) => console-based
        return super.insertRecord(tableName, columns, types);
    }
    //endregion

    //region updateNhanKhau
    /**
     * updateNhanKhau: Gọi super.update(...) => single static connect
     * - Kiểm tra nếu columnName = "MaHoGiaDinh" => check hogiadinhtbl
     */
    public boolean updateNhanKhau(String tableName, String columnName, String newValue,
                                  String conditionColumn, String conditionValue) throws SQLException {
        // Kiểm tra khóa ngoại nếu cột = MaHoGiaDinh
        if ("MaHoGiaDinh".equalsIgnoreCase(columnName)) {
            if (!checkForeignKey("hogiadinhtbl", "MaHoGiaDinh", newValue)) {
                System.err.println("Error: MaHoGiaDinh mới không tồn tại trong bảng hogiadinhtbl!");
                return false;
            }
        }
        // Gọi hàm update cũ
        return super.update("nhankhautbl", columnName, newValue, conditionColumn, conditionValue);
    }
    //endregion

    //region deleteNhanKhau
    /**
     * deleteNhanKhau: Kiểm tra MaNhanKhau có bị tham chiếu ở tamtru, tamvang hay không
     * - Xong gọi super.delete(...)
     */
    public boolean deleteNhanKhau(String conditionColumn, String conditionValue) {
        // Kiểm tra: MaNhanKhau có tham chiếu tamtru, tamvang
        if ("MaNhanKhau".equalsIgnoreCase(conditionColumn)) {
            // if (isReferencedInOtherTables(...)) ...
        }
        // Xóa
        return super.delete("nhankhautbl", conditionColumn, conditionValue);
    }
    public boolean deleteNhanKhauP(String conditionColumn, String conditionValue) throws SQLException {
        // Kiểm tra: MaNhanKhau có tham chiếu tamtru, tamvang
        if ("MaNhanKhau".equalsIgnoreCase(conditionColumn)) {
            if (isReferencedInOtherTables(conditionValue)) {
                System.err.println("Error: Không thể xóa vì MaNhanKhau đang được tham chiếu!");
                return false;
            }
        }
        // Xóa
        return super.deleteP("nhankhautbl", conditionColumn, conditionValue);
    }

    //region Kiểm tra tham chiếu
    private boolean isReferencedInOtherTables(String conditionValue) {
        Connection conn = getConnectionAdmin();
        if (conn == null || isConnectionClosed(conn)) {
            System.err.println("Lỗi: Kết nối không khả dụng trong isReferencedInOtherTables.");
            return false;
        }
    // Nguy hiểm
        if (checkForeignKey("tamvangtbl", "MaNhanKhau", conditionValue)) {
            return false;
        }
        if (checkForeignKey("tamtrutbl", "MaNhanKhau", conditionValue)) {
            return false;
        }
    // Nguy hieem
        /*
        if (checkForeignKey("chitietkhoanthutbl", "MaHoGiaDinh", conditionValue)) {
            return true;
        }
        */
        return false;
    }
    /*
    private boolean isReferencedInOtherTables(String tableName, String columnName, String value) {
        // Demo:
        if (checkForeignKey("tamvangtbl", columnName, value)) {
            return true;
        }
        if (checkForeignKey("tamtrutbl", columnName, value)) {
            return true;
        }
        return false;
    }
    */

    //endregion

    //region searchNhanKhau
    /**
     * Tìm kiếm (hoặc load all) trong nhankhautbl
     */
    public ArrayList<Object[]> searchNhanKhau(String tableName, String columnName, String searchValue) {
        // Chỉ cần gọi super.search(...) => "nhankhautbl"
        return super.search("nhankhautbl", columnName, searchValue);
    }
    //endregion
}
