package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.TamVang;
import com.example.dal.KhoanThuDAL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTamVangController {
    TamVang newTamVang;
    String maTamVang;
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

        String[] colums = new String[]{"MaTamVang","MaNhanKhau","ThoiGianBatDau","LyDo"};
        String[] types=new String[]{"string","string","date","string"};
        String[] newValue = {maTamVang,id_text.getText(), ngayvang.getValue().toString(),lydo_text.getText()};
        StringBuffer condition = new StringBuffer("MaTamVang = ");
        condition.append("'").append(maTamVang).append("'");

        boolean t= KhoanThuDAL.update1("tamvangtbl", colums, types,newValue,condition.toString());
        
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
