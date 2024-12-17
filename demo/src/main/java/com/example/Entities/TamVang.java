package com.example.Entities;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TamVang {
    SimpleStringProperty maNhanKhau;
    SimpleStringProperty cccd,phong;
    SimpleStringProperty ten;
    SimpleStringProperty lydo;
    SimpleObjectProperty<LocalDate> ngayvang;


    public TamVang(String ten, String cccd,String phong,
     String lydo,LocalDate ngayvang) {
        this.cccd = new SimpleStringProperty(cccd);
        this.ten = new SimpleStringProperty(ten);
        this.phong = new SimpleStringProperty(phong);
        this.ngayvang = new SimpleObjectProperty<>(ngayvang);
        this.lydo = new SimpleStringProperty(lydo);
        
    }
    public TamVang(String maNhanKhau,
     String lydo,LocalDate ngayvang) {
        this.maNhanKhau.set(maNhanKhau);
        this.ngayvang.set(ngayvang);
        this.lydo.set(lydo);
        
    }
    public String getMaNhanKhau() {
        return this.maNhanKhau.get();
    }

    public void setMaNhanKhau(String maNhanKhau) {
        this.maNhanKhau.set(maNhanKhau);
    }
    public String getCccd() {
        return this.cccd.get();
    }

    public void setCccd(String cccd) {
        this.cccd.set(cccd);
    }

    public String getTen() {
        return this.ten.get();
    }

    public void setTen(String ten) {
        this.ten.set(ten);
    }

    public String getPhong() {
        return this.phong.get();
    }

    public void setPhong(String phong) {
        this.phong.set(phong);
    }



    public  LocalDate getNgayvang() {
        return this.ngayvang.get();
    }

    public void setNgayvang(LocalDate ngayvang) {
        this.ngayvang.set(ngayvang);
    }

    public String getLydo() {
        return this.lydo.get();
    }

    public void setLydo(String lydo) {
        this.lydo.set(lydo);
    }

}


