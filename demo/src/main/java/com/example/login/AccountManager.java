package com.example.login;

public class AccountManager {
    private static String username = "admin";
    private static String password = "password";
    private static String email = "phandang@gmail.com";
    private static String phonenumber = "123456789";

    public static void setPhonenumber(String phone_number) {
        AccountManager.phonenumber = phone_number;
    }

    public static String getPhonenumber() {
        return phonenumber;
    }

    public static void setEmail(String email) {
        AccountManager.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String newUsername) {
        username = newUsername;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String newPassword) {
        password = newPassword;
    }
}
