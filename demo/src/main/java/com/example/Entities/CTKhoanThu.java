package com.example.Entities;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class    CTKhoanThu {
    //String maKT;
    SimpleStringProperty maKT,trangthai;
    SimpleStringProperty tenKT;
    SimpleStringProperty id;    // ma ho
    SimpleStringProperty phong;
    SimpleStringProperty ten; // ten 
    SimpleObjectProperty<LocalDate> ngaynop;
    SimpleIntegerProperty tiennop,danop,conthieu;


    public CTKhoanThu(String maKT,String tenKT, String id,String phong,String ten, 
    LocalDate ngaynop, int tiennop,int danop) {
        this.maKT = new SimpleStringProperty(maKT);
        this.id = new SimpleStringProperty(id);
        this.phong = new SimpleStringProperty(phong);
        this.ten = new SimpleStringProperty(ten);
        this.tenKT= new SimpleStringProperty(tenKT);
        this.tiennop = new SimpleIntegerProperty(tiennop);
        this.danop = new SimpleIntegerProperty(danop);
        this.ngaynop = new SimpleObjectProperty<>(ngaynop);
        this.conthieu = new SimpleIntegerProperty(); 
        this.conthieu.bind(this.tiennop.subtract(this.danop.getValue()));
        this.trangthai = new SimpleStringProperty(danop > tiennop ? "Dư" : ( danop == tiennop ? "Xong" : "Thiếu"));
        this.danop.addListener((obs, oldVal, newVal) -> {
            this.trangthai.set(this.getDanop() > this.getTiennop() ? "Dư": (this.getDanop() == this.getTiennop() ?  "Xong" : "Thiếu"));
        });
    }

    public String getPhong() {
        return this.phong.getValue();
    }

    public void setPhong(String phong) {
         this.phong.set(phong);
    }

    public String getTrangthai() {
        return this.trangthai.getValue();
    }

    public void setTrangthai(String trangthai) {
        
        this.trangthai.set(trangthai);
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

    public int getTiennop() {
        return this.tiennop.get();
    }

    public void setTiennop(int tiennop) {
        this.tiennop.set(tiennop);
    }

    public int getDanop() {
        return this.danop.get();
    }

    public void setDanop(int danop) {
        this.danop.set(danop);
    }
    public int getConthieu() {
        return this.conthieu.get();
    }

    public void setConthieu(int conthieu) {
        this.tiennop.set(conthieu);
    }

}
