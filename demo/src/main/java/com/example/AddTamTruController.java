
package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.CanHo;
import com.example.Entities.TamTru;

import com.example.dal.TamTruDAL;
import com.example.dal.NhanKhauDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTamTruController {
    ArrayList<TamTru> listNew=new ArrayList<>();
    TamTru newTamTru;

    @FXML
    TextField id_text,dcthuongtru_text, dctamtru_text;
    @FXML
    DatePicker ngaybdtamtru;
    @FXML
    Button save;


    public void initialize(){


    }
    @FXML
    private void xacnhan() throws IOException{
        id_text.setEditable(false);
        if(id_text.getText().isEmpty()) return;
        if(dcthuongtru_text.getText().isEmpty()) return;
        if(dctamtru_text.getText().isEmpty()) return;
        if(ngaybdtamtru.getValue()==null) return;
        try {
            ngaybdtamtru.getValue();
        } catch (Exception e) {
            return ;
        }

        String[] column = new String[]{"MaNhanKhau","DiaChiThuongTru", "DiaChiTamTru","NgayBatDauTamTru"};
        String [] types = new String[]{"string","string","string", "date"};

        String[] value = {id_text.getText(),dcthuongtru_text.getText(), dctamtru_text.getText(), ngaybdtamtru.getValue().toString()};
        Resident r = NhanKhauDAL.loadData(id_text.getText());

        if(r!=null){
            boolean t= TamTruDAL.insert1("tamtrutbl", column, types,value);
            String newMaTamTru = TamTruDAL.selectMaTrigger("tamtrutbl", "MaTamTru", column, types, value);
            newTamTru = new TamTru(newMaTamTru,r.getId(),r.getName(),r.getIdentityCard(),dcthuongtru_text.getText(),dctamtru_text.getText(),ngaybdtamtru.getValue());
            if(t==true){
                listNew.add(newTamTru);
            }

        }


    }

    public TamTru getNewTamTru() {
        return this.newTamTru;
    }

    public void setNewTamTru(TamTru newTamTru) {
        this.newTamTru = newTamTru;
    }


}
