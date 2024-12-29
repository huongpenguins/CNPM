package com.example.Entities;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class CTQuyenGop {
     //String maKT;
    SimpleStringProperty maKT;
    SimpleStringProperty tenKT;
    SimpleStringProperty id;    // ma ho
    SimpleStringProperty ten; // ten 
    SimpleObjectProperty<LocalDate> ngaynop;
    SimpleIntegerProperty danop;


    public CTQuyenGop(String tenKT, String id,String ten, 
    LocalDate ngaynop, int danop) {
        this.id = new SimpleStringProperty(id);
        this.ten = new SimpleStringProperty(ten);
        this.tenKT= new SimpleStringProperty(tenKT);
        this.danop = new SimpleIntegerProperty(danop);
        this.ngaynop = new SimpleObjectProperty<>(ngaynop);
    }

    public String getMaKT() {
        return this.maKT.getValue();
    }

    public void setMaKT(String maKT) {
        this.tenKT.set(maKT);
    }
    public String getTenKT() {
        return this.tenKT.getValue();
    }

    public void setTenKT(String tenKT) {
        this.tenKT .set(tenKT);
    }

    public String getId() {
        return this.id.getValue();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTen() {
        return this.ten.getValue();
    }

    public void setTen(String ten) {
        this.ten.set(ten);
    }

    public LocalDate getNgaynop() {
        return this.ngaynop.getValue();
    }

    public void setNgaynop(LocalDate ngaynop) {
        this.ngaynop.set(ngaynop);
    }

    public int getDanop() {
        return this.danop.get();
    }

    public void setDanop(int danop) {
        this.danop.set(danop);
    }


}
