package com.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Predicate;
import org.controlsfx.control.RangeSlider;

import com.example.Entities.CTQuyenGop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CTQuyengopController {
    String maKT;
    String tenKhoanThu;
    LocalDate tungay,remove,denngay;
    @FXML
    Button apply,thongke;
    @FXML
    AnchorPane filterbar;
    @FXML
    TextField id_text,ten_text,search;
    @FXML
    DatePicker ngaynop_date;
    @FXML
    RangeSlider tiennop_filter;
    @FXML
    TableView<CTQuyenGop> table;
    @FXML
    TableColumn<CTQuyenGop,String> tenKT;
    @FXML 
    TableColumn<CTQuyenGop,String> id,ten;
    @FXML
    TableColumn<CTQuyenGop,Integer> danop;
    @FXML 
    TableColumn<CTQuyenGop,LocalDate> ngaynop;

     ObservableList<CTQuyenGop> data = FXCollections.observableArrayList(
        new CTQuyenGop("Học phí", "KT01", "Khoản học phí kỳ 1", LocalDate.of(2024, 1, 15), 500000),
        new CTQuyenGop("ĐiệnĐiện", "KT02", "Thanh toán tiền điện tháng 1", LocalDate.of(2024, 2, 1), 300000),
        new CTQuyenGop("NướcNước", "KT03", "Thanh toán tiền nước tháng 1", LocalDate.of(2024, 2, 5), 200000),
         new CTQuyenGop("Vệ sinh", "KT04", "Phí vệ sinh chung cư", LocalDate.of(2024, 3, 10), 100000),
        new CTQuyenGop("Mạng", "KT05", "Cước Internet tháng 1", LocalDate.of(2024, 1, 25), 250000)
       );
      
    
    public void initialize(){
        
        tiennop_filter.setHighValue(tiennop_filter.getMax());
        tiennop_filter.setLowValue(tiennop_filter.getMin());
        id.setCellValueFactory(new PropertyValueFactory<CTQuyenGop,String>("id"));
        ten.setCellValueFactory(new PropertyValueFactory<CTQuyenGop,String>("ten"));
        tenKT.setCellValueFactory(new PropertyValueFactory<CTQuyenGop,String>("tenKT"));
        ngaynop.setCellValueFactory(new PropertyValueFactory<CTQuyenGop,LocalDate>("ngaynop"));
        danop.setCellValueFactory(new PropertyValueFactory<CTQuyenGop,Integer>("danop"));
        //them DL vao bang
        table.setItems(data);

        //tim kiem
        search.setOnKeyPressed(event -> { 
            if(event.getCode() == KeyCode.ENTER)
                search();
            
         });

    }
    
    public void setData(){
        
    }
    @FXML
    private void ThongKe(){

    }
    
    private void Add() throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("thanhtoan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 445);
        ThanhToanController t = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thanh toán");
        
        subStage.show();
        subStage.setOnHiding(event->{
            if(t.hoaDon==null) return;
            else{
                for(CTQuyenGop i : data){
                    
                    if(i.getId().equals(t.hoaDon.getMaho())){
                        i.setDanop(i.getDanop()+t.hoaDon.getSotiennop());
                        i.setNgaynop(t.hoaDon.getThoidiem());
                        table.refresh();
                        return;
                        
                    }
                }
                // lay ttin ten tu ma ho


                data.add(new CTQuyenGop(tenKhoanThu, t.hoaDon.getMaho(), " ", t.hoaDon.getThoidiem(), t.hoaDon.getSotiennop()));
               

            }
        });
    }
    FilteredList<CTQuyenGop> filter = new FilteredList<>(data,p->true);
    @FXML
    private void search(){
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<CTQuyenGop> searchFilter = CTQuyenGop ->{
            if(CTQuyenGop.getId().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(CTQuyenGop.getTen().toLowerCase().contains(l.toLowerCase()))
            return true;
           
            if(CTQuyenGop.getNgaynop().toString().toLowerCase().contains(l.toLowerCase()))
            return true;
            try {
            if(CTQuyenGop.getDanop()==Integer.parseInt(l))
            return true;
            } catch (Exception e) {
                return false;
            }
                                              
            return false;
        };
            
        filter.setPredicate(searchFilter);

        table.setItems(filter);
    }
    @FXML
        private void applyFilter(){
        Predicate<CTQuyenGop> pFilter = CTQuyenGop ->{
            boolean chk_id = id_text.getText().isEmpty()||CTQuyenGop.getId().toLowerCase().contains(id_text.getText().toLowerCase());
            boolean chk_ten=ten_text.getText().isEmpty()||CTQuyenGop.getTen().toLowerCase().contains(ten_text.getText().toLowerCase());
            boolean chk_tiennop =(CTQuyenGop.getDanop()>=tiennop_filter.getLowValue()&&CTQuyenGop.getDanop()<=tiennop_filter.getHighValue());
            boolean chk_ngaynop = ngaynop_date.getValue()==null||(ngaynop_date.getValue()!=null&&ngaynop_date.getValue().isEqual(CTQuyenGop.getNgaynop()));
  
            return chk_ten&&chk_id&&chk_ngaynop&&chk_tiennop;
        };

        filter.setPredicate(pFilter);
        table.setItems(filter);
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
        id_text.clear();
        ten_text.clear();
        ngaynop_date.setValue(null);
        tiennop_filter.setHighValue(tiennop_filter.getMax());
        tiennop_filter.setLowValue(tiennop_filter.getMin());
        Predicate<CTQuyenGop> remove = khoanThu ->true;
        filter.setPredicate(remove);
        table.setItems(filter);
        filterbar.setLayoutY(-131);
    }
    @FXML 
    private void showFilterBar(){
        if(filterbar.getLayoutY()<0){
            filterbar.setLayoutY(160);
        }
        else if(filterbar.getLayoutY()>=0){
            filterbar.setLayoutY(-131);
        }
    }
    
}



