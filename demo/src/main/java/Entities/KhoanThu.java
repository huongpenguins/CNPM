package Entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class KhoanThu {
    SimpleStringProperty id;
    SimpleStringProperty ten;
    SimpleStringProperty loai;
    SimpleObjectProperty<LocalDate> batdau;
    SimpleObjectProperty<LocalDate> hannop;
    SimpleStringProperty ghichu;

    public KhoanThu(String id, String ten,String loai, 
    LocalDate batdau, LocalDate hannop,String ghichu) {
        this.id = new SimpleStringProperty(id);
        this.ten = new SimpleStringProperty(ten);
        this.loai = new SimpleStringProperty(loai);
        this.ghichu = new SimpleStringProperty(ghichu);
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
        this.ten = new SimpleStringProperty(ten);
    }

    public String getLoai() {
        return this.loai.get();
    }

    public void setLoai(String loai) {
        this.loai = new SimpleStringProperty(loai);
    }



    public  LocalDate getBatdau() {
        return this.batdau.get();
    }

    public void setBatdau(LocalDate batdau) {
        this.batdau = new SimpleObjectProperty<>(batdau);
    }

    public LocalDate getHannop() {
        return this.hannop.get();
    }

    public void setHannop(LocalDate hannop) {
        this.hannop = new SimpleObjectProperty<>(hannop);
    }
 

    public String getGhichu() {
        return this.ghichu.get();
    }

    public void setGhichu(String ghichu) {
        this.ghichu = new SimpleStringProperty(ghichu);
    }

}
