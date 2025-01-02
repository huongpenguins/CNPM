package com.example;

import java.io.IOException;
import java.time.LocalDate;

import com.example.Entities.HoaDon;
import com.example.dal.HoaDonDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ThanhToanController {
    HoaDon hoaDon;
    String maKhoanThu,tenKhoanThu;
    LocalDate ngaynop;
    int soTien;
    String maHo;

    @FXML
    TextField id,sotiennop;
    @FXML
    Button save,tralai;

    @FXML
    private void xacnhan() throws IOException{
        if(id.getText().isEmpty()) return;
        if(id.getText().equals(maHo)==false){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Không khớp thông tin");
            alert.showAndWait();
        }
        if(sotiennop.getText().isEmpty()) return;
        Integer sotien=null;
        try {
            sotien=Integer.parseInt(sotiennop.getText());
          
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nhập sai số tiền");
            alert.showAndWait();
        } 
        this.ngaynop = LocalDate.now();
        this.hoaDon = new HoaDon(id.getText(),maKhoanThu, sotien, ngaynop);
    // luu hoa Don vao csdl
            HoaDonDAL hoaDonDAL= new HoaDonDAL();
            String[] colums = new String[]{"MaKhoanThu","MaHoGiaDinh","SoTienDaNop","ThoiDiemNop"};
            String[] types=new String[]{"string","string","int","date"};
            String[] newValue = {maKhoanThu, maHo,sotiennop.getText(),LocalDate.now().toString()};
            boolean t = hoaDonDAL.insert1("hoadontbl", colums, types,newValue);
            if(t==true){
                this.hoaDon = new HoaDon(maHo,maKhoanThu,sotien,LocalDate.now());
                
            }
            
        Stage thisStage = (Stage)save.getScene().getWindow();
        thisStage.close();
    }
    
}

