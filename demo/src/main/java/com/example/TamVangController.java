package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import com.example.Entities.KhoanThu;
import com.example.Entities.TamVang;
import com.example.dal.NhanKhauDAL;
import com.example.dal.TamVangDAL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TamVangController {
    
    ResultSet resultSet;
    // noi luu tru data
    ObservableList<TamVang> data = TamVangDAL.loadData("0");
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
    TableColumn<TamVang,Void> chinhsua,chitiet,xoa,completed;
    @FXML
    AnchorPane filterbar;
    @FXML
    DatePicker ngayvang_date;
    TamVangDAL tamVangDAL = new TamVangDAL();
    
    public void initialize(){
        table.setEditable(false);
        
       
        // lien ket cot voi thuoc tinh trong TamVang
        phong.setCellValueFactory(new PropertyValueFactory<TamVang,String>("phong"));
        ten.setCellValueFactory(new PropertyValueFactory<TamVang,String>("ten"));
        lydo.setCellValueFactory(new PropertyValueFactory<TamVang,String>("lydo"));
        cccd.setCellValueFactory(new PropertyValueFactory<TamVang,String>("cccd"));
        ngayvang.setCellValueFactory(new PropertyValueFactory<TamVang,LocalDate>("ngayvang"));


        table.setItems(data);
        
        // them button chinh sua vao cot
        chinhsua.setCellFactory(col -> new TableCell<TamVang, Void>() {
            
            private final Button btn = new Button("Sửa");
           
            {    
                btn.setStyle("-fx-background-image: url('picture/edit.png'); "+
                             "-fx-background-position: center; "+
                             "-fx-background-repeat: no-repeat; " +
                             "-fx-background-size: contain; "  );
            
                
               btn.setOnAction(event->{
                TamVang k = getTableView().getItems().get(getIndex());
                try {
                    edit_tamvang(k);
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
        xoa.setCellFactory(col -> new TableCell<TamVang, Void>() {
            private final Button btn1 = new Button("Xoá");

            {
              btn1.setOnAction(event->{
                    btn1.setStyle("-fx-background-image: url('picture/xoa.png'); "+
                    "-fx-background-position: center; "+
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-size: contain; "  );

   
                    TamVang curItem = getTableRow().getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận xoá");
                    alert.setHeaderText("Bạn có chắc muốn xoá mục này?");
                    alert.setContentText("Mục: " + curItem.getTen());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean t = tamVangDAL.deleteTamVang("tamvangtbl","MaTamVang" ,curItem.getMaTamVang());
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

        completed.setCellFactory(col -> new TableCell<TamVang, Void>() {
            private final Button btn1 = new Button("Hoàn thành");

            {
                btn1.setStyle("-fx-background-image: url('picture/tick.png'); "+
                "-fx-background-position: center; "+
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: contain; "  );

                

              btn1.setOnAction(event->{
                    TamVang curItem = getTableRow().getItem();
                    try {
                        boolean t = TamVangDAL.updateTamVang1(" tamvvangtbl "," Completed "," 1 "," MaTamVang ",curItem.getMaTamVang());
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
        
            if(addTamVangController.listNew.isEmpty()) return;
            else{
                for(TamVang i : addTamVangController.listNew){
                    data.add(i);
                    table.refresh();
                }
            }
        });
        
    }
    
    private void edit_tamvang(TamVang k) throws IOException{
        Stage subStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("edit_tamvang.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 600);
        EditTamVangController edit = fxmlLoader.getController();
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Sửa phí thu");
        edit.maTamVang=(k.getMaTamVang());
        edit.id_text.setText(k.getMaNhanKhau());
        edit.lydo_text.setText(k.getLydo());
        edit.ngayvang.setValue(k.getNgayvang());
        edit.setNewTamVang(k);
        subStage.show();
        subStage.setOnHiding(event->{
            if(!edit.id_text.getText().equals(k.getMaNhanKhau())){
                
            k.setMaNhanKhau(edit.id_text.getText());
            k.setLydo(edit.lydo_text.getText());
            k.setNgayvang(edit.ngayvang.getValue());

            table.refresh();
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

    public static void main(String[] args) {

    }
}
