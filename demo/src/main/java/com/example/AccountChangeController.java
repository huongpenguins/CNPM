package com.example;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AccountChangeController {
   
    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu,xacnhan;
    @FXML
    TextField hoten,sdt,cccd,email,diachi;
    @FXML
    CheckBox male,female;
    @FXML
    ImageView khunganh; 
    @FXML
    ComboBox chucvu;
    @FXML 
    Label checkhoten,checkemail,checkcccd,checksdt;
    @FXML
    DatePicker ngsinh;
    
    
    public void initialize(){
        
        chucvu.getItems().add("Kế toán");
        chucvu.getItems().add("Quản lý");
        chucvu.getItems().add("Nhà đầu tư");

        
        hoten.focusedProperty().addListener((ob,oldValue,newValue)->{
         
            if(oldValue==true&&newValue==false){
                
                String l = hoten.getText();
                System.out.println(l);
                if(l.length()==0) checkhoten.setText("Chưa nhập");
                else{
                    for(int i=0;i<l.length();i++){
                        if(Character.isLetter(l.charAt(i))==false){
                            if(l.charAt(i)!=' '){
                                checkhoten.setText("Nhập sai");
                            return;
                            }
                        }
                        
                    }
                    checkhoten.setText("");
                }
            }

        });
        
        cccd.focusedProperty().addListener((ob,oldValue,newValue)->{
         
            if(oldValue==true&&newValue==false){
                
                String l = cccd.getText();
                
                if(l.length()==0) checkcccd.setText("Chưa nhập");
                else{
                    if(l.length()!=12) {
                        checkcccd.setText("Nhập sai");
                        return;
                    }
                    for(int i=0;i<l.length();i++){
                        if(l.charAt(0)!='0'){
                                checkcccd.setText("Nhập sai");
                            return;
                            }
                        if(Character.isDigit(l.charAt(i))==false){
                            checkcccd.setText("Nhập sai");
                            return;
                        }
                        
                    }
                    checkcccd.setText("");
                }
            }

        });

        sdt.focusedProperty().addListener((ob,oldValue,newValue)->{
         
            if(oldValue==true&&newValue==false){
                
                String l = sdt.getText();
                
                if(l.length()==0) checksdt.setText("Chưa nhập");
                else{
                    if(l.length()!=10) {
                        checksdt.setText("Nhập sai");
                        return;
                    }
                    for(int i=0;i<l.length();i++){
                        if(l.charAt(0)!='0'){
                                checksdt.setText("Nhập sai");
                            return;
                            }
                        if(Character.isDigit(l.charAt(i))==false){
                            checksdt.setText("Nhập sai");
                            return;
                        }
                        
                    }
                    checksdt.setText("");
                }
            }

        });

        email.focusedProperty().addListener((ob,oldValue,newValue)->{
         
            if(oldValue==true&&newValue==false){
                
                String l = email.getText();
                
                if(l.length()==0) checkemail.setText("Chưa nhập");
                else{
                    if(!l.contains("@gmail.com")){
                        checkemail.setText("Nhập sai");
                        return;
                    }
                    checkemail.setText("");
                }
            }

        });

        
        
    }

   
    @FXML
    private void xacNhan() throws IOException{
        if (!hoten.getText().isEmpty() && !sdt.getText().isEmpty() && !cccd.getText().isEmpty() && 
        !diachi.getText().isEmpty() && chucvu.getValue() != null && ngsinh.getValue() != null){
                if(checkhoten.getText().isEmpty() && checkemail.getText().isEmpty() && 
                checkcccd.getText().isEmpty() && checksdt.getText().isEmpty())
                
                switchToAccount();
            }
        
    }
    
    @FXML
    private void checkMale(){
        if(female.isSelected()) female.setSelected(false);
    }
    @FXML 
    private void checkFemale(){
        if(male.isSelected()) male.setSelected(false);
    }

    @FXML
    private void menuClick(){
        if(sidebar.getLayoutX()<0){
            sidebar.setLayoutX(0);
        }
        else sidebar.setLayoutX(-sidebar.getWidth());
    }

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
        App.setRoot("secondary");
    }
    @FXML
    private void switchToDanCu() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("secondary");
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

    
}
