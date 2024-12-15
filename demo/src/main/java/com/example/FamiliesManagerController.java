package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FamiliesManagerController  {

    public void initialize() {
        // Không cần xử lý gì trong hàm khởi tạo
        // Đặt sidebar ra ngoài màn hình khi khởi chạy
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }
    }

    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu;

    @FXML
    private void menuClick() {
        // Kiểm tra nếu sidebar đã được khởi tạo
        if (sidebar != null) {
            double currentPosition = sidebar.getLayoutX();
            double sidebarWidth = sidebar.getPrefWidth();

            // Nếu sidebar hiện tại ở ngoài màn hình -> kéo vào
            if (currentPosition < 0) {
                sidebar.setLayoutX(0);
            } else {
                // Nếu sidebar đã hiển thị -> đẩy ra khỏi màn hình
                sidebar.setLayoutX(-sidebarWidth);
            }
        } else {
            System.out.println("Sidebar chưa được khởi tạo!");
        }
    }


    @FXML
    private void signout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn muốn đăng xuất");
        alert.showAndWait();
        if(alert.getResult()==ButtonType.OK){
            switchToSignIn();
        }


    }
    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }
    @FXML
    private void switchToGiaDinh() throws IOException {
        App.setRoot("FamiliesManager");
    }
    @FXML
    private void switchToDanCu() throws IOException {
        App.setRoot("ResidentsManager");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("fee");
    }
    @FXML
    private void switchToCanHo() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToTamTru() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToTamVang() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToSignIn(){

    }
    @FXML
    private void switchToAccount() {
        try {
            App.setRoot("account"); // Đảm bảo "account.fxml" tồn tại
        } catch (IOException e) {
            System.out.println("Lỗi: Không thể chuyển sang màn hình tài khoản. Kiểm tra đường dẫn của account.fxml.");
            e.printStackTrace();
        }
    }

}

