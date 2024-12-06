package com.example.login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;

public class ResetPasswordApp extends Application {

    private String verificationCode; // Mã xác thực giả lập
    private String userEmail = AccountManager.getEmail(); // Email giả lập của người dùng
    private String userPhoneNumber = AccountManager.getPhonenumber(); //SDT giả lâp người dùng

    @Override
    public void start(Stage primaryStage) {
        // Giao diện đăng nhập
        Label lblInstruction = new Label("Nhập email/sđt để đặt lại mật khẩu:");
        TextField txtEmailorPhoneNumber = new TextField();
        Button btnSendCode = new Button("Gửi mã xác thực");
        Label lblStatus = new Label();

        btnSendCode.setOnAction(event -> {
            String email = txtEmailorPhoneNumber.getText();
            String phone_number = txtEmailorPhoneNumber.getText();
            if (email.isEmpty() && phone_number.isEmpty()) {
                lblStatus.setText("Vui lòng nhập email/sdt.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else if (email.equals(userEmail)) {
                sendVerificationCode(email);
                lblStatus.setText("Mã xác thực đã được gửi!");
                lblStatus.setStyle("-fx-text-fill: green;");
                openVerificationWindow(primaryStage); // Mở cửa sổ nhập mã xác thực
            } else if (phone_number.equals(userPhoneNumber)) {
                sendVerificationCode(phone_number);
                lblStatus.setText("Mã xác thực đã được gửi!");
                lblStatus.setStyle("-fx-text-fill: green;");
                openVerificationWindow(primaryStage); // Mở cửa sổ nhập mã xác thực
            }   else {
                lblStatus.setText("Email/sdt không đúng.");
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        VBox vbox = new VBox(10, lblInstruction, txtEmailorPhoneNumber, btnSendCode, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 350, 200);
        primaryStage.setTitle("Quên mật khẩu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Gửi mã xác thực (giả lập)
    private void sendVerificationCode(String email_or_phone_number) {
        Random random = new Random();
        verificationCode = String.format("%06d", random.nextInt(999999)); // Tạo mã 6 chữ số
        System.out.println("Mã xác thực gửi đến email " + email_or_phone_number + ": " + verificationCode);
    }

    // Cửa sổ xác thực mã
    private void openVerificationWindow(Stage parentStage) {
        Stage verificationStage = new Stage();
        verificationStage.setTitle("Xác thực mã");

        // Đặt modality để chặn tương tác với cửa sổ gốc
        verificationStage.initModality(Modality.APPLICATION_MODAL);

        // Liên kết với cửa sổ gốc
        verificationStage.initOwner(parentStage);

        Label lblInstruction = new Label("Nhập mã xác thực đã nhận:");
        TextField txtCode = new TextField();
        Button btnVerify = new Button("Xác thực");
        Label lblStatus = new Label();

        btnVerify.setOnAction(event -> {
            String inputCode = txtCode.getText();
            if (inputCode.isEmpty()) {
                lblStatus.setText("Vui lòng nhập mã xác thực.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else if (!inputCode.equals(verificationCode)) {
                lblStatus.setText("Mã xác thực không đúng.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else {
                lblStatus.setText("Xác thực thành công!");
                lblStatus.setStyle("-fx-text-fill: green;");
                openResetPasswordWindow(parentStage); // Mở cửa sổ đặt lại mật khẩu
                verificationStage.close();
            }
        });

        VBox vbox = new VBox(10, lblInstruction, txtCode, btnVerify, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 350, 200);
        verificationStage.setScene(scene);
        verificationStage.show();
    }

    // Cửa sổ đặt lại mật khẩu
    private void openResetPasswordWindow(Stage parentStage) {
        Stage resetPasswordStage = new Stage();
        resetPasswordStage.setTitle("Đặt lại mật khẩu");

        // Đặt modality để chặn tương tác với cửa sổ gốc
        resetPasswordStage.initModality(Modality.APPLICATION_MODAL);

        // Liên kết với cửa sổ gốc
        resetPasswordStage.initOwner(parentStage);


        Label lblNewPassword = new Label("Mật khẩu mới:");
        PasswordField txtNewPassword = new PasswordField();
        Label lblConfirmPassword = new Label("Xác nhận mật khẩu:");
        PasswordField txtConfirmPassword = new PasswordField();
        Button btnReset = new Button("Đặt lại mật khẩu");
        Label lblStatus = new Label();

        btnReset.setOnAction(event -> {
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                lblStatus.setText("Vui lòng nhập đầy đủ thông tin.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else if (!newPassword.equals(confirmPassword)) {
                lblStatus.setText("Mật khẩu xác nhận không khớp.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else {
                lblStatus.setText("Đặt lại mật khẩu thành công!");
                AccountManager.setPassword(newPassword);
                lblStatus.setStyle("-fx-text-fill: green;");
                resetPasswordStage.close();
            }
        });

        VBox vbox = new VBox(10, lblNewPassword, txtNewPassword, lblConfirmPassword, txtConfirmPassword, btnReset, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 400, 250);
        resetPasswordStage.setResizable(true);
        resetPasswordStage.setScene(scene);
        resetPasswordStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
