package com.example;

import java.io.IOException;

import com.example.Entities.KhoanThu;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFeeController {
    KhoanThu newKhoanThu;
    @FXML 
    TextField id_text,ten_text,ghichu;
    @FXML
    DatePicker batdau,hannop;
    @FXML
    Button save;
    @FXML
    ComboBox<String> loai,donvi;

    
    public void initialize(){
        
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
        Integer sotien=null;
        try {
            sotien=Integer.parseInt(ghichu.getText());
          
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nhập sai số tiền");
            alert.showAndWait();
        } 


            this.newKhoanThu = new KhoanThu(id_text.getText(), ten_text.getText(),loai.getValue().toString()
            ,batdau.getValue() , hannop.getValue(), sotien,donvi.getValue());
            
        

        
        
    }
}
