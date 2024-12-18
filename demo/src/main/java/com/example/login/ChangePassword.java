package com.example.login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.security.NoSuchAlgorithmException;

public class ChangePassword extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Đổi mật khẩu");

        // Tạo layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Set background color for the grid
        grid.setStyle("-fx-background-color: #f4f6f9;");

        // Nhập mật khẩu cũ
        Label oldPasswordLabel = new Label("Mật khẩu cũ:");
        oldPasswordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-radius: 5px;");
        grid.add(oldPasswordLabel, 0, 0);
        grid.add(oldPasswordField, 1, 0);

        // Nhập mật khẩu mới
        Label newPasswordLabel = new Label("Mật khẩu mới:");
        newPasswordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-radius: 5px;");
        grid.add(newPasswordLabel, 0, 1);
        grid.add(newPasswordField, 1, 1);

        // Nhập lại mật khẩu mới
        Label confirmNewPasswordLabel = new Label("Nhập lại mật khẩu mới:");
        confirmNewPasswordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        PasswordField confirmNewPasswordField = new PasswordField();
        confirmNewPasswordField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-radius: 5px;");
        grid.add(confirmNewPasswordLabel, 0, 2);
        grid.add(confirmNewPasswordField, 1, 2);

        // Nút đổi mật khẩu
        Button changePasswordButton = new Button("Đổi mật khẩu");
        changePasswordButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;");
        changePasswordButton.setOnMouseEntered(e -> changePasswordButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;"));
        changePasswordButton.setOnMouseExited(e -> changePasswordButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;"));
        grid.add(changePasswordButton, 1, 3);

        // Hiển thị thông báo
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c;");
        grid.add(messageLabel, 1, 4);

        // Xử lý sự kiện đổi mật khẩu
        changePasswordButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmNewPassword = confirmNewPasswordField.getText();

            // Kiểm tra nhập liệu
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                messageLabel.setText("Vui lòng nhập đầy đủ thông tin.");
            } else if (!newPassword.equals(confirmNewPassword)) {
                messageLabel.setText("Mật khẩu mới không khớp.");
            } else {
                // Giả lập kiểm tra mật khẩu cũ
                boolean isOldPasswordCorrect = false; // Thay thế bằng logic thực tế
                try {
                    isOldPasswordCorrect = PasswordHasher.checkPassword(oldPassword, AccountManager.getPasswordByMaNv(LoginPage.Ma_nv));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                if (isOldPasswordCorrect) {
                    AccountManager.updatePasswordByMa_nv(newPassword, LoginPage.Ma_nv);
                    messageLabel.setText("Đổi mật khẩu thành công!");
                    messageLabel.setTextFill(Color.GREEN); // Success message in green
                } else {
                    messageLabel.setText("Mật khẩu cũ không đúng.");
                }
            }
        });

        // Hiển thị giao diện
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//7R6F21R5VN2TWWN66SWFXP44