package com.example.Entities;

public class SoDienNuoc {
    String MaKhoanThu,maNhanKhau;
    int soDien;

    public SoDienNuoc(String MaKhoanThu, String maNhanKhau, int soDien) {
        this.MaKhoanThu = MaKhoanThu;
        this.maNhanKhau = maNhanKhau;
        this.soDien = soDien;
    }

    public String getMaKhoanThu() {
        return this.MaKhoanThu;
    }

    public void setMaKhoanThu(String MaKhoanThu) {
        this.MaKhoanThu = MaKhoanThu;
    }

    public String getMaNhanKhau() {
        return this.maNhanKhau;
    }

    public void setMaNhanKhau(String maNhanKhau) {
        this.maNhanKhau = maNhanKhau;
    }

    public int getSoDien() {
        return this.soDien;
    }

    public void setSoDien(int soDien) {
        this.soDien = soDien;
    }

}
