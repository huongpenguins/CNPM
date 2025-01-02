package com.example;

import java.io.IOException;

import com.example.Entities.KhoanThu;
import com.example.Entities.TamVang;
import com.example.dal.KhoanThuDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditFeeController {
    KhoanThu k;
    @FXML 
    TextField id_text,ten_text,ghichu;
    @FXML
    DatePicker batdau,hannop;
    @FXML
    Button save;
    @FXML
    ComboBox<String> loai,donvi;

    
    public void initialize(){
        id_text.setEditable(false);
        loai.getItems().add("Quyên góp");
        loai.getItems().add("Bắt buộc");
        donvi.getItems().add("Diện tích");
        donvi.getItems().add("Xe");
        donvi.getItems().add("Số điện/nước");
        donvi.getItems().add("Không");
    
    }
    @FXML
    private void xacnhan() throws IOException{
        if(id_text.getText().isEmpty()) return;
        if(ten_text.getText().isEmpty()) return;
        if(loai.getValue() == null) return;
        if(batdau.getValue()==null) return;
        if(hannop.getValue()==null) return;
        if(ghichu.getText().isEmpty()) return;
        if(donvi.getValue()==null) return;
        if(batdau.getValue().isAfter(hannop.getValue())) {
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ngày bắt đầu nộp tiền muộn hơn hạn nộp");
            return;
        }
        

            String[] columns = new String[]{"MaKhoanThu","TenKhoanThu","Loai","ThoiGianBatDau","ThoiGianKetThuc","SoTien","DonVi"};
            String[] types=new String[]{"string","string","string","date","date","int","string"};
            String[] newValue = {id_text.getText(), ten_text.getText(),loai.getValue().toString()
                ,batdau.getValue().toString() , hannop.getValue().toString(), ghichu.getText(),donvi.getValue()};
            StringBuffer condition = new StringBuffer("MaKhoanThu = ");
            condition.append("'").append(id_text.getText()).append("'");

            boolean t= KhoanThuDAL.update1("khoanthutbl", columns, types,newValue,condition.toString());
                
            if(t==true){
                Integer sotien=null;
                try {
                    sotien=Integer.parseInt(ghichu.getText());
                
                } catch (Exception e) {
                    
                } 
                k.setTen(ten_text.getText());
                k.setLoai(loai.getValue().toString());
                k.setBatdau(batdau.getValue());
                k.setDonvi(donvi.getValue());
                k.setHannop(hannop.getValue());
                k.setGhichu(sotien);
            }
            
            Stage thisStage = (Stage)save.getScene().getWindow();
            thisStage.close();

    }
}
