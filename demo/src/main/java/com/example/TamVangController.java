package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import com.example.Entities.TamVang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TamVangController {
    
    ResultSet resultSet;
    // noi luu tru data
    ObservableList<TamVang> data = FXCollections.observableArrayList(
        new TamVang( "Nguyen Van A", "123456789", "Phòng 101", "Về quê", LocalDate.of(2024, 1, 1)),
        new TamVang( "Tran Thi B", "987654321", "Phòng 102", "Công tác", LocalDate.of(2024, 12, 1)),
        new TamVang( "Le Van C", "456123789", "Phòng 103", "Khám bệnh", LocalDate.of(2024, 12, 5)),
        new TamVang( "Pham Thi D", "321654987", "Phòng 104", "Du lịch", LocalDate.of(2024, 1, 15))
        );
    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,TamVang,canho,tamtru,tamvang,trangchu,apply,remove;
    @FXML
    TextField search,text_ten,text_phong,text_cccd,text_lydo;
    
    @FXML
    TableView<TamVang> table;
    @FXML 
    TableColumn<TamVang,String> ten,phong,lydo,cccd;
    @FXML 
    TableColumn<TamVang,LocalDate> ngayvang;
    @FXML
    TableColumn<TamVang,Void> chinhsua,chitiet,xoa;
    @FXML
    AnchorPane filterbar;
    @FXML
    DatePicker ngayvang_date;

    
    public void initialize(){
        table.setEditable(false);
        
       
        // lien ket cot voi thuoc tinh trong TamVang
        phong.setCellValueFactory(new PropertyValueFactory<TamVang,String>("phong"));
        ten.setCellValueFactory(new PropertyValueFactory<TamVang,String>("ten"));
        lydo.setCellValueFactory(new PropertyValueFactory<TamVang,String>("lydo"));
        phong.setCellValueFactory(new PropertyValueFactory<TamVang,String>("phong"));
        ngayvang.setCellValueFactory(new PropertyValueFactory<TamVang,LocalDate>("ngayvang"));


        table.setItems(data);
        
        // them button chinh sua vao cot
        chinhsua.setCellFactory(col -> new TableCell<TamVang, Void>() {
            private final Button btn = new Button("Sửa");

            {   
                
               btn.setOnAction(event->{

               table.setEditable(true);
                
                //chinh sua ten
                ten.setCellFactory(TextFieldTableCell.forTableColumn());
                ten.setOnEditCommit(
                    new EventHandler<CellEditEvent<TamVang, String>>() {
                        @Override
                        public void handle(CellEditEvent<TamVang, String> t) {
                            ((TamVang) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setTen(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );

                cccd.setCellFactory(TextFieldTableCell.forTableColumn());
                cccd.setOnEditCommit(
                    new EventHandler<CellEditEvent<TamVang, String>>() {
                        @Override
                        public void handle(CellEditEvent<TamVang, String> t) {
                            ((TamVang) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setCccd(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );

                phong.setCellFactory(TextFieldTableCell.forTableColumn());
                phong.setOnEditCommit(
                    new EventHandler<CellEditEvent<TamVang, String>>() {
                        @Override
                        public void handle(CellEditEvent<TamVang, String> t) {
                            ((TamVang) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPhong(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );
                
                lydo.setCellFactory(TextFieldTableCell.forTableColumn());
                lydo.setOnEditCommit(
                    new EventHandler<CellEditEvent<TamVang, String>>() {
                        @Override
                        public void handle(CellEditEvent<TamVang, String> t) {
                            ((TamVang) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setLydo(t.getNewValue());
                        }
                    }
                    // them vao csdl

                );
                    // chinh sua ngayvang
                ngayvang.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate date){
                        return date.toString();
                    }
                    @Override
                    public LocalDate fromString(String string){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse(string, formatter);
                        if(date==null){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd.");
                        alert.showAndWait();
                        
                        }
                        return date;
                    }
                }));
                ngayvang.setOnEditCommit(
                    new EventHandler<CellEditEvent<TamVang, LocalDate>>() {
                        @Override
                        public void handle(CellEditEvent<TamVang, LocalDate> t) {
                            ((TamVang) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setNgayvang(t.getNewValue());
                        }
                    }
                );

                
               
                });
                
                table.setOnKeyPressed(keyevent -> {
                    if (keyevent.getCode() == KeyCode.ENTER) {
                       table.setEditable(false);
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
        xoa.setCellFactory(col -> new TableCell<TamVang, Void>() {
            private final Button btn1 = new Button("Xoá");

            {
              btn1.setOnAction(event->{
                
                    TamVang curItem = getTableRow().getItem();
                    data.remove(curItem);

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

 

        // nut tim kiem
        search.setOnKeyPressed(event -> { 
            if(event.getCode() == KeyCode.ENTER)
                search();
            
         });
         
    }
     FilteredList<TamVang> filter = new FilteredList<>(data,p->true);
    @FXML
    private void search(){
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<TamVang> searchFilter = TamVang ->{
            if(TamVang.getCccd().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(TamVang.getTen().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(TamVang.getPhong().toLowerCase().contains(l.toLowerCase()))
            return true;
            if(TamVang.getLydo().toLowerCase().contains(l.toLowerCase()))
            return true;
            try {
                if(TamVang.getNgayvang().toString().toLowerCase().contains(l.toLowerCase()))
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
    private void add_tamvang() throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add_tamvang.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 450);
        AddTamVangController addTamVangController = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thêm phí thu");
        subStage.show();
        subStage.setOnHiding(event->{
            
            if(addTamVangController.newTamVang==null) return;
            else{
                data.add(addTamVangController.newTamVang);
                 


            }
        });
        
    }

    @FXML 
    private void applyFilter(){
        Predicate<TamVang> pFilter = TamVang ->{
            boolean chk_ten = text_ten.getText().isEmpty()||TamVang.getTen().toLowerCase().contains(text_ten.getText().toLowerCase());
            boolean chk_cccd=text_cccd.getText().isEmpty()||TamVang.getCccd().toLowerCase().contains(text_cccd.getText().toLowerCase());
            boolean chk_phong=text_phong.getText().isEmpty()||TamVang.getPhong().toLowerCase().contains(text_phong.getText().toLowerCase());
            boolean chk_lydo=text_lydo.getText().isEmpty()||TamVang.getLydo().toLowerCase().contains(text_lydo.getText().toLowerCase());
            boolean chk_ngayvang = ngayvang_date.getValue()==null||(ngayvang_date.getValue()!=null&&ngayvang_date.getValue().isEqual(TamVang.getNgayvang()));
            
            return chk_ten&&chk_cccd&&chk_phong&&chk_lydo&&chk_ngayvang;
        };

        filter.setPredicate(pFilter);
        table.setItems(filter);
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
        text_ten.clear();
        text_cccd.clear();
        text_lydo.clear();
        text_phong.clear();
        ngayvang_date.setValue(null);
        Predicate<TamVang> remove = TamVang ->true;
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
