
package com.example;

import java.io.IOException;

import com.example.Entities.TamTru;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTamTruController {
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
        if(id_text.getText().isEmpty()) return;
        if(dcthuongtru_text.getText().isEmpty()) return;
        if(dctamtru_text.getText().isEmpty()) return;
        if(ngaybdtamtru.getValue()==null) return;
        try {
            ngaybdtamtru.getValue();
        } catch (Exception e) {
            return ;
        }
        newTamTru = new TamTru(id_text.getText(), dctamtru_text.getText(), ngaybdtamtru.getValue());
        // luu cai tren vao csdl


        // lay ra ho ten dctamtru ung vs id_text tu csdl de set cho thuoc tinh
        // newTamTru.ten =
        //            . dctamtru =


    }

    public TamTru getNewTamTru() {
        return this.newTamTru;
    }

    public void setNewTamTru(TamTru newTamTru) {
        this.newTamTru = newTamTru;
    }


}
