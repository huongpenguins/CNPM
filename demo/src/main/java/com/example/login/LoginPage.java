package com.example.login;

import com.example.connect.connect_mysql;
import com.example.App;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginPage extends Application {
    public static String MaNV = null;//xác minh ta khoanrn dang dang nhap
    public static String sdt_or_email = null;
    @Override
    public void start(Stage loginStage) {
        // Nhãn tên đăng nhập
        Label lblUsername = new Label("Nhập sdt/email:");
        lblUsername.setTextFill(Color.DARKSLATEBLUE);
        lblUsername.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Ô nhập tên đăng nhập
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Nhập sdt/email");
        txtUsername.setStyle("-fx-font-size: 14px; -fx-border-color: lightgray;");

        // Nhãn mật khẩu
        Label lblPassword = new Label("Mật khẩu:");
        lblPassword.setTextFill(Color.DARKSLATEBLUE);
        lblPassword.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Ô nhập mật khẩu
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Nhập mật khẩu");
        txtPassword.setStyle("-fx-font-size: 14px; -fx-border-color: lightgray;");

        // Nút đăng nhập
        Button btnLogin = new Button("Đăng nhập");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-radius: 5px;");

        // Nút quên mật khẩu
        Button btnForgotPassword = new Button("Quên mật khẩu");
        btnForgotPassword.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-radius: 5px;");

        // Nhãn hiển thị thông báo
        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        btnLogin.setOnAction(event -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            // Xác thực đăng nhập với cơ sở dữ liệu
            boolean isValid = false;

            try (Connection conn = connect_mysql.getConnection()) {
                String query = "SELECT * FROM admin_account WHERE email = ? OR sdt = ?";  // Thay đổi bảng và cột tùy theo cấu trúc của bạn
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, username);
                    stmt.setString(2, username);  // Email hoặc số điện thoại

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String storedPassword = rs.getString("password");  // Thay "password" bằng tên cột mật khẩu trong cơ sở dữ liệu của bạn
                            if (PasswordHasher.checkPassword(password, storedPassword)) {
                                isValid = true;
                            }
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (isValid) {
                lblMessage.setText("Đăng nhập thành công, vui lòng chờ!");
                lblMessage.setTextFill(Color.GREEN);

                // Hiệu ứng chờ 2 giây trước khi chuyển giao diện
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    App app = new App();
                    try {
                        MaNV = AccountManager.getMaNvByPhoneOrEmail(username);
                        loginStage.close(); // Đóng trang đăng nhập
                        app.start(new Stage()); // Mở ứng dụng chính
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                pause.play();
            } else {
                lblMessage.setText("Tên sdt/email hoặc mật khẩu không đúng.");
                lblMessage.setTextFill(Color.RED);
            }
        });



        // Xử lý sự kiện quên mật khẩu
        btnForgotPassword.setOnAction(event -> {
            ResetPasswordApp resetPasswordApp = new ResetPasswordApp();
            resetPasswordApp.start(new Stage());
        });

        // Tạo bố cục dạng lưới
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.add(lblUsername, 0, 0);
        gridPane.add(txtUsername, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtPassword, 1, 1);

        // Tạo hộp nút bấm
        HBox buttonBox = new HBox(10, btnForgotPassword, btnLogin);
        buttonBox.setAlignment(Pos.CENTER);

        // Bố cục chính
        VBox root = new VBox(20, gridPane, buttonBox, lblMessage);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setEffect(new DropShadow(10, Color.GRAY));

        // Cấu hình Scene
        Scene scene = new Scene(root, 450, 300);
        loginStage.setTitle("Đăng nhập");
        loginStage.setResizable(false);
        loginStage.setScene(scene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
