package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.TamTru;
import com.example.dal.KhoanThuDAL;
import com.example.dal.TamTruDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTamTruController {
    TamTru newTamTru;

    @FXML
    TextField id_text,dctamtru_text;
    @FXML
    DatePicker ngaybdtamtru;
    @FXML
    Button save;


    public void initialize(){


    }
    @FXML
    private void xacnhan() throws IOException{
        if(id_text.getText().isEmpty()) return;
        if(dctamtru_text.getText().isEmpty()) return;
        if(ngaybdtamtru.getValue()==null) return;
        try {
            ngaybdtamtru.getValue();
        } catch (Exception e) {
            return ;
        }

        ArrayList<Object[]> newTV = new ArrayList<>();
        TamTruDAL tamtruDAL = new TamTruDAL();
        String[] colums = new String[]{"MaNhanKhau","Dc Tạm Trú","Ngày bd Tạm Trú"};
        String[] types=new String[]{"string","string","date"};
        //newTT = tamTruDAL.update1("tamtrutbl", column,types,);
        String[] newValue = {id_text.getText(), ngaybdtamtru.getValue().toString(),dctamtru_text.getText()};
        StringBuffer condition = new StringBuffer("MaTamTru = ");
        condition.append("'").append(id_text.getText()).append("'");

        boolean t= KhoanThuDAL.update1("khoanthutbl", colums, types,newValue,condition.toString());

        Stage thisStage = (Stage)save.getScene().getWindow();
        thisStage.close();

    }

    public TamTru getNewTamTru() {
        return this.newTamTru;
    }

    public void setNewTamTru(TamTru newTamTru) {
        this.newTamTru = newTamTru;
    }

}
