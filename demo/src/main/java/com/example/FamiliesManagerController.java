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
    }

    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu;

    @FXML
    private void menuClick(){
        if(sidebar.getLayoutX()<0){
            sidebar.setLayoutX(0);
        }
        else sidebar.setLayoutX(-sidebar.getWidth());
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
        App.setRoot("com/example/home.fxml");
    }
    @FXML
    private void switchToGiaDinh() throws IOException {
        App.setRoot("FamiliesManager");
    }
    @FXML
    private void switchToDanCu() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("com/example/fee.fxml");
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
    private void switchToAccount() throws IOException {
        App.setRoot("account");
    }
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}

