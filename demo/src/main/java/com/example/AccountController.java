package com.example;

import java.io.IOException;

import com.example.login.AccountManager;
import com.example.login.ChangePassword;
import com.example.login.LoginPage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountController {
    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu,doimk,suaTTin;

    //hien menu chuyen man hinh
    @FXML
    private void menuClick(){
        if(sidebar.getLayoutX()<0){
            sidebar.setLayoutX(0);
        }
        else sidebar.setLayoutX(-sidebar.getWidth());
    }

    // dang xuat
    @FXML 
    private void signout(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn muốn đăng xuất");
        alert.showAndWait();
        if(alert.getResult()==ButtonType.OK){
            switchToSignIn();
        }


    }
    // tat ca ham duoi deu chuyen sang man hinh khac
    @FXML
    private void switchToChangeAccount() throws IOException {
        App.setRoot("accountchange");
    }

    @FXML
    private void switchToDoiMK() throws IOException {

        // Tạo cửa sổ mới (ChangePassword)
        ChangePassword changePassword = new ChangePassword();
        Stage newStage = new Stage();  // Tạo một Stage mới cho cửa sổ thay đổi mật khẩu
        // Mở cửa sổ mới và tạm dừng thực thi các câu lệnh tiếp theo cho đến khi cửa sổ mới đóng lại
        changePassword.start(newStage);
        // Sau khi cửa sổ mới đóng lại, thực hiện tiếp các câu lệnh sau
        AccountManager accountManager = new AccountManager();
    }

    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }
    @FXML
    private void switchToGiaDinh() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToDanCu() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("khoanthu");
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
        App.setRoot("secondary");
    }
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
