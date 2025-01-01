package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.KhoanThu;
import com.example.dal.KhoanThuDAL;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddFeeController {
    ArrayList<KhoanThu> listNew=new ArrayList<>();
    KhoanThu newKhoanThu;
    @FXML 
    TextField ten_text,ghichu;
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

            KhoanThuDAL khoanThuDAL =new KhoanThuDAL();
            String[] colums = new String[]{"TenKhoanThu","Loai","ThoiGianBatDau","ThoiGianKetThuc","SoTien","DonVi"};
            String[] types=new String[]{"string","string","date","date","int","string"};
            String[] newValue = { ten_text.getText(),loai.getValue().toString()
                ,batdau.getValue().toString() , hannop.getValue().toString(), ghichu.getText(),donvi.getValue()};
            boolean t= khoanThuDAL.insert1("khoanthutbl", colums, types,newValue);
            if(t==true){
                String id = khoanThuDAL.selectMaTrigger("khoanthutbl", "MaKhoanThu", colums, types, newValue);
                this.newKhoanThu = new KhoanThu(id, ten_text.getText(),loai.getValue().toString()
            ,batdau.getValue() , hannop.getValue(), sotien,donvi.getValue());
                listNew.add(newKhoanThu);

            }
            
            ten_text.clear();
            loai.setValue(null);
            batdau.setValue(null);
            hannop.setValue(null);
            ghichu.clear();
            donvi.setValue(null);
    }
}
