package com.example.dal;

import com.example.Admin;

import java.sql.*;
import java.util.ArrayList;

public class HoGiaDinhDAL extends Admin {

    static String tableName = "hogiadinhtbl";

    // Kiểm tra khóa ngoại chung
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
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra khóa ngoại: " + e.getMessage());
            return false;
        }
    }

    // Thêm hàm kiểm tra trạng thái kết nối
    private boolean isConnectionClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra trạng thái kết nối: " + e.getMessage());
            return true;
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
        // (Nếu bạn vẫn muốn checkForeignKey thì giữ code check ở đây)
        return super.insertRecord(tableName, columns, values);
    }

    // Update dữ liệu
    public boolean updateHoGiaDinh(String tableName, String columnName, String newValue,
                                   String conditionColumn, String conditionValue) {
        // Kiểm tra khóa ngoại cho MaCanHo, MaXe
       /* if ("MaCanHo".equalsIgnoreCase(columnName)) {
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
*/
        // Trước đây bị sai, update sang "nhankhautbl"
        // Giờ sửa lại để update sang chính "hogiadinhtbl"
        return super.update("hogiadinhtbl", columnName, newValue, conditionColumn, conditionValue);
    }

    // Delete dữ liệu
    private boolean isReferencedInOtherTables(String conditionValue) {
        Connection conn = getConnectionAdmin();
        if (conn == null || isConnectionClosed(conn)) {
            System.err.println("Lỗi: Kết nối không khả dụng trong isReferencedInOtherTables.");
            return false;
        }

        if (checkForeignKey("nhankhautbl", "MaHoGiaDinh", conditionValue)) {
            return true;
        }
        /*
        if (checkForeignKey("chitietkhoanthutbl", "MaHoGiaDinh", conditionValue)) {
            return true;
        }
        */
        return false;
    }


    public boolean deleteHoGiaDinh(String tableName, String conditionColumn, String conditionValue) {
        if ("MaHoGiaDinh".equalsIgnoreCase(conditionColumn)) {
            if (isReferencedInOtherTables(conditionValue)) {
                System.err.println("Error: Không thể xóa vì MaHoGiaDinh đang được tham chiếu! Hãy xóa hết cư dân trong hộ khẩu trước!");
                return false;
            }
        }
        return super.delete("hogiadinhtbl", conditionColumn, conditionValue);
    }

    public boolean deleteHoGiaDinhP(String tableName, String conditionColumn, String conditionValue) throws SQLException {
        if ("MaHoGiaDinh".equalsIgnoreCase(conditionColumn)) {
            if (isReferencedInOtherTables(conditionValue)) {
                System.err.println("Error: Không thể xóa vì MaHoGiaDinh đang được tham chiếu!");
                return false;
            }
        }
        return super.deleteP("hogiadinhtbl", conditionColumn, conditionValue);
    }

    // Search dữ liệu
    public ArrayList<Object[]> searchHoGiaDinh(String tableName, String columnName, String searchValue) {
        // Gọi super.search với đúng bảng "hogiadinhtbl"
        return super.search("hogiadinhtbl", columnName, searchValue);
    }

    public ArrayList<Object[]> getPaymentDetailsForHousehold(String householdId) {
        String sql = "SELECT " +
                " kt.MaKhoanThu," +
                " kt.TenKhoanThu," +
                " kt.ThoiGianBatDau," +
                " kt.ThoiGianKetThuc," +
                " kt.SoTien AS TienQuyDinh," +
                " kt.DonVi," +
                " kt.loai," +
                " hdt.SoTienDaNop," +
                " hdt.ThoiDiemNop," +
                " sdn.SO AS SoDienNuoc " +
                "FROM hogiadinhtbl hg " +
                " LEFT JOIN hoadontbl hdt ON hg.MaHoGiaDinh = hdt.MaHoGiaDinh " +
                " LEFT JOIN khoanthutbl kt ON kt.MaKhoanThu = hdt.MaKhoanThu " +
                " LEFT JOIN sodiennuoctbl sdn ON sdn.MaKhoanThu = kt.MaKhoanThu " +
                "       AND sdn.MaHoGiaDinh = hg.MaHoGiaDinh " +
                "WHERE hg.MaHoGiaDinh = ?";

        ArrayList<Object[]> list = new ArrayList<>();
        try (PreparedStatement ps = getConnectionAdmin().prepareStatement(sql)) {
            ps.setString(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[10];
                row[0] = rs.getString("MaKhoanThu");
                row[1] = rs.getString("TenKhoanThu");
                row[2] = rs.getDate("ThoiGianBatDau");
                row[3] = rs.getDate("ThoiGianKetThuc");
                row[4] = rs.getInt("TienQuyDinh");
                row[5] = rs.getString("DonVi");
                row[6] = rs.getString("loai");
                row[7] = rs.getInt("SoTienDaNop");  // cẩn thận với null => default 0
                row[8] = rs.getDate("ThoiDiemNop");
                row[9] = rs.getInt("SoDienNuoc");   // cẩn thận với null => default 0
                list.add(row);
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi getPaymentDetailsForHousehold(): " + ex.getMessage());
        }
        return list;
    }

    public ArrayList<Object[]> getPaymentDetailsForHousehold1(String householdId) {
        ArrayList<Object[]> data =  new ArrayList<>();
        ResultSet rs=null;
        StringBuffer query = new StringBuffer("SELECT kt.MaKhoanThu,kt.TenKhoanThu,kt.ThoiGianBatDau,kt.ThoiGianKetThuc,kt.SoTien as TienQuyDinh,kt.DonVi,kt.Loai, MAX(hd.ThoiDiemNop) as ThoiDiemNop, SUM(hd.SoTienDaNop) as SoTienDaNop, ")
                .append("CASE ")
                .append("    WHEN kt.DonVi = 'Số điên/nước' THEN kt.SoTien * (SELECT so FROM SoDienNuoctbl where MaKhoanthu = kt.MaKhoanThu AND MaHoGiaDinh = gd.MaHoGiaDinh) ")
                .append("    WHEN kt.DonVi = 'Diện tích' THEN kt.SoTien * ch.DienTich ")
                .append("    WHEN kt.DonVi = 'Xe' THEN kt.SoTien * (SELECT SUM(MaXe) from xetbl where MaHoGiaDinh = gd.MaHoGiaDinh ) ")
                .append("    ELSE kt.SoTien ")
                .append("END as SoTienPhaiNop ")
                .append( " FROM KhoanThutbl kt CROSS JOIN hogiadinhtbl gd ")
                .append(" left join HoaDonTBL hd on ( kt.MaKhoanThu = hd.MaKhoanThu and hd.MaHoGiaDinh = gd.MaHoGiaDinh ) ")
                .append(" left join canhotbl ch on ch.MaCanHo = gd.MaCanHo ")
                .append(" left join NhanKhautbl nk on nk.MaNhanKhau = gd.MaNhanKhau ")
                .append(" WHERE gd.MaHoGiaDinh = '").append(householdId).append("' ")
                .append(" AND kt.completed =0 ")
                .append(" GROUP BY kt.MaKhoanThu,kt.TenKhoanThu,kt.ThoiGianBatDau,kt.ThoiGianKetThuc,kt.SoTien,kt.DonVi,kt.Loai,gd.MaHoGiaDinh, nk.HoTen,ch.TenCanHo,gd.MaHoGiaDinh ");
        Statement statement;
        try {
            statement = connection_admin.createStatement();
            rs= ((Statement) statement).executeQuery(query.toString());
            while (rs.next()) {
                Object[] row = new Object[11];
                row[0] = rs.getString("MaKhoanThu");
                row[1] = rs.getString("TenKhoanThu");
                row[2] = rs.getDate("ThoiGianBatDau");
                row[3] = rs.getDate("ThoiGianKetThuc");
                row[4] = rs.getInt("TienQuyDinh");
                row[5] = rs.getString("DonVi");
                row[6] = rs.getString("loai");
                row[7] = rs.getInt("SoTienDaNop");  // cẩn thận với null => default 0
                row[8] = rs.getDate("ThoiDiemNop");
                row[10] =rs.getInt("SoTienPhaiNop");
                data.add(row);
            }
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
