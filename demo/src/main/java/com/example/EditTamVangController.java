package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.TamVang;
import com.example.dal.TamVangDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTamVangController {
     TamVang newTamVang;

    @FXML 
    TextField id_text,lydo_text;
    @FXML
    DatePicker ngayvang;
    @FXML
    Button save;

    
    public void initialize(){


    }
    @FXML
    private void xacnhan() throws IOException{
        if(id_text.getText().isEmpty()) return;
        if(lydo_text.getText().isEmpty()) return;
        if(ngayvang.getValue()==null) return;
        try {
            ngayvang.getValue();
        } catch (Exception e) {
            return ;
        }
        newTamVang = new TamVang(id_text.getText(),lydo_text.getText(),ngayvang.getValue());
        ArrayList<Object[]> newTV = new ArrayList<>();
        TamVangDAL tamVangDAL = new TamVangDAL();
        String[] colums = new String[]{"MaNhanKhau","ThoiGianBatDau","LyDo"};
        String[] types=new String[]{"string","date","string"};
        newTV = tamVangDAL.search("tamvangtbl", "MaNhanKhau", id_text.getText());
        
        
        

        
        // lay ra ho ten ,cccd, phong ung vs id_text tu csdl de set cho thuoc tinh
        // newTamVang.ten = 
        //            .cccd = 
        //            . phong = 
        Stage thisStage = (Stage)save.getScene().getWindow();
        thisStage.close();
        
    }

    public TamVang getNewTamVang() {
        return this.newTamVang;
    }

    public void setNewTamVang(TamVang newTamVang) {
        this.newTamVang = newTamVang;
    }

}
