package com.example.login;

import com.example.connect.connect_mysql;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountManager {

    // Phương thức kiểm tra sự tồn tại của nhân viên theo số điện thoại
    public static boolean isPhoneNumberExist(String phoneNumber) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "SELECT COUNT(*) FROM admin_account WHERE sdt = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, phoneNumber);  // Tìm theo số điện thoại

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;  // Nếu số lượng > 0, tức là có tồn tại
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra số điện thoại: " + e.getMessage());
        }
        return false;  // Trả về false nếu không tìm thấy
    }
    // Phương thức kiểm tra sự tồn tại của nhân viên theo email
    public static boolean isEmailExist(String email) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "SELECT COUNT(*) FROM admin_account WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);  // Tìm theo email

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;  // Nếu số lượng > 0, tức là có tồn tại
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra email: " + e.getMessage());
        }
        return false;  // Trả về false nếu không tìm thấy
    }
    // Phương thức tìm mã nhân viên qua số điện thoại hoặc email
    public static String getMaNvByPhoneOrEmail(String phoneOrEmail) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm kiếm mã nhân viên theo số điện thoại hoặc email
                String query = "SELECT Ma_nv FROM admin_account WHERE sdt = ? OR email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, phoneOrEmail);  // Tham số cho số điện thoại
                    stmt.setString(2, phoneOrEmail);  // Tham số cho email

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        // Trả về mã nhân viên nếu tìm thấy
                        return resultSet.getString("Ma_nv");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm mã nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }
    // Phương thức tìm Họ tên bằng mã nhân viên
    public static String getNameByMaNv(String MaNV) {
        if (MaNV == null || MaNV.isEmpty()) {
            System.err.println("Mã nhân viên không hợp lệ.");
            return null;  // Trả về null nếu mã nhân viên không hợp lệ
        }

        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Họ tên của nhân viên theo mã nhân viên
                String query = "SELECT HoTen FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        // Trả về Họ tên nếu tìm thấy
                        return resultSet.getString("HoTen");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Họ tên nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm CCCD bằng mã nhân viên
    public static String getCCCDByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm CCCD của nhân viên theo mã nhân viên
                String query = "SELECT CCCD FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("CCCD");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm CCCD nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm SĐT bằng mã nhân viên
    public static String getSdtByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm SĐT của nhân viên theo mã nhân viên
                String query = "SELECT SDT FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("SDT");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm SĐT nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm Email bằng mã nhân viên
    public static String getEmailByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Email của nhân viên theo mã nhân viên
                String query = "SELECT Email FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("Email");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Email nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm Địa chỉ bằng mã nhân viên
    public static String getDiaChiByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Địa chỉ của nhân viên theo mã nhân viên
                String query = "SELECT DiaChi FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("DiaChi");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Địa chỉ nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm Giới tính bằng mã nhân viên
    public static String getGioiTinhByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Giới tính của nhân viên theo mã nhân viên
                String query = "SELECT GioiTinh FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("GioiTinh");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Giới tính nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm Chức vụ bằng mã nhân viên
    public static String getChucVuByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Chức vụ của nhân viên theo mã nhân viên
                String query = "SELECT ChucVu FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("ChucVu");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Chức vụ nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm Ngày sinh bằng mã nhân viên
    public static Date getNgaySinhByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm Ngày sinh của nhân viên theo mã nhân viên
                String query = "SELECT NgaySinh FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getDate("NgaySinh");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm Ngày sinh nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }





    // Phương thức tìm mật khẩu bằng mã nhân viên
    public static String getPasswordByMaNv(String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                // Câu truy vấn tìm mật khẩu của nhân viên theo mã nhân viên
                String query = "SELECT Password FROM admin_account WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, MaNV);  // Tham số cho MaNV

                    var resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        // Trả về mật khẩu nếu tìm thấy
                        return resultSet.getString("Password");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm mật khẩu nhân viên: " + e.getMessage());
        }
        return null;  // Trả về null nếu không tìm thấy
    }

    // Phương thức cập nhật thông tin của nhân viên dựa trên mã nhân viên
    public static void updateAllInfo(String MaNV, String newFullName, String newCCCD, String newPhoneNumber,
                                     String newEmail, String newAddress, String newGender,
                                     String newPosition, String newBirthDate) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET HoTen = ?, cccd = ?, sdt = ?, email = ?, " +
                        "DiaChi = ?, GioiTinh = ?, ChucVu = ?, NgaySinh = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Thiết lập giá trị cho các tham số trong câu lệnh SQL
                    stmt.setString(1, newFullName);
                    stmt.setString(2, newCCCD);
                    stmt.setString(3, newPhoneNumber);
                    stmt.setString(4, newEmail);
                    stmt.setString(5, newAddress);
                    stmt.setString(6, newGender);
                    stmt.setString(7, newPosition);
                    stmt.setDate(8, Date.valueOf(newBirthDate)); // Định dạng "yyyy-MM-dd"
                    stmt.setString(9, MaNV);  // Sử dụng mã nhân viên làm điều kiện WHERE

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        // In thông báo thành công
                        System.out.println("Thông tin của nhân viên với mã " + MaNV + " đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }


    // Phương thức cập nhật tên nhân viên
    public static void updateFullName(String newFullName, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET HoTen = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newFullName);
                    stmt.setString(2, MaNV);  // Sử dụng mã nhân viên làm điều kiện WHERE
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Tên nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật tên: " + e.getMessage());
        }
    }

    // Phương thức cập nhật CCCD của nhân viên
    public static void updateCCCD(String newCCCD, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET cccd = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newCCCD);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("CCCD của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật CCCD: " + e.getMessage());
        }
    }

    // Phương thức cập nhật số điện thoại của nhân viên
    public static void updatePhoneNumber(String newPhoneNumber, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET sdt = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newPhoneNumber);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Số điện thoại của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật số điện thoại: " + e.getMessage());
        }
    }

    // Phương thức cập nhật email của nhân viên
    public static void updateEmail(String newEmail, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET email = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newEmail);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Email của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật email: " + e.getMessage());
        }
    }

    // Phương thức cập nhật địa chỉ của nhân viên
    public static void updateAddress(String newAddress, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET DiaChi = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newAddress);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Địa chỉ của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật địa chỉ: " + e.getMessage());
        }
    }

    // Phương thức cập nhật giới tính của nhân viên
    public static void updateGender(String newGender, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET GioiTinh = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newGender);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Giới tính của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật giới tính: " + e.getMessage());
        }
    }

    // Phương thức cập nhật chức vụ của nhân viên
    public static void updatePosition(String newPosition, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET ChucVu = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newPosition);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Chức vụ của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chức vụ: " + e.getMessage());
        }
    }

    // Phương thức cập nhật ngày sinh của nhân viên
    public static void updateBirthDate(String newBirthDate, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET NgaySinh = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newBirthDate);
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Ngày sinh của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + MaNV);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật ngày sinh: " + e.getMessage());
        }
    }
    // Phương thức cập nhật mật khẩu của nhân viên theo Ma_nv
    public static void updatePasswordByMa_nv( String newPassword, String MaNV) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET Password = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, PasswordHasher.hashPassword(newPassword));
                    stmt.setString(2, MaNV);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Mật khẩu của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với email " + MaNV);
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }

    // Phương thức cập nhật mật khẩu của nhân viên theo email
    public static void updatePasswordByEmail( String newPassword, String userEmail) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET Password = ? WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, PasswordHasher.hashPassword(newPassword));
                    stmt.setString(2, userEmail);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Mật khẩu của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với email " + userEmail);
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }
    public static void updatePasswordByPhone( String newPassword, String userPhone) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET Password = ? WHERE sdt = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, PasswordHasher.hashPassword(newPassword));
                    stmt.setString(2, userPhone);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Mật khẩu của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với sdt " + userPhone);
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }

}
