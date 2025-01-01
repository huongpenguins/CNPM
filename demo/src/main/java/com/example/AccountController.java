package com.example;

import java.io.IOException;
import java.sql.ResultSet;

import com.example.login.AccountManager;
import com.example.login.ChangePassword;
import com.example.login.LoginPage;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountController {
    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu,doimk,suaTTin;
    @FXML
    TextField hoten, diachi, email, sdt, cccd, chucvu, ngaysinh, gioitinh, manv;

    //hien menu chuyen man hinh
    public void initialize(){
        hoten.setText(AccountManager.getNameByMaNv(LoginPage.MaNV));
        diachi.setText(AccountManager.getDiaChiByMaNv(LoginPage.MaNV));
        email.setText(AccountManager.getEmailByMaNv(LoginPage.MaNV));
        sdt.setText(AccountManager.getSdtByMaNv(LoginPage.MaNV));
        cccd.setText(AccountManager.getCCCDByMaNv(LoginPage.MaNV));
        chucvu.setText(AccountManager.getChucVuByMaNv(LoginPage.MaNV));
        ngaysinh.setText(AccountManager.getNgaySinhByMaNv(LoginPage.MaNV).toString());
        gioitinh.setText(AccountManager.getGioiTinhByMaNv(LoginPage.MaNV));
        manv.setText(LoginPage.MaNV);
    }
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
        // FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add_fee.fxml"));
        // AccountChangeController controller = new AccountChangeController();
        // controller.setAccountInfo(hoten.getText(), diachi.getText(), email.getText(), sdt.getText(), cccd.getText(), chucvu.getText(), ngaysinh.getText(), gioitinh.getText(), manv.getText());
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
    private void switchToAccount() throws IOException {
        App.setRoot("account");
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
        App.setRoot("Apartment");
    }
    @FXML
    private void switchToTamTru() throws IOException {
        App.setRoot("tamtru");
    }
    @FXML
    private void switchToTamVang() throws IOException {
        App.setRoot("tamvang");
    }
    @FXML 
    private void switchToSignIn(){
        
    }
   
}
