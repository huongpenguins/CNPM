package com.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Predicate;
import org.controlsfx.control.RangeSlider;

import com.example.Entities.CTKhoanThu;
import com.example.Entities.KhoanThu;
import com.example.dal.ChiTietKhoanThuDAL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CTKhoanThuController {
    KhoanThu k;
    String maKT,tenKhoanThu;
    LocalDate tungay,denngay;
    @FXML
    Button apply,thongke,addfile;
    @FXML
    AnchorPane filterbar;
    @FXML
    TextField id_text,ten_text,search;
    @FXML
    DatePicker ngaynop_date;
    @FXML
    RangeSlider tiennop_filter,tienthieu_filter;
    @FXML
    TableView<CTKhoanThu> table;
    @FXML
    TableColumn<CTKhoanThu,String> tenKT;
    @FXML 
    TableColumn<CTKhoanThu,String> id,ten,trangthai,phong;
    @FXML
    TableColumn<CTKhoanThu,Integer> tiennop,danop,conthieu;
    @FXML 
    TableColumn<CTKhoanThu,LocalDate> ngaynop;
    @FXML
    TableColumn<CTKhoanThu,Void> thanhtoan;
    
    ObservableList<CTKhoanThu> data=FXCollections.observableArrayList();
    
    public void initialize(){
        
        tiennop_filter.setHighValue(tiennop_filter.getMax());
        tiennop_filter.setLowValue(tiennop_filter.getMin());
        tienthieu_filter.setHighValue(tienthieu_filter.getMax());
        tienthieu_filter.setLowValue(tienthieu_filter.getMin());

        id.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,String>("id"));
        phong.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,String>("phong"));
        ten.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,String>("ten"));
        tenKT.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,String>("tenKT"));
        tiennop.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,Integer>("tiennop"));
        ngaynop.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,LocalDate>("ngaynop"));
        danop.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,Integer>("danop"));
        trangthai.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,String>("trangthai"));
        conthieu.setCellValueFactory(new PropertyValueFactory<CTKhoanThu,Integer>("conthieu"));

        //them DL vao bang
        table.setItems(data);

        
        //them button vao thanh toan
         thanhtoan.setCellFactory(col -> new TableCell<CTKhoanThu, Void>() {
            private final Button btn = new Button("Thanh toán");

            {   


                btn.setOnAction(event->{
                    try {
                        ThanhToan(getTableView().getItems().get(getIndex()));
                    } catch (IOException e) {
                        
                        e.printStackTrace();
                    }
                });
            
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    
                    try {
                        if(getTableRow().getItem().getDanop()
                        ==getTableRow().getItem().getTiennop()){
                            btn.setDisable(true);
                        }
                    } catch (Exception e) {
                        System.out.println("chua chon");
                    }
                }
            }
        });
        //tim kiem
        search.setOnKeyPressed(event -> { 
            if(event.getCode() == KeyCode.ENTER)
                search();
            
         });

    }
    



    public void loadData(){
        
        data = ChiTietKhoanThuDAL.loadData1(maKT, "0", "Bắt buộc");
        table.setItems(data);
    }
      @FXML
    private void Addfile(){
         FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

                File file = fileChooser.showOpenDialog(addfile.getScene().getWindow());

                if (file != null) {
                    
                    System.out.println("File đã chọn: " + file.getAbsolutePath());

                    
                    }
                
    }
    
    private void ThanhToan(CTKhoanThu ct) throws IOException{
         Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("thanhtoan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 445);
        ThanhToanController thanhToanController = fxmlLoader.getController();
        thanhToanController.maHo = ct.getId();
        thanhToanController.soTien = ct.getTiennop(); // so tien phai nop
        thanhToanController.tenKhoanThu = ct.getTenKT();
        thanhToanController.maKhoanThu = maKT;
        //thanhToanController.maHo = ct.getId();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thanh toán");
        
        subStage.show();
        subStage.setOnHiding(event->{
            
            if(thanhToanController.hoaDon==null) return;
            else{
                data = ChiTietKhoanThuDAL.loadData1(maKT, "0", "Bắt buộc");
                table.setItems(data);
               

            }
        });
    }
    
    @FXML
    private void search(){
        FilteredList<CTKhoanThu> filter = new FilteredList<>(data,p->true);
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<CTKhoanThu> searchFilter = ctkhoanThu ->{
            if(ctkhoanThu.getId().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(ctkhoanThu.getTen().toLowerCase().contains(l.toLowerCase()))
            return true;
           
            if(ctkhoanThu.getNgaynop().toString().toLowerCase().contains(l.toLowerCase()))
            return true;
            try {
            if(ctkhoanThu.getTiennop()==Integer.parseInt(l))
            return true;
            if(ctkhoanThu.getDanop()==Integer.parseInt(l))
            return true;  
            } catch (Exception e) {
                return false;
            }
                                              
            return false;
        };
            
        filter.setPredicate(searchFilter);

        table.setItems(filter);
        table.refresh();
    }
    @FXML
        private void applyFilter(){
            FilteredList<CTKhoanThu> filter = new FilteredList<>(data,p->true);
        Predicate<CTKhoanThu> pFilter = ctkhoanThu ->{
            boolean chk_id = id_text.getText().isEmpty()||ctkhoanThu.getId().toLowerCase().contains(id_text.getText().toLowerCase());
            boolean chk_ten=ten_text.getText().isEmpty()||ctkhoanThu.getTen().toLowerCase().contains(ten_text.getText().toLowerCase());
            boolean chk_tiennop =(ctkhoanThu.getTiennop()>=tiennop_filter.getLowValue()&&ctkhoanThu.getTiennop()<=tiennop_filter.getHighValue());
            boolean chk_ngaynop = ngaynop_date.getValue()==null||(ngaynop_date.getValue()!=null&&ngaynop_date.getValue().isEqual(ctkhoanThu.getNgaynop()));
            boolean chk_tienthieu = (ctkhoanThu.getTiennop()-ctkhoanThu.getDanop()>=tiennop_filter.getLowValue()&&ctkhoanThu.getTiennop()-ctkhoanThu.getDanop()<=tiennop_filter.getHighValue());
            
            return chk_ten&&chk_id&&chk_ngaynop&&chk_tiennop&&chk_tienthieu;
        };

        filter.setPredicate(pFilter);
        table.setItems(filter);
        table.refresh();
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
        FilteredList<CTKhoanThu> filter = new FilteredList<>(data,p->true);
        id_text.clear();
        ten_text.clear();
        ngaynop_date.setValue(null);
        tiennop_filter.setHighValue(tiennop_filter.getMax());
        tiennop_filter.setLowValue(tiennop_filter.getMin());
        tienthieu_filter.setHighValue(tienthieu_filter.getMax());
        tienthieu_filter.setLowValue(tienthieu_filter.getMin());
        Predicate<CTKhoanThu> remove = khoanThu ->true;
        filter.setPredicate(remove);
        table.setItems(filter);
        table.refresh();
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
