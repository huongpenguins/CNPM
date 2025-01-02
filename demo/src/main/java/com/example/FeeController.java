package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import com.example.Entities.KhoanThu;
import com.example.dal.KhoanThuDAL;

import javafx.scene.control.TableCell;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FeeController {

    
    ObservableList<KhoanThu> data = KhoanThuDAL.loadData("0"); // tai khoan thu chua hoan thanh completed=0

    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu,apply,remove;
    @FXML
    TextField search,text_ten,text_loai,text_ghichu,topage,text_donvi;
    @FXML
    TableView<KhoanThu> table;
    @FXML 
    TableColumn<KhoanThu,String> ten;
    @FXML 
    TableColumn<KhoanThu,String> loai,donvi;
    @FXML
    TableColumn<KhoanThu,Integer> ghichu;
    @FXML 
    TableColumn<KhoanThu,LocalDate> batdau,hannop;
    @FXML
    TableColumn<KhoanThu,Void> chinhsua,chitiet,xoa,completed;
    @FXML
    AnchorPane filterbar;
    @FXML
    DatePicker tungay,denngay;
    KhoanThuDAL khoanThuDAL = new KhoanThuDAL();
  
    public void initialize(){
        table.setEditable(false);
        
       
        // lien ket cot voi thuoc tinh trong KhoanThu
        ten.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("ten"));
        loai.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("loai"));
        ghichu.setCellValueFactory(new PropertyValueFactory<KhoanThu,Integer>("ghichu"));
        batdau.setCellValueFactory(new PropertyValueFactory<KhoanThu,LocalDate>("batdau"));
        hannop.setCellValueFactory(new PropertyValueFactory<KhoanThu,LocalDate>("hannop"));
        donvi.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("donvi"));


        table.setItems(data);
        
        // them button chinh sua vao cot
        chinhsua.setCellFactory(col -> new TableCell<KhoanThu, Void>() {
            private final Button btn = new Button("Sửa");

            {   

                btn.setStyle("-fx-background-image: url('picture/edit.png'); "+
                "-fx-background-position: center; "+
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: contain; "  );

   
                btn.setOnAction(event->{
                    KhoanThu k = getTableView().getItems().get(getIndex());
                        try {
                            edit_fee(k);
                            
                            table.refresh();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
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
                }
            }
            
        });
        
        //Them button vao xoa
        xoa.setCellFactory(col -> new TableCell<KhoanThu, Void>() {
            private final Button btn1 = new Button("Xoá");

            {
              btn1.setOnAction(event->{

                    btn1.setStyle("-fx-background-image: url('picture/xoa.png'); "+
                    "-fx-background-position: center; "+
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-size: contain; "  );

   
                    KhoanThu curItem = getTableRow().getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận xoá");
                    alert.setHeaderText("Bạn có chắc muốn xoá mục này?");
                    alert.setContentText("Mục: " + curItem.getTen());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean t = khoanThuDAL.deleteKhoanThu("khoanthutbl","MaKhoanThu" ,curItem.getId());
                        if(t==true) {
                            data.remove(curItem);
                            table.refresh();
                        }
                        
                    }

                    
              });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn1);
                }
            }
        });

        // them button vao chi tiet
        chitiet.setCellFactory(col -> new TableCell<KhoanThu, Void>() {
            private final Button btn2 = new Button("Chi tiết");

            {
                btn2.setStyle("-fx-background-image: url('picture/detail.png'); "+
                "-fx-background-position: center; "+
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: contain; "  );

   
                btn2.setOnAction(event->{
                    try {
                        KhoanThu k = getTableView().getItems().get(getIndex());
                        showChiTiet(k);

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
                    setGraphic(btn2);
                }
            }
        });

        completed.setCellFactory(col -> new TableCell<KhoanThu, Void>() {
            private final Button btn1 = new Button("Hoàn thành");

            {
                btn1.setStyle("-fx-background-image: url('picture/tick.png'); "+
                "-fx-background-position: center; "+
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: contain; "  );

                

              btn1.setOnAction(event->{
                    KhoanThu curItem = getTableRow().getItem();
                    try {
                        boolean t = khoanThuDAL.updateKhoanThu(" khoanthutbl "," Completed "," 1 "," MaKhoanThu ",curItem.getId());
                        if(t==true){
                            data.remove(curItem);
                            table.refresh();
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
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
                    setGraphic(btn1);
                }
            }
        });
       
       
       
        // nut tim kiem
        search.setOnKeyPressed(event -> { 
            if(event.getCode() == KeyCode.ENTER)
                search();

         });


    }
    
    


    
    @FXML
    private void search(){
        FilteredList<KhoanThu> filter = new FilteredList<>(data,p->true);
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<KhoanThu> searchFilter = khoanThu ->{
            if(khoanThu.getId().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getTen().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getLoai().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getBatdau().toString().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getHannop().toString().toLowerCase().contains(l.toLowerCase()))
            return true;
            try {
                if(khoanThu.getGhichu()==Integer.parseInt(l))
            return true;
            } catch (Exception e) {
                return false;
            }
            if(khoanThu.getDonvi().toLowerCase().contains(l.toLowerCase()))
            return true;

            return false;
        };
            
        filter.setPredicate(searchFilter);

        table.setItems(filter);
        table.refresh();
    }
    
    @FXML
    private void add_fee() throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add_fee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 600);
        AddFeeController addFeeController = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thêm phí thu");
        subStage.show();
        subStage.setOnHiding(event->{
            if(!addFeeController.listNew.isEmpty()){
                for(KhoanThu i : addFeeController.listNew){
                    data.add(i);
                    table.refresh();
                }
            }
        });
        
    }
        
    private void edit_fee(KhoanThu kt) throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit_fee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 600);
        EditFeeController edit = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Sửa phí thu");
        edit.batdau.setValue(kt.getBatdau());
        edit.donvi.setValue(kt.getDonvi());
        edit.ghichu.setText(kt.ghichu.getValue().toString());
        edit.hannop.setValue(kt.getHannop());
        edit.id_text.setText(kt.getId());
        edit.ten_text.setText(kt.getTen());
        edit.loai.setValue(kt.getLoai());
        edit.k = kt;
        subStage.show();
        subStage.setOnHiding(event->{
        
            data = KhoanThuDAL.loadData("0");
            table.setItems(data);
            
        });
       
    }
    private void showChiTiet(KhoanThu k) throws IOException{
        if(!k.getLoai().toString().equals("Quyên góp")){
            
            Stage subStage = new Stage();
            FXMLLoader fxmlLoader;
            if(k.getDonvi().equals("Số điện/nước")){
             fxmlLoader = new FXMLLoader(App.class.getResource("CTDienNuoc.fxml"));
             Scene scene = new Scene(fxmlLoader.load(), 1000, 600); 
             DienNuocController diennuoc = fxmlLoader.getController();
             diennuoc.k = k;
             diennuoc.maKT = k.getId();
             diennuoc.tenKhoanThu = k.getTen();
             diennuoc.tungay = k.getBatdau();
             diennuoc.denngay = k.getHannop();
             diennuoc.loadData();
             subStage.setResizable(false);
             subStage.setScene(scene);
             subStage.setTitle(k.getTen());
             subStage.show();
        }
            else{
            fxmlLoader = new FXMLLoader(App.class.getResource("CTKhoanThu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600); 
            CTKhoanThuController ctKThuController = fxmlLoader.getController();
            ctKThuController.k = k;
            ctKThuController.maKT = k.getId();
            ctKThuController.tenKhoanThu = k.getTen();
            ctKThuController.tungay = k.getBatdau();
            ctKThuController.denngay = k.getHannop();
            ctKThuController.loadData();
            subStage.setResizable(false);
            subStage.setScene(scene);
            subStage.setTitle(k.getTen());
            subStage.show();
            }
            
            
            
        }
        // quyep gop thi man hinh chi tiet khoan thu se khac vi ko phai bat buoc
        else{
            
            Stage subStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("quyengop.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600); 
            CTQuyengopController ctQGopController = fxmlLoader.getController();
            ctQGopController.maKT = k.getId();
            ctQGopController.tenKhoanThu = k.getTen();
            ctQGopController.tungay = k.getBatdau();
            ctQGopController.denngay = k.getHannop();
            
            subStage.setResizable(false);
            subStage.setScene(scene);
            subStage.setTitle(k.getTen());
            subStage.show();
        }
        
    }

    @FXML 
    private void applyFilter(){
        FilteredList<KhoanThu> filter = new FilteredList<>(data,p->true);
        Predicate<KhoanThu> pFilter = khoanThu ->{
            boolean chk_ten = text_ten.getText().isEmpty()||khoanThu.getTen().toLowerCase().contains(text_ten.getText().toLowerCase());
            boolean chk_loai=text_loai.getText().isEmpty()||khoanThu.getLoai().toLowerCase().contains(text_loai.getText().toLowerCase());
            boolean chk_ghichu = text_ghichu.getText().isEmpty()||khoanThu.getGhichu()==Integer.parseInt(text_ghichu.getText());
            boolean chk_tungay = tungay.getValue()==null||(tungay.getValue().isBefore(khoanThu.getBatdau()));
            boolean chk_dennhay = denngay.getValue()==null||(denngay.getValue().isAfter(khoanThu.getHannop()));
            boolean chk_donvi = text_donvi.getText().isEmpty()|| (khoanThu.getDonvi().toLowerCase().contains(text_donvi.getText().toLowerCase()));
            return chk_ten&&chk_loai&&chk_ghichu&&chk_dennhay&&chk_tungay&&chk_donvi;
        };

        filter.setPredicate(pFilter);
        table.setItems(filter);
        table.refresh();
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
        FilteredList<KhoanThu> filter = new FilteredList<>(data,p->true);
        text_ten.clear();
        text_ghichu.clear();
        text_loai.clear();
        tungay.setValue(null);
        denngay.setValue(null);
        Predicate<KhoanThu> remove = khoanThu ->true;
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
    @FXML
    private void menuClick(){
        if(sidebar.getLayoutX()<0){
            sidebar.setLayoutX(0);
        }
        else sidebar.setLayoutX(-sidebar.getWidth());
    }
    
    @FXML 
    private void signout(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn muốn đăng xuất");
        alert.showAndWait();
        if(alert.getResult()==ButtonType.OK){
            switchToSignIn();
        }


    }
    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }
    @FXML
    private void switchToGiaDinh() throws IOException {
        App.setRoot("FamiliesManager");
    }
    @FXML
    private void switchToDanCu() throws IOException {
        App.setRoot("ResidentsManager");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("fee");
    }
    @FXML
    private void switchToCanHo() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToTamTru() throws IOException {
        App.setRoot("secondary");
    }
    @FXML
    private void switchToTamVang() throws IOException {
        App.setRoot("tamvang");
    }
    @FXML 
    private void switchToSignIn(){
        
    }
    @FXML
    private void switchToAccount() throws IOException {
        App.setRoot("account");
    }
    

}
