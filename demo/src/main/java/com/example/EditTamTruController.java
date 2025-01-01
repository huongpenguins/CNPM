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
    String maTamTru;
    @FXML
    TextField id_text,dctamtru_text, dcthuongtru_text;
    @FXML
    DatePicker ngaybdtamtru;
    @FXML
    Button save;


    public void initialize(){


    }
    @FXML
    private void xacnhan() throws IOException{
        if(id_text.getText().isEmpty()) return;
        if(dcthuongtru_text.getText().isEmpty()) return;
        if(dctamtru_text.getText().isEmpty()) return;
        if(ngaybdtamtru.getValue()==null) return;
        try {
            ngaybdtamtru.getValue();
        } catch (Exception e) {
            return ;
        }

        ArrayList<Object[]> newTV = new ArrayList<>();
        TamTruDAL tamtruDAL = new TamTruDAL();
        String[] colums = new String[]{"MaTamTru","MaNhanKhau","DiaChiThuongTru", "DiaChiTamTru","NgayBatDauTamTru"};
        String[] types=new String[]{"string","string","string", "string","date"};
        //newTT = tamTruDAL.update1("tamtrutbl", column,types,);
        String[] newValue = {maTamTru,id_text.getText(),dcthuongtru_text.getText(),dctamtru_text.getText(), ngaybdtamtru.getValue().toString()};
        StringBuffer condition = new StringBuffer("MaTamTru = ");
        condition.append("'").append(maTamTru).append("'");

        boolean t= TamTruDAL.update1("tamtrutbl", colums, types,newValue,condition.toString());

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
