package com.example.login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginPage extends Application {

    @Override
    public void start(Stage loginStage) {
        // Tạo nhãn cho tên đăng nhập
        Label lblUsername = new Label("Tên đăng nhập/sdt/email:");
        lblUsername.setEffect(new Blend()); // Áp dụng hiệu ứng
        lblUsername.setTextFill(Color.BLUEVIOLET); // Đặt màu chữ

        // Tạo ô nhập cho tên đăng nhập
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Nhập tên đăng nhập/sdt/email"); // Gợi ý nhập liệu

        // Tạo nhãn cho mật khẩu
        Label lblPassword = new Label("Mật khẩu:");
        lblPassword.setEffect(new Blend()); // Áp dụng hiệu ứng
        lblPassword.setTextFill(Color.BLUEVIOLET); // Đặt màu chữ

        // Tạo ô nhập mật khẩu
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Nhập mật khẩu"); // Gợi ý nhập liệu

        // Tạo các nút
        Button btnLogin = new Button("Đăng nhập");
        Button btnForgotPassword = new Button("Quên mật khẩu");

        // Tạo nhãn hiển thị thông báo
        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-padding: 10;"); // Căn lề cho nhãn

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnAction(event -> {
            String username = txtUsername.getText(); // Lấy tên đăng nhập từ ô nhập
            String password = txtPassword.getText(); // Lấy mật khẩu từ ô nhập

            // Kiểm tra thông tin đăng nhập
            if ((AccountManager.getUsername().equals(username) ||
                    AccountManager.getEmail().equals(username) ||
                    AccountManager.getPhonenumber().equals(username)) &&
                    AccountManager.getPassword().equals(password)) {
                lblMessage.setText("Đăng nhập thành công!");
                lblMessage.setStyle("-fx-text-fill: green;"); // Đổi màu chữ thông báo thành màu xanh
            } else {
                lblMessage.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
                lblMessage.setStyle("-fx-text-fill: red;"); // Đổi màu chữ thông báo thành màu đỏ
            }
        });

        // Xử lý sự kiện "Quên mật khẩu"
        btnForgotPassword.setOnAction(event -> {
            ResetPasswordApp resetPasswordApp = new ResetPasswordApp();
            resetPasswordApp.start(new Stage()); // Mở giao diện quên mật khẩu
        });

        // Tạo bố cục dạng lưới cho các trường nhập
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Khoảng cách ngang giữa các cột
        gridPane.setVgap(10); // Khoảng cách dọc giữa các hàng
        gridPane.setPadding(new Insets(10)); // Khoảng cách viền ngoài

        // Thêm các thành phần vào lưới
        gridPane.add(lblUsername, 0, 0); // Nhãn "Tên đăng nhập"
        gridPane.add(txtUsername, 1, 0); // Ô nhập "Tên đăng nhập"
        gridPane.add(lblPassword, 0, 1); // Nhãn "Mật khẩu"
        gridPane.add(txtPassword, 1, 1); // Ô nhập "Mật khẩu"

        // Tạo hộp chứa nút bấm và căn giữa
        HBox buttonBox = new HBox(10, btnForgotPassword, btnLogin); // Khoảng cách giữa các nút là 10
        buttonBox.setAlignment(Pos.CENTER);

        // Tạo bố cục chính dạng VBox
        VBox root = new VBox(20, gridPane, buttonBox, lblMessage);// Khoảng cách giữa các thành phần là 10
        root.setAlignment(Pos.CENTER); // Căn giữa các thành phần
        root.setPadding(new Insets(20)); // Căn lề cho bố cục

        // Tạo Scene và cấu hình Stage
        Scene scene = new Scene(root, 400, 250); // Kích thước cửa sổ là 350x250
        loginStage.setTitle("Đăng nhập"); // Tiêu đề cửa sổ
        loginStage.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
        loginStage.setScene(scene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng JavaFX
    }
}
