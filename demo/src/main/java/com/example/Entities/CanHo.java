package com.example.Entities;

public class CanHo {
    private int MaCanHo;
    private String MaHoKhau;
    private String TenCanHo;
    private int Tang;
    private float DienTich;
    private String MoTa;

    public void setMaHoKhau(String maHoKhau) {
        this.MaHoKhau = maHoKhau;
    }

    public void setMaCanHo(int maCanHo) {
        this.MaCanHo = maCanHo;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public void setTenCanHo(String tenCanHo) {
        this.TenCanHo = tenCanHo;
    }

    public void setTang(int tang) {
        this.Tang = tang;
    }

    public void setDienTich(float dienTich) {
        this.DienTich = dienTich;
    }

    public String getMaHoKhau() {
        return MaHoKhau;
    }

    public float getDienTich() {
        return DienTich;
    }

    public int getMaCanHo() {
        return MaCanHo;
    }

    public String getMoTa() {
        return MoTa;
    }

    public String getTenCanHo() {
        return TenCanHo;
    }

    public int getTang() {
        return Tang;
    }

    public static void main(String[] args) {

    }
}
