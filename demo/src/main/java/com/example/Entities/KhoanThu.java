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
    public SimpleIntegerProperty ghichu;//don gia
    SimpleStringProperty donvi;
    boolean completed;
    public  SimpleIntegerProperty I_Tong_tien;

    public KhoanThu(String id, String ten,String loai, 
    LocalDate batdau, LocalDate hannop,int ghichu,String donvi) {
        this.id = new SimpleStringProperty(id);
        this.ten = new SimpleStringProperty(ten);
        this.loai = new SimpleStringProperty(loai);
        this.ghichu = new SimpleIntegerProperty(ghichu);
        this.batdau = new SimpleObjectProperty<>(batdau);
        this.hannop = new SimpleObjectProperty<>(hannop);
        this.donvi = new SimpleStringProperty(donvi);
        completed=false;
    }

    
    public boolean isCompleted() {
        return this.completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getId() {
        return this.id.get();
    }

    public void setId(String id) {
        this.id.set(id);
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

    public String getDonvi() {
        return this.donvi.get();
    }

    public void setDonvi(String donvi) {
        this.loai.set(donvi);
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
