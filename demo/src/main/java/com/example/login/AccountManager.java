package com.example.login;

import com.example.connect.connect_mysql;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountManager {

    private static String Ma_nv = "admin";  // Mã nhân viên (
    private static String password = "password";
    private static String email = "phanlehaidanghsht@gmail.com";
    private static String phonenumber = "123456789";
    private static String fullName = "Phan Dang";
    private static String cccd = "1234567890123";
    private static String address = "123 Street";
    private static String gender = "Male";
    private static String position = "Manager";
    private static String birthDate = "1990-01-01";  // Ngày sinh định dạng yyyy-mm-dd

    public static String getMa_nv() {
        return Ma_nv;
    }

    public static void setMa_nv(String ma_nv) {
        Ma_nv = ma_nv;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AccountManager.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        AccountManager.email = email;
    }

    public static String getPhonenumber() {
        return phonenumber;
    }

    public static void setPhonenumber(String phonenumber) {
        AccountManager.phonenumber = phonenumber;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        AccountManager.fullName = fullName;
    }

    public static String getCccd() {
        return cccd;
    }

    public static void setCccd(String cccd) {
        AccountManager.cccd = cccd;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        AccountManager.address = address;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        AccountManager.gender = gender;
    }

    public static String getPosition() {
        return position;
    }

    public static void setPosition(String position) {
        AccountManager.position = position;
    }

    public static String getBirthDate() {
        return birthDate;
    }

    public static void setBirthDate(String birthDate) {
        AccountManager.birthDate = birthDate;
    }



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

    // Phương thức cập nhật tất cả thông tin của nhân viên
    public static void updateAllInfo(String newFullName, String newCCCD, String newPhoneNumber, String newEmail, String newAddress, String newGender, String newPosition, String newBirthDate) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET HoTen = ?, cccd = ?, sdt = ?, email = ?, DiaChi = ?, GioiTinh = ?, ChucVu = ?, NgaySinh = ?, Password = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Thiết lập giá trị cho các tham số trong câu lệnh SQL
                    stmt.setString(1, newFullName);
                    stmt.setString(2, newCCCD);
                    stmt.setString(3, newPhoneNumber);
                    stmt.setString(4, newEmail);
                    stmt.setString(5, newAddress);
                    stmt.setString(6, newGender);
                    stmt.setString(7, newPosition);
                    stmt.setString(8, newBirthDate);
                    stmt.setString(10, Ma_nv);  // Sử dụng username (mã nhân viên) làm điều kiện WHERE

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Cập nhật lại các thuộc tính của AccountManager sau khi thay đổi
                        fullName = newFullName;
                        cccd = newCCCD;
                        phonenumber = newPhoneNumber;
                        email = newEmail;
                        address = newAddress;
                        gender = newGender;
                        position = newPosition;
                        birthDate = newBirthDate;

                        System.out.println("Thông tin của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + Ma_nv);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

    // Phương thức cập nhật tên nhân viên
    public static void updateFullName(String newFullName, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET HoTen = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newFullName);
                    stmt.setString(2, username);  // Sử dụng mã nhân viên làm điều kiện WHERE
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Tên nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật tên: " + e.getMessage());
        }
    }

    // Phương thức cập nhật CCCD của nhân viên
    public static void updateCCCD(String newCCCD, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET cccd = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newCCCD);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("CCCD của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật CCCD: " + e.getMessage());
        }
    }

    // Phương thức cập nhật số điện thoại của nhân viên
    public static void updatePhoneNumber(String newPhoneNumber, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET sdt = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newPhoneNumber);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Số điện thoại của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật số điện thoại: " + e.getMessage());
        }
    }

    // Phương thức cập nhật email của nhân viên
    public static void updateEmail(String newEmail, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET email = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newEmail);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Email của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật email: " + e.getMessage());
        }
    }

    // Phương thức cập nhật địa chỉ của nhân viên
    public static void updateAddress(String newAddress, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET DiaChi = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newAddress);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Địa chỉ của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật địa chỉ: " + e.getMessage());
        }
    }

    // Phương thức cập nhật giới tính của nhân viên
    public static void updateGender(String newGender, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET GioiTinh = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newGender);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Giới tính của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật giới tính: " + e.getMessage());
        }
    }

    // Phương thức cập nhật chức vụ của nhân viên
    public static void updatePosition(String newPosition, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET ChucVu = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newPosition);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Chức vụ của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chức vụ: " + e.getMessage());
        }
    }

    // Phương thức cập nhật ngày sinh của nhân viên
    public static void updateBirthDate(String newBirthDate, String username) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET NgaySinh = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, newBirthDate);
                    stmt.setString(2, username);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Ngày sinh của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với mã nhân viên " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật ngày sinh: " + e.getMessage());
        }
    }
    // Phương thức cập nhật mật khẩu của nhân viên theo Ma_nv
    public static void updatePasswordByMa_nv( String newPassword, String Manv) {
        try (Connection conn = new connect_mysql().getConnection()) {
            if (conn != null) {
                String query = "UPDATE admin_account SET Password = ? WHERE Ma_nv = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, PasswordHasher.hashPassword(newPassword));
                    stmt.setString(2, Manv);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Mật khẩu của nhân viên đã được cập nhật thành công.");
                    } else {
                        System.out.println("Không tìm thấy nhân viên với email " + Ma_nv);
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
