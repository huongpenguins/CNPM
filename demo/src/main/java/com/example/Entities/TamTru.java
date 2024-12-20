package com.example.Entities;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TamTru {
    SimpleStringProperty maNhanKhau;
    SimpleStringProperty cccd;           // Khai báo biến
    SimpleStringProperty ten;
    SimpleStringProperty dcthuongtru;
    SimpleStringProperty dctamtru;
    SimpleObjectProperty<LocalDate> ngaybdtamtru;

    public TamTru(String ten, String cccd, String dcthuongtru,
                  String dctamtru, LocalDate ngaybdtamtru) {
        this.cccd = new SimpleStringProperty(cccd);
        this.ten = new SimpleStringProperty(ten);
        this.dcthuongtru = new SimpleStringProperty(dcthuongtru);
        this.ngaybdtamtru = new SimpleObjectProperty<>(ngaybdtamtru);
        this.dctamtru = new SimpleStringProperty(dctamtru);
    }

    public TamTru(String maNhanKhau, String dcthuongtru, String dctamtru, LocalDate ngaybdtamtru) {
        this.maNhanKhau.set(maNhanKhau);
        this.dctamtru.set(dctamtru);
        this.ngaybdtamtru.set(ngaybdtamtru);
    }

    public String getMaNhanKhau() {
        return this.maNhanKhau.get();
    }

    public void setMaNhanKhau(String maNhanKhau) {
        this.maNhanKhau.set(maNhanKhau);
    }
    public String getCCCD() {
        return this.cccd.get();
    }

    public void setCCCD(String cccd) {
        this.cccd.set(cccd);
    }

    public String getTen() {
        return this.ten.get();
    }

    public void setTen(String ten) {
        this.ten.set(ten);
    }

    public String getDcThuongTru() {return this.dcthuongtru.get();}

    public void setDcThuongTru(String dcthuongtru) {this.dcthuongtru.set(dcthuongtru);}

    public  LocalDate getNgaybdtamtru() {
        return this.ngaybdtamtru.get();
    }

    public void setNgaybdtamtru(LocalDate ngaybdtamtru) {
        this.ngaybdtamtru.set(ngaybdtamtru);
    }

    public String getDcTamTru() {return this.dctamtru.get();}

    public void setDcTamTru(String dctamtru) {this.dctamtru.set(dctamtru);}
}
