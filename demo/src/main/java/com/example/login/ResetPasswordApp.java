package com.example.login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.concurrent.Task;
import javafx.stage.Window;

import java.util.Properties;
import java.util.Random;

public class ResetPasswordApp extends Application {

    private String verificationCode;
    private String current_user_phone_number = null;
    private String current_user_email = null;

    @Override
    public void start(Stage primaryStage) {
        Label lblInstruction = new Label("Nhập email hoặc số điện thoại để đặt lại mật khẩu:");
        lblInstruction.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");

        TextField txtEmailorPhoneNumber = new TextField();
        txtEmailorPhoneNumber.setPromptText("Nhập email/sdt");
        txtEmailorPhoneNumber.setStyle("-fx-font-size: 14px; -fx-border-color: #D5D8DC;");

        Button btnSendCode = new Button("Gửi mã xác thực");
        btnSendCode.setStyle("-fx-background-color: #5DADE2; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;");

        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-font-size: 13px;");

        btnSendCode.setOnAction(event -> {
            String input = txtEmailorPhoneNumber.getText();
            if (input.isEmpty()) {
                lblStatus.setText("Vui lòng nhập email hoặc số điện thoại.");
                lblStatus.setStyle("-fx-text-fill: red;");
            } else if (AccountManager.isEmailExist(input)) {
                current_user_email = input;
                sendVerificationCode(lblStatus);
            } else if (AccountManager.isPhoneNumberExist(input)) {
                current_user_phone_number = input;
                sendVerificationCode(lblStatus);
            } else {
                lblStatus.setText("Email hoặc số điện thoại không đúng.");
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        VBox vbox = new VBox(15, lblInstruction, txtEmailorPhoneNumber, btnSendCode, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #F2F4F4; -fx-border-color: #D5D8DC; -fx-border-radius: 5px;");
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setTitle("Quên mật khẩu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendVerificationCode(Label lblStatus) {
        Random random = new Random();
        verificationCode = String.format("%06d", random.nextInt(999999));

        // Cài đặt thông tin SMTP
        String host = "smtp.gmail.com";
        final String username = "phandang30122003@gmail.com";  // Địa chỉ email người gửi
        final String password = "Dang&Phan@@@";  // Mật khẩu email người gửi

        // Cài đặt thuộc tính cho session gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo session với SMTP server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Task<Void> sendTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Tạo tin nhắn gửi đi
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));  // Email người gửi
                    String recipient = null;
                    if(current_user_email == null) {
                        String Manv = AccountManager.getMaNvByPhoneOrEmail(current_user_phone_number);
                        recipient = AccountManager.getEmailByMaNv(Manv);
                    } else recipient = current_user_email;
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));  // Email người nhận
                    message.setSubject("Mã xác thực");

                    // Nội dung email
                    String emailContent = "Mã xác thực của bạn là: " + verificationCode;
                    message.setText(emailContent);

                    // Gửi email
                    Transport.send(message);

                    // Cập nhật giao diện khi gửi thành công
                    Platform.runLater(() -> {
                        lblStatus.setText("Mã xác thực đã được gửi!");
                        lblStatus.setStyle("-fx-text-fill: green;");
                        openVerificationWindow(lblStatus.getScene().getWindow());
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        lblStatus.setText("Không thể gửi mã xác thực. Vui lòng thử lại.");
                        lblStatus.setStyle("-fx-text-fill: red;");
                    });
                }
                return null;
            }
        };

        new Thread(sendTask).start();
    }

    private void openVerificationWindow(Window parentStage) {
        Stage verificationStage = new Stage();
        verificationStage.setTitle("Xác thực mã");
        verificationStage.initModality(Modality.APPLICATION_MODAL);
        verificationStage.initOwner(parentStage);

        Label lblInstruction = new Label("Nhập mã xác thực đã nhận:");
        lblInstruction.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");

        TextField txtCode = new TextField();
        txtCode.setPromptText("Nhập mã xác thực");
        txtCode.setStyle("-fx-font-size: 14px; -fx-border-color: #D5D8DC;");

        Button btnVerify = new Button("Xác thực");
        btnVerify.setStyle("-fx-background-color: #5DADE2; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;");

        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-font-size: 13px;");

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
                openResetPasswordWindow(parentStage);
                verificationStage.close();
            }
        });

        VBox vbox = new VBox(15, lblInstruction, txtCode, btnVerify, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #F2F4F4; -fx-border-color: #D5D8DC; -fx-border-radius: 5px;");
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 250);
        verificationStage.setScene(scene);
        verificationStage.show();
    }

    private void openResetPasswordWindow(Window parentStage) {
        Stage resetPasswordStage = new Stage();
        resetPasswordStage.setTitle("Đặt lại mật khẩu");
        resetPasswordStage.initModality(Modality.APPLICATION_MODAL);
        resetPasswordStage.initOwner(parentStage);

        Label lblNewPassword = new Label("Mật khẩu mới:");
        lblNewPassword.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");

        PasswordField txtNewPassword = new PasswordField();
        txtNewPassword.setPromptText("Nhập mật khẩu mới");
        txtNewPassword.setStyle("-fx-font-size: 14px; -fx-border-color: #D5D8DC;");

        Label lblConfirmPassword = new Label("Xác nhận mật khẩu:");
        lblConfirmPassword.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");

        PasswordField txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("Nhập lại mật khẩu");
        txtConfirmPassword.setStyle("-fx-font-size: 14px; -fx-border-color: #D5D8DC;");

        Button btnReset = new Button("Đặt lại mật khẩu");
        btnReset.setStyle("-fx-background-color: #5DADE2; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5px;");

        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-font-size: 13px;");

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
                if (current_user_email != null) {
                    AccountManager.updatePasswordByEmail(newPassword, current_user_email);
                } else {
                    AccountManager.updatePasswordByPhone(newPassword, current_user_phone_number);
                }
                lblStatus.setStyle("-fx-text-fill: green;");
                resetPasswordStage.close();
            }
        });

        VBox vbox = new VBox(15, lblNewPassword, txtNewPassword, lblConfirmPassword, txtConfirmPassword, btnReset, lblStatus);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #F2F4F4; -fx-border-color: #D5D8DC; -fx-border-radius: 5px;");
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 450, 300);
        resetPasswordStage.setScene(scene);
        resetPasswordStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
