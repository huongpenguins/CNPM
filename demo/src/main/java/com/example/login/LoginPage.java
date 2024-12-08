package com.example.login;

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

public class LoginPage extends Application {

    @Override
    public void start(Stage loginStage) {
        // Nhãn tên đăng nhập
        Label lblUsername = new Label("Tên đăng nhập/sdt/email:");
        lblUsername.setTextFill(Color.DARKSLATEBLUE);
        lblUsername.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Ô nhập tên đăng nhập
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Nhập tên đăng nhập/sdt/email");
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

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnAction(event -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            if ((AccountManager.getUsername().equals(username) ||
                    AccountManager.getEmail().equals(username) ||
                    AccountManager.getPhonenumber().equals(username)) &&
                    AccountManager.getPassword().equals(password)) {
                lblMessage.setText("Đăng nhập thành công, vui lòng chờ!");
                lblMessage.setTextFill(Color.GREEN);

                // Hiệu ứng chờ 2 giây trước khi chuyển giao diện
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    App app = new App();
                    try {
                        loginStage.close();
                        app.start(new Stage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                pause.play();
            } else {
                lblMessage.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
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
