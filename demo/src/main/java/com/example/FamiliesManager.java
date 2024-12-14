package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FamiliesManager extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Tải tệp FXML
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/FamiliesManager.fxml"));

            // Tạo Scene với kích thước đã định
            Scene scene = new Scene(root, 1000, 600);

            // Đặt tiêu đề cho cửa sổ
            stage.setTitle("Quản lý hộ gia đình");

            // Đặt Scene cho Stage
            stage.setScene(scene);

            // Hiển thị Stage
            stage.show();
        } catch (IOException e) {
            // In ra lỗi nếu không thể tải FXML
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Khởi chạy ứng dụng JavaFX
        launch(args);
    }
}