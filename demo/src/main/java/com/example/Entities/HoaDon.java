package com.example.Entities;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HoaDon {
    LocalDate thoidiem;
    
    private SimpleStringProperty maho;
    private SimpleStringProperty maKT;
    private SimpleIntegerProperty sotiennop;
    
    public HoaDon(String maho,String maKT,int sotiennop,LocalDate thoidiem){
        this.maho = new SimpleStringProperty(maho);
        this.maKT = new SimpleStringProperty(maKT);
        this.sotiennop = new SimpleIntegerProperty(sotiennop);
        this.thoidiem = thoidiem;
    }

    public LocalDate getThoidiem() {
        return this.thoidiem;
    }

    public void setThoidiem(LocalDate thoidiem) {
        this.thoidiem = thoidiem;
    }

    public String getMaho() {
        return this.maho.getValue();
    }

    public void setMaho(String maho) {
        this.maho.set(maho);
    }

    public String getMaKT() {
        return this.maKT.getValue();
    }

    public void setMaKT(String maKT) {
        this.maKT.set(maKT);
    }

    public int getSotiennop() {
        return this.sotiennop.getValue();
    }

    public void setSotiennop(int sotiennop) {
        this.sotiennop.set(sotiennop);
    }

}
