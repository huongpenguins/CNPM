package com.example.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    // Hàm để tạo hash mật khẩu với salt
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Tạo một salt ngẫu nhiên
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Băm mật khẩu kết hợp với salt
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashedPassword = digest.digest(password.getBytes());

        // Kết hợp hash và salt thành một chuỗi
        byte[] saltAndHash = new byte[salt.length + hashedPassword.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hashedPassword, 0, saltAndHash, salt.length, hashedPassword.length);

        // Chuyển sang dạng Base64 để dễ lưu trữ
        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    // Hàm để kiểm tra mật khẩu
    public static boolean checkPassword(String password, String storedPassword) throws NoSuchAlgorithmException {
        // Giải mã chuỗi lưu trữ để tách salt và hash
        byte[] storedData = Base64.getDecoder().decode(storedPassword);
        byte[] salt = new byte[16];
        System.arraycopy(storedData, 0, salt, 0, 16);
        byte[] storedHash = new byte[storedData.length - 16];
        System.arraycopy(storedData, 16, storedHash, 0, storedHash.length);

        // Tạo hash mới từ mật khẩu nhập vào kết hợp với salt
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashedPassword = digest.digest(password.getBytes());

        // So sánh hash mới với hash đã lưu
        for (int i = 0; i < storedHash.length; i++) {
            if (storedHash[i] != hashedPassword[i]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "mysecretpassword";
        String hashedPassword = hashPassword(password);
        System.out.println("Hashed Password: " + hashedPassword);

        // Kiểm tra mật khẩu
        boolean isCorrect = checkPassword("mysecretpassword", hashedPassword);
        System.out.println("Password is correct: " + isCorrect);
    }
}
