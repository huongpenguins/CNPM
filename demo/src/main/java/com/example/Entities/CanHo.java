package com.example.Entities;

import javafx.beans.property.*;

public class CanHo {
    private StringProperty maCanHo;
    private StringProperty tenCanHo;
    private IntegerProperty tang;
    private FloatProperty dienTich;
    private StringProperty moTa;

    // Chỉ để hiển thị, lấy từ hogiadinhtbl (nếu có):
    private StringProperty maHoGiaDinh;

    // Constructor 5 tham số (chuẩn cho canhotbl)
    public CanHo(String maCanHo, String tenCanHo, int tang, float dienTich, String moTa) {
        this.maCanHo = new SimpleStringProperty(maCanHo);
        this.tenCanHo = new SimpleStringProperty(tenCanHo);
        this.tang = new SimpleIntegerProperty(tang);
        this.dienTich = new SimpleFloatProperty(dienTich);
        this.moTa = new SimpleStringProperty(moTa);
        this.maHoGiaDinh = new SimpleStringProperty("");
    }

    // Constructor 6 tham số (để hiển thị thêm MaHoGiaDinh từ JOIN)
    public CanHo(String maCanHo, String tenCanHo, int tang, float dienTich, String moTa, String maHoGiaDinh) {
        this(maCanHo, tenCanHo, tang, dienTich, moTa);
        if (maHoGiaDinh == null) {
            maHoGiaDinh = "";
        }
        this.maHoGiaDinh.set(maHoGiaDinh);
    }

    public String getMaCanHo()     { return maCanHo.get(); }
    public String getTenCanHo()    { return tenCanHo.get(); }
    public int    getTang()        { return tang.get(); }
    public float  getDienTich()    { return dienTich.get(); }
    public String getMoTa()        { return moTa.get(); }
    public String getMaHoGiaDinh() { return maHoGiaDinh.get(); }

    public void setMaCanHo(String value)     { this.maCanHo.set(value); }
    public void setTenCanHo(String value)    { this.tenCanHo.set(value); }
    public void setTang(int value)           { this.tang.set(value); }
    public void setDienTich(float value)     { this.dienTich.set(value); }
    public void setMoTa(String value)        { this.moTa.set(value); }
    public void setMaHoGiaDinh(String value) { this.maHoGiaDinh.set(value); }
}
