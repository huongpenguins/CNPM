package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import javax.security.auth.callback.Callback;
import javafx.scene.control.TableCell;
import Entities.KhoanThu;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FeeController {
    int pageIndex;
    final int ROW_PER_PAGE = 10;
    ResultSet resultSet;
    // noi luu tru data
    ObservableList<KhoanThu> data = FXCollections.observableArrayList(
        new KhoanThu("KT01", "Học phí", "Giáo dục", LocalDateTime.of(2024, 1, 1, 8, 0).toLocalDate(), LocalDateTime.of(2024, 2, 28, 23, 59).toLocalDate(), "Học kỳ 1"),
        new KhoanThu("KT02", "Tiền điện", "Sinh hoạt", LocalDateTime.of(2024, 12, 1, 0, 0).toLocalDate(), LocalDateTime.of(2024, 12, 31, 23, 59).toLocalDate(), "Tháng 12"),
        new KhoanThu("KT03", "Tiền nước", "Sinh hoạt", LocalDateTime.of(2024, 12, 5, 0, 0).toLocalDate(), LocalDateTime.of(2024, 12, 25, 23, 59).toLocalDate(), "Tháng 12"),
        new KhoanThu("KT04", "Tiền thuê nhà", "Sinh hoạt", LocalDateTime.of(2024, 1, 1, 0, 0).toLocalDate(), LocalDateTime.of(2024, 1, 31, 23, 59).toLocalDate(), "Tháng 1")
        );
      
    
    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu,add;
    @FXML
    TextField search;
    @FXML
    Pagination pagination;
    @FXML
    TableView<KhoanThu> table;
    @FXML 
    TableColumn<KhoanThu,String> id,ten,loai, ghichu;
    @FXML 
    TableColumn<KhoanThu,LocalDate> batdau,hannop;
    @FXML
    TableColumn<KhoanThu,Void> chinhsua,chitiet,xoa;
    
    public void initialize(){
        table.setEditable(false);
        // lien ket cot voi thuoc tinh trong KhoanThu
        id.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("id"));
        ten.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("ten"));
        loai.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("loai"));
        ghichu.setCellValueFactory(new PropertyValueFactory<KhoanThu,String>("ghichu"));
        batdau.setCellValueFactory(new PropertyValueFactory<KhoanThu,LocalDate>("batdau"));
        hannop.setCellValueFactory(new PropertyValueFactory<KhoanThu,LocalDate>("hannop"));
       
        // them Dl vao bang
        table.setItems(data);
        
        // them button vao cot
        chinhsua.setCellFactory(col -> new TableCell<KhoanThu, Void>() {
            private final Button btn = new Button("Sửa");

            {   
                btn.setOnAction(event->{

                table.setEditable(true);
                table.setOnKeyPressed(keyevent -> {
                    if (keyevent.getCode() == KeyCode.ENTER) {
                        table.setEditable(false);
                    }

                });
                //chinh sua ten
                ten.setCellFactory(TextFieldTableCell.forTableColumn());
                ten.setOnEditCommit(
                    new EventHandler<CellEditEvent<KhoanThu, String>>() {
                        @Override
                        public void handle(CellEditEvent<KhoanThu, String> t) {
                            ((KhoanThu) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setTen(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );

                loai.setCellFactory(TextFieldTableCell.forTableColumn());
                loai.setOnEditCommit(
                    new EventHandler<CellEditEvent<KhoanThu, String>>() {
                        @Override
                        public void handle(CellEditEvent<KhoanThu, String> t) {
                            ((KhoanThu) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setLoai(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );

                ghichu.setCellFactory(TextFieldTableCell.forTableColumn());
                ghichu.setOnEditCommit(
                    new EventHandler<CellEditEvent<KhoanThu, String>>() {
                        @Override
                        public void handle(CellEditEvent<KhoanThu, String> t) {
                            ((KhoanThu) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setGhichu(t.getNewValue());
                        }
                    }
                    // luu vao csdl

                );
                
                    // chinh sua batdau
                batdau.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate date){
                        return date.toString();
                    }
                    @Override
                    public LocalDate fromString(String string){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/mm/dd");
                        LocalDate date = LocalDate.parse(string, formatter);
                        if(date==null){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd.");
                        alert.showAndWait();
                        
                        }
                        return date;
                    }
                }));
                batdau.setOnEditCommit(
                    new EventHandler<CellEditEvent<KhoanThu, LocalDate>>() {
                        @Override
                        public void handle(CellEditEvent<KhoanThu, LocalDate> t) {
                            ((KhoanThu) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setBatdau(t.getNewValue());
                        }
                    }
                );

                // chinh sua hannop
                hannop.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate date){
                        return date.toString();
                    }
                    @Override
                    public LocalDate fromString(String string){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/mm/dd");
                        LocalDate date = LocalDate.parse(string, formatter);
                        if(date==null){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd.");
                        alert.showAndWait();
                        
                        }
                        return date;
                    }
                }));
                hannop.setOnEditCommit(
                    new EventHandler<CellEditEvent<KhoanThu, LocalDate>>() {
                        @Override
                        public void handle(CellEditEvent<KhoanThu, LocalDate> t) {
                            ((KhoanThu) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setHannop(t.getNewValue());
                        }
                    }
                    // luu vao csdl o day

                );
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
                    int curIndex = getTableRow().getIndex();
                    table.getItems().remove(curIndex);

                    // Xoa trong csdl


                    
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

        // nut tim kiem
        search.setOnKeyPressed(event -> { 
            if(event.getCode() == KeyCode.ENTER)
                search();
            
         });
         
         

    }
    FilteredList<KhoanThu> filter = new FilteredList<>(data,p->true);
    @FXML
    private void search(){
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<KhoanThu> searchFilter = khoanThu ->{
            if(khoanThu.getTen().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getLoai().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getGhichu().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getBatdau().toString().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(khoanThu.getHannop().toString().toLowerCase().contains(l.toLowerCase()))
            return true;

            return false;
        };
            
        filter.setPredicate(searchFilter);

        table.setItems(filter);
    }
    
    @FXML
    private void add_fee() throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add_fee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 500);
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thêm phí thu");
        subStage.show();
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
        App.setRoot("secondary");
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
        App.setRoot("secondary");
    }
    @FXML 
    private void switchToSignIn(){
        
    }
    @FXML
    private void switchToAccount() throws IOException {
        App.setRoot("account");
    }
    

}
