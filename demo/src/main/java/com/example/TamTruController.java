package com.example;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import com.example.Entities.TamTru;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TamTruController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/quanlychungcu";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "2003";

    private final ObservableList<TamTru> data = FXCollections.observableArrayList();
    private final FilteredList<TamTru> filter = new FilteredList<>(data, p -> true);

    @FXML
    private VBox sidebar;
    @FXML
    private Button menu, account, giadinh, dancu, tamtru, canho, tamvang, trangchu, apply, remove;
    @FXML
    private TextField search, text_ten, text_dcthuongtru, text_cccd, text_dctamtru;
    @FXML
    private TableView<TamTru> table;
    @FXML
    private TableColumn<TamTru, String> ten, dcthuongtru, dctamtru, cccd;
    @FXML
    private TableColumn<TamTru, LocalDate> ngaybdtamtru;
    @FXML
    private TableColumn<TamTru, Void> chinhsua, chitiet, xoa;
    @FXML
    private AnchorPane filterbar;
    @FXML
    private DatePicker ngaybdtamtru_date;

    public void initialize() {
        table.setEditable(false);

        cccd.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        ten.setCellValueFactory(new PropertyValueFactory<>("ten"));
        dcthuongtru.setCellValueFactory(new PropertyValueFactory<>("dcthuongtru"));
        dctamtru.setCellValueFactory(new PropertyValueFactory<>("dctamtru"));
        ngaybdtamtru.setCellValueFactory(new PropertyValueFactory<>("ngaybdtamtru"));

        table.setItems(data);

        chinhsua.setCellFactory(col -> createEditButtonCell());
        xoa.setCellFactory(col -> createDeleteButtonCell());

        search.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search();
            }
        });

        loadDatabaseData();
    }

    private void loadDatabaseData() {
        String query = "SELECT HoTen, CCCD, DcThuongTru, DcTamTru, Ngaybdtamtru FROM TamTru";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                TamTru tamTru = new TamTru(
                        resultSet.getString("HoTen"),
                        resultSet.getString("CCCD"),
                        resultSet.getString("DcThuongTru"),
                        resultSet.getString("DcTamTru"),
                        resultSet.getDate("Ngaybdtamtru").toLocalDate()
                );
                data.add(tamTru);
            }

        } catch (SQLException e) {
            showError("Error loading data", e.getMessage());
        }
    }

    private TableCell<TamTru, Void> createEditButtonCell() {
        return new TableCell<>() {
            private final Button btn = new Button("Sửa");

            {
                btn.setOnAction(event -> {
                    TamTru current = getTableRow().getItem();
                    if (current != null) {
                        enableEditingForRow(current);
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
        };
    }

    private void enableEditingForRow(TamTru tamTru) {
        table.setEditable(true);

        ten.setCellFactory(TextFieldTableCell.forTableColumn());
        ten.setOnEditCommit(event -> updateDatabase(event, "HoTen", event.getNewValue()));

        cccd.setCellFactory(TextFieldTableCell.forTableColumn());
        cccd.setOnEditCommit(event -> updateDatabase(event, "CCCD", event.getNewValue()));

        dcthuongtru.setCellFactory(TextFieldTableCell.forTableColumn());
        dcthuongtru.setOnEditCommit(event -> updateDatabase(event, "DcThuongTru", event.getNewValue()));

        dctamtru.setCellFactory(TextFieldTableCell.forTableColumn());
        dctamtru.setOnEditCommit(event -> updateDatabase(event, "DcTamTru", event.getNewValue()));

        ngaybdtamtru.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    showError("Định dạng ngày không hợp lệ", "Vui lòng nhập ngày ở định dạng yyyy-MM-dd.");
                    return null;
                }
            }
        }));
        ngaybdtamtru.setOnEditCommit(event -> updateDatabase(event, "Ngaybdtamtru", event.getNewValue().toString()));

        table.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                table.setEditable(false);
            }
        });
    }

    private TableCell<TamTru, Void> createDeleteButtonCell() {
        return new TableCell<>() {
            private final Button btn = new Button("Xoá");

            {
                btn.setOnAction(event -> {
                    TamTru current = getTableRow().getItem();
                    if (current != null) {
                        deleteFromDatabase(current);
                        data.remove(current);
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
        };
    }

    private void updateDatabase(CellEditEvent<TamTru, ?> event, String columnName, Object newValue) {
        TamTru tamTru = event.getRowValue();

        String query = "UPDATE TamTru SET " + columnName + " = ? WHERE CCCD = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setObject(1, newValue);
            preparedStatement.setString(2, tamTru.getCCCD());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            showError("Lỗi cập nhật dữ liệu", e.getMessage());
        }
    }

    private void deleteFromDatabase(TamTru tamTru) {
        String query = "DELETE FROM TamTru WHERE CCCD = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tamTru.getCCCD());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            showError("Lỗi xóa dữ liệu", e.getMessage());
        }
    }

    @FXML
    private void search() {
        String searchText = search.getText().toLowerCase();

        Predicate<TamTru> searchFilter = tamTru ->
                tamTru.getCCCD().toLowerCase().contains(searchText) ||
                        tamTru.getTen().toLowerCase().contains(searchText) ||
                        tamTru.getDcThuongTru().toLowerCase().contains(searchText) ||
                        tamTru.getDcTamTru().toLowerCase().contains(searchText) ||
                        tamTru.getNgaybdtamtru().toString().contains(searchText);

        filter.setPredicate(searchFilter);
        table.setItems(filter);
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
