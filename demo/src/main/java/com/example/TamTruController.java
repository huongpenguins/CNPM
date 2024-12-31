package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

import com.example.Entities.TamTru;

import com.example.Entities.TamVang;
import com.example.dal.TamTruDAL;
import com.example.dal.TamVangDAL;
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

public class TamTruController {

    ResultSet resultSet;
    // noi luu tru data
    ObservableList<TamTru> data = TamTruDAL.loadData("0");

    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,TamTru,canho,tamtru,tamvang,trangchu,apply,remove;
    @FXML
    TextField search,text_ten,text_dcthuongtru,text_cccd,text_dctamtru;

    @FXML
    TableView<TamTru> table;
    @FXML
    TableColumn<TamTru,String> ten,dcthuongtru,dctamtru,cccd;
    @FXML
    TableColumn<TamTru,LocalDate> ngaybdtamtru;
    @FXML
    TableColumn<TamTru,Void> chinhsua,xoa;
    @FXML
    AnchorPane filterbar;
    @FXML
    DatePicker ngaybdtamtru_date;
    TamTruDAL tamtruDAL = new TamTruDAL();


    public void initialize(){
        table.setEditable(false);


        // lien ket cot voi thuoc tinh trong TamTru
        dcthuongtru.setCellValueFactory(new PropertyValueFactory<TamTru,String>("DiaChiThuongTru"));
        ten.setCellValueFactory(new PropertyValueFactory<TamTru,String>("HoTen"));
        dctamtru.setCellValueFactory(new PropertyValueFactory<TamTru,String>("DiaChiTamTru"));
        cccd.setCellValueFactory(new PropertyValueFactory<TamTru,String>("CCCD"));
        ngaybdtamtru.setCellValueFactory(new PropertyValueFactory<TamTru,LocalDate>("NgayBatDauTamTru"));


        table.setItems(data);

        // them button chinh sua vao cot
        chinhsua.setCellFactory(col -> new TableCell<TamTru, Void>() {
            private final Button btn = new Button("Sửa");
            {

                btn.setOnAction(event->{
                    TamTru k = getTableView().getItems().get(getIndex());
                    try {
                        edit_tamtru(k);
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
        xoa.setCellFactory(col -> new TableCell<TamTru, Void>() {
            private final Button btn1 = new Button("Xoá");

            {
                btn1.setOnAction(event->{

                    TamTru curItem = getTableRow().getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận xoá");
                    alert.setHeaderText("Bạn có chắc muốn xoá mục này?");
                    alert.setContentText("Mục: " + curItem.getTen());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean t = tamtruDAL.deleteTamTru("tamtrutbl","MaTamTru" ,curItem.getMaNhanKhau());
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



        // nut tim kiem
        search.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER)
                search();

        });

    }
    FilteredList<TamTru> filter = new FilteredList<>(data,p->true);
    @FXML
    private void search(){
        String l = search.getText();
        // cap nhat observablelist data de hien thi DL

        Predicate<TamTru> searchFilter = TamTru ->{
            if(TamTru.getCCCD().toLowerCase().contains(l.toLowerCase()))
                return true;
            if(TamTru.getTen().toLowerCase().contains(l.toLowerCase()))
                return true;
            if(TamTru.getDcThuongTru().toLowerCase().contains(l.toLowerCase()))
                return true;
            if(TamTru.getDcTamTru().toLowerCase().contains(l.toLowerCase()))
                return true;
            try {
                if(TamTru.getNgaybdtamtru().toString().toLowerCase().contains(l.toLowerCase()))
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
    private void add_tamtru() throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add_tamtru.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 450);
        AddTamTruController addTamTruController = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thêm tạm trú");
        subStage.show();
        subStage.setOnHiding(event->{

            if(addTamTruController.newTamTru==null) return;
            else{
                for(TamTru i : addTamTruController.listNew){
                    data.add(i);
                    table.refresh();
                }
            }
        });

    }

    private void edit_tamtru(TamTru k) throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit_tamtru.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 450);
        EditTamTruController edit = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Sửa tạm trú");
        edit.id_text.setText(k.getMaNhanKhau());
        edit.dctamtru_text.setText(k.getDcTamTru());
        edit.ngaybdtamtru.setValue(k.getNgaybdtamtru());

        subStage.show();
        subStage.setOnHiding(event->{
            if(!edit.id_text.getText().equals(k.getMaNhanKhau())){

                k.setMaNhanKhau(edit.id_text.getText());
                k.setDcTamTru(edit.dctamtru_text.getText());
                k.setNgaybdtamtru(edit.ngaybdtamtru.getValue());

                table.refresh();
            }

        });
    }

    @FXML
    private void applyFilter(){
        Predicate<TamTru> pFilter = TamTru ->{
            boolean chk_ten = text_ten.getText().isEmpty()||TamTru.getTen().toLowerCase().contains(text_ten.getText().toLowerCase());
            boolean chk_cccd=text_cccd.getText().isEmpty()||TamTru.getCCCD().toLowerCase().contains(text_cccd.getText().toLowerCase());
            boolean chk_dcthuongtru=text_dcthuongtru.getText().isEmpty()||TamTru.getDcThuongTru().toLowerCase().contains(text_dcthuongtru.getText().toLowerCase());
            boolean chk_dctamtru=text_dctamtru.getText().isEmpty()||TamTru.getDcTamTru().toLowerCase().contains(text_dctamtru.getText().toLowerCase());
            boolean chk_ngaybdtamtru = ngaybdtamtru_date.getValue()==null||(ngaybdtamtru_date.getValue()!=null&&ngaybdtamtru_date.getValue().isEqual(TamTru.getNgaybdtamtru()));

            return chk_ten&&chk_cccd&&chk_dcthuongtru&&chk_dctamtru&&chk_ngaybdtamtru;
        };

        filter.setPredicate(pFilter);
        table.setItems(filter);
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
        text_ten.clear();
        text_cccd.clear();
        text_dcthuongtru.clear();
        text_dctamtru.clear();
        ngaybdtamtru_date.setValue(null);
        Predicate<TamTru> remove = TamTru ->true;
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
        App.setRoot("ResidentsManager");
    }
    @FXML
    private void switchToKhoanThu() throws IOException {
        App.setRoot("fee");
    }
    @FXML
    private void switchToCanHo() throws IOException {
        App.setRoot("Apartment");
    }
    @FXML
    private void switchToTamTru() throws IOException {
        App.setRoot("tamtru");
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
