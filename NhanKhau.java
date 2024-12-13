package com.example.Entities;

public class NhanKhau {
    private String MaNhanKhau;
    private String MaHoGiaDinh;
    private String HoTen;
    private int CCCD;
    private String NgaySinh;
    private String NoiSinh;
    private String DanToc;
    private String NgheNghiep;

    public void setMaNhanKhau(String maNhanKhau) {this.MaNhanKhau = maNhanKhau;}
    public void setMaHoGiaDinh(String maHoGiaDinh) {this.MaHoGiaDinh = maHoGiaDinh;}
    public void setHoTen(String hoten) {this.HoTen = hoten;}
    public void setCCCD(int cccd) {this.CCCD = cccd;}
    public void setNgaySinh(String ngaysinh) {this.NgaySinh = ngaysinh;}
    public void setNoiSinh(String noisinh) {this.NoiSinh = noisinh;}
    public void setDanToc(String dantoc) {this.DanToc = dantoc;}
    public void setNgheNghiep(String nghenghiep) {this.NgheNghiep = nghenghiep;}

    public String getMaNhanKhau(){ return MaNhanKhau; }
    public String getMaHoGiaDinh(){ return MaHoGiaDinh; }
    public String getHoTen(){ return HoTen; }
    public int getCCCD(){ return CCCD; }
    public String getNgaySinh(){ return NgaySinh; }
    public String getNoiSinh(){ return NoiSinh; }
    public String getDanToc(){ return DanToc; }
    public String getNgheNghiep(){ return NgheNghiep;}

    public NhanKhau(String maNhanKhau, String maHoGiaDinh, String hoten, int cccd, String ngaysinh, String noisinh, String dantoc, String nghenghiep) {
        this.MaNhanKhau =maNhanKhau;
        this.MaHoGiaDinh = maHoGiaDinh;
        this.HoTen = hoten;
        this.CCCD = cccd;
        this.NgaySinh = ngaysinh;
        this.NoiSinh = noisinh;
        this.DanToc = dantoc;
        this.NgheNghiep = nghenghiep;
    }

    public NhanKhau(String maHoGiaDinh,String maNhanKhau){
        this.MaHoGiaDinh=maHoGiaDinh;
        this.MaNhanKhau=maNhanKhau;
    }

}
