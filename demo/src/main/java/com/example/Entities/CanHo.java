package com.example.Entities;

import javafx.beans.property.*;

public class CanHo {
    private StringProperty maCanHo;
    private StringProperty maHoGiaDinh;
    private StringProperty tenCanHo;
    private IntegerProperty tang;
    private FloatProperty dienTich;
    private StringProperty moTa;

    // Constructor
    public CanHo(String maCanHo, String maHoGiaDinh, String tenCanHo, int tang, float dienTich, String moTa) {
        this.maCanHo = new SimpleStringProperty(maCanHo);
        this.maHoGiaDinh = new SimpleStringProperty(maHoGiaDinh);
        this.tenCanHo = new SimpleStringProperty(tenCanHo);
        this.tang = new SimpleIntegerProperty(tang);
        this.dienTich = new SimpleFloatProperty(dienTich);
        this.moTa = new SimpleStringProperty(moTa);
    }

    private String ten;  // Thuộc tính tên căn hộ

    // Getter cho thuộc tính ten
    public String getTen() {
        return ten;
    }

    // Getters and Setters using Properties
    public StringProperty maCanHoProperty() {
        return maCanHo;
    }

    public String getMaCanHo() {
        return maCanHo.get();
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo.set(maCanHo);
    }

    public StringProperty maHoGiaDinhProperty() {
        return maHoGiaDinh;
    }

    public String getMaHoGiaDinh() {
        return maHoGiaDinh.get();
    }

    public void setMaHoGiaDinh(String maHoGiaDinh) {
        this.maHoGiaDinh.set(maHoGiaDinh);
    }

    public StringProperty tenCanHoProperty() {
        return tenCanHo;
    }

    public String getTenCanHo() {
        return tenCanHo.get();
    }

    public void setTenCanHo(String tenCanHo) {
        this.tenCanHo.set(tenCanHo);
    }

    public IntegerProperty tangProperty() {
        return tang;
    }

    public int getTang() {
        return tang.get();
    }

    public void setTang(int tang) {
        this.tang.set(tang);
    }

    public FloatProperty dienTichProperty() {
        return dienTich;
    }

    public float getDienTich() {
        return dienTich.get();
    }

    public void setDienTich(float dienTich) {
        this.dienTich.set(dienTich);
    }

    public StringProperty moTaProperty() {
        return moTa;
    }

    public String getMoTa() {
        return moTa.get();
    }

    public void setMoTa(String moTa) {
        this.moTa.set(moTa);
    }
}

