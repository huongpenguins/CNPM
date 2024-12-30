package com.example.Entities;

public class CanHo {
    private String MaCanHo;
    private String MaHoKhau;
    private String TenCanHo;
    private int Tang;
    private float DienTich;
    private String MoTa;

    public CanHo(String maCanHo, String maHoKhau, String tenCanHo, int tang, float dienTich, String moTa) {
        this.MaCanHo = maCanHo;
        this.MaHoKhau = maHoKhau;
        this.TenCanHo = tenCanHo;
        this.Tang = tang;
        this.DienTich = dienTich;
        this.MoTa = moTa;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.MaHoKhau = maHoKhau;
    }

    public void setMaCanHo(String maCanHo) {
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

    public String getMaCanHo() {
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
