package com.example.Entities;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class KhoanThu {
    SimpleStringProperty id;
    SimpleStringProperty ten;
    SimpleStringProperty loai;
    SimpleObjectProperty<LocalDate> batdau;
    SimpleObjectProperty<LocalDate> hannop;
    SimpleIntegerProperty ghichu;

    public KhoanThu(String id, String ten,String loai, 
    LocalDate batdau, LocalDate hannop,int ghichu) {
        this.id = new SimpleStringProperty(id);
        this.ten = new SimpleStringProperty(ten);
        this.loai = new SimpleStringProperty(loai);
        this.ghichu = new SimpleIntegerProperty(ghichu);
        this.batdau = new SimpleObjectProperty<>(batdau);
        this.hannop = new SimpleObjectProperty<>(hannop);
        
    }

    public String getId() {
        return this.id.get();
    }

    public void setId(SimpleStringProperty id) {
        this.id = id;
    }

    public String getTen() {
        return this.ten.get();
    }

    public void setTen(String ten) {
        this.ten.set(ten);
    }

    public String getLoai() {
        return this.loai.get();
    }

    public void setLoai(String loai) {
        this.loai.set(loai);
    }



    public  LocalDate getBatdau() {
        return this.batdau.get();
    }

    public void setBatdau(LocalDate batdau) {
        this.batdau.set(batdau);
    }

    public LocalDate getHannop() {
        return this.hannop.get();
    }

    public void setHannop(LocalDate hannop) {
        this.hannop.set(hannop);
    }
 

    public int getGhichu() {
        return this.ghichu.get();
    }

    public void setGhichu(int ghichu) {
        this.ghichu.set(ghichu);
    }

}
