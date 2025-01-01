package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Predicate;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

public class DienNuocController {
    KhoanThu k;
    String maKT,tenKhoanThu;
    LocalDate tungay,denngay;
    @FXML
    Button apply,addfile;

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
                    if(getTableView().getItems().get(getIndex())==null){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Chưa nhap so dien nuoc. Hay them file o tren");
                    }
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
        data = ChiTietKhoanThuDAL.loadDienNuoc(maKT, "0", "Bắt buộc");
        table.setItems(data);
    }
    @FXML
    private void Addfile(){
         FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

                File file = fileChooser.showOpenDialog(addfile.getScene().getWindow());

                if (file != null) {
                    try {
                        readFile(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                }
            }
    }

    private void readFile(File file) throws FileNotFoundException, IOException{
        String[] column ={"MaKhoanThu","MaHoGiaDinh","So"};
        String[] types = {"string","string","int"};
        String maHoGiaDinh=null;
        int so=-1;
        String filePath = file.getAbsolutePath();
        FileInputStream fs = new FileInputStream(filePath);
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        XSSFSheet sheet = wb.getSheetAt(0);  // Lấy sheet đầu tiên
        XSSFRow row;
        XSSFCell cell;
        int rows; 
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 2; 

        for(int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if(row != null) {
                for(int c = 0; c < cols; c++) {
                    cell = row.getCell((short)c);
                    if(cell != null) {
                        if(c==0){
                            if(cell.getCellType() == CellType.STRING){
                               maHoGiaDinh = cell.toString(); 
                            }
                            else{
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("file dinh dang sai");
                                return;
                            }
                            
                        }
                        else if(c==1){
                            if(cell.getCellType() == CellType.NUMERIC){
                            so = (int)cell.getNumericCellValue();
                            }
                            else{
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("file dinh dang sai");
                                return;
                            }
                        }
                        if(maHoGiaDinh!=null&&so!=-1){
                        String[] value ={maKT,maHoGiaDinh,String.valueOf(so)};
                        boolean t =Admin.insert1("SoDienNuoctbl", column, types, value);
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Khong the nhap so dien nuoc vi da ton tai");
                        }
                    }
                }
            }

            data = ChiTietKhoanThuDAL.loadData1(maKT, "0", "Bắt buộc");
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
        subStage.setResizable(false);
        subStage.setScene(scene);
        subStage.setTitle("Thanh toán");
        
        subStage.show();
        subStage.setOnHiding(event->{
            
            if(thanhToanController.hoaDon==null) return;
            else{
                // for(CTKhoanThu i : data){
                    
                //     if(i.getId().equals(thanhToanController.hoaDon.getMaho())){
                //         i.setDanop(i.getDanop()+thanhToanController.hoaDon.getSotiennop());
                //         i.setNgaynop(thanhToanController.hoaDon.getThoidiem());// cap nhat ngay nop la ngay gan nhat
                //         table.refresh();
                //         return;
                        
                //     }
                // }
                // data.add(new CTKhoanThu(ct.getTenKT(), thanhToanController.hoaDon.getMaho(),
                // ct.getTen(), thanhToanController.hoaDon.getThoidiem(),
                //  ct.getTiennop(), thanhToanController.hoaDon.getSotiennop()));
                // Tai lai csdl r cap nhat bangbang
                table.refresh();

            }
        });
    }
    
  
     private FilteredList<CTKhoanThu> filter = new FilteredList<>(data, p -> true);
    @FXML
    private void search(){
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
    }
    @FXML
        private void applyFilter(){
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
        filterbar.setLayoutY(-131);
    }
    @FXML
    private void Remove(){
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
