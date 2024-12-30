package com.example;

import java.io.IOException;
import java.util.ArrayList;

import com.example.Entities.CanHo;
import com.example.Entities.KhoanThu;
import com.example.Entities.TamVang;
import com.example.dal.CanHoDAL;
import com.example.dal.NhanKhauDAL;
import com.example.dal.TamVangDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTamVangController {
    ArrayList<TamVang> listNew=new ArrayList<>();
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
        id_text.setEditable(false);
        if(id_text.getText().isEmpty()) return;
        if(lydo_text.getText().isEmpty()) return;
        if(ngayvang.getValue()==null) return;
        try {
            ngayvang.getValue();
        } catch (Exception e) {
            return ;
        }

        
        String[] column = new String[]{"MaNhanKhau","ThoiGianBatDau","LyDo"};
        String [] types = new String[]{"string","date","string"};
        String[] value = {id_text.getText(),ngayvang.getValue().toString(),lydo_text.getText()};
        Resident r =NhanKhauDAL.loadData(id_text.getText());

                if(r!=null){
                    CanHo c = CanHoDAL.loadData(r.getHouseholdId());
                    newTamVang = new TamVang(r.getName(),r.getIdentityCard(),c.getTenCanHo(),lydo_text.getText(),ngayvang.getValue());
                    boolean t= TamVangDAL.insert1("khoanthutbl", column, types,value);
                    if(t==true){
                    listNew.add(newTamVang);
                        }

            }

        
    }

    public TamVang getNewTamVang() {
        return this.newTamVang;
    }

    public void setNewTamVang(TamVang newTamVang) {
        this.newTamVang = newTamVang;
    }



}
