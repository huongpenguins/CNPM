package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.beans.property.SimpleStringProperty;
import com.example.Entities.CanHo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

public class ApartmentController  {

    @FXML
    private VBox sidebar;
    @FXML
    private Button menu, account, giadinh, dancu, khoanthu, canho, tamtru, tamvang, trangchu;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete, btnViewDetails;

    @FXML
    private TableView<CanHo> quanlycanho;
    @FXML
    private TableColumn<CanHo, String> macanho;
    @FXML
    private TableColumn<CanHo, String> mahokhau;
    @FXML
    private TableColumn<CanHo, String> tencanho;
    @FXML
    private TableColumn<CanHo, String> tang;
    @FXML
    private TableColumn<CanHo, String> dientich;
    @FXML
    private TableColumn<CanHo, String> mota;

    private ObservableList<CanHo> masterData = FXCollections.observableArrayList();
    private FilteredList<CanHo> filteredData;

    public void initialize() {
        // Đặt sidebar ra ngoài màn hình khi khởi tạo
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Thiết lập cellValueFactory
        macanho.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaCanHo()));
        mahokhau.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaHoKhau()));
        tencanho.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTenCanHo()));
        tang.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getTang())));
        dientich.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDienTich())));
        mota.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMoTa()));

        loadCanHoData();

        // Vô hiệu hóa Sửa, Xóa, Xem chi tiết khi chưa chọn
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnViewDetails.setDisable(true);

        quanlycanho.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
            btnViewDetails.setDisable(!isSelected);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCanHo(newValue);
        });

        // Gắn sự kiện cho các nút (nếu chưa có trong FXML)
        btnSearch.setOnAction(event -> searchCanHo());
        btnAdd.setOnAction(event -> addCanHo());
        btnEdit.setOnAction(event -> editCanHo());
        btnDelete.setOnAction(event -> deleteCanHo());
        btnViewDetails.setOnAction(event -> viewCanHoDetails());
    }

    private void loadCanHoData() {
        masterData.clear();
        try {
            // SQL xử lý:
            // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "user", "pass");
            // PreparedStatement pstmt = conn.prepareStatement("SELECT household_id, apartment_id, resident_id, vehicle_id, issue_date, owner_name, phone_number FROM households");
            // ResultSet rs = pstmt.executeQuery();
            // while(rs.next()){
            //     masterData.add(new Household(
            //         rs.getString("household_id"),
            //         rs.getString("apartment_id"),
            //         rs.getString("resident_id"),
            //         rs.getString("vehicle_id"),
            //         rs.getString("issue_date"),
            //         rs.getString("owner_name"),
            //         rs.getString("phone_number")
            //     ));
            // }
            // conn.close();

            // Dữ liệu giả lập
            masterData.addAll(
                    new CanHo("HK001", "CH001", "NK001", 3, 30, "Mô tả căn hộ"),
                    new CanHo("HK002", "CH002", "NK002", 4, 40, "Mô tả căn hộ"),
                    new CanHo("HK003", "CH003", "NK003", 2, 25, "Mô tả căn hộ")
            );

            filteredData = new FilteredList<>(masterData, p -> true);
            quanlycanho.setItems(filteredData);

        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    @FXML
    private void searchCanHo() {
        filterCanHo(txtSearch.getText());
    }

    private void filterCanHo(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            String lowerCaseKeyword = keyword.toLowerCase();
            filteredData.setPredicate(hh ->
                    hh.getMaCanHo().toLowerCase().contains(lowerCaseKeyword) ||
                            hh.getTenCanHo().toLowerCase().contains(lowerCaseKeyword)
            );
        }
    }

    @FXML
    private void addCanHo() {
        Dialog<Pair<String, String[]>> dialog = createCanHoDialog("Thêm căn hộ", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String macanho = data.getKey();
            String[] details = data.getValue();
            String mahokhau = details[0];
            String tencanho = details[1];
            String tang = details[2];
            String dientich = details[3];
            String mota = details[4];

            System.out.println("Thêm căn hộ: " + macanho + ", " + mahokhau + ", " + tencanho + ", " + tang + ", " + dientich + ", " + mota );
            // SQL xử lý: INSERT INTO CanHo VALUES(...)

            showAlert("Thành công", "Căn Hộ mới đã được thêm!");
            // loadCanHoData();
        });
    }

    @FXML
    private void editCanHo() {
        CanHo selected = quanlycanho.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Pair<String, String[]>> dialog = createCanHoDialog("Sửa căn hộ", selected);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String macanho = data.getKey();
                String[] details = data.getValue();
                String mahokhau = details[0];
                String tenhokhau = details[1];
                String tang = details[2];
                String dientich = details[3];
                String mota = details[4];

                System.out.println("Cập nhật căn hộ: " + macanho + ", " + mahokhau + ", " + tencanho + ", " + tang + ", " + dientich + ", " + mota);
                // SQL xử lý: UPDATE CanHo SET ... WHERE macanho = ?
                showAlert("Sửa thành công", "Thông tin căn hộ đã được cập nhật!");
                // loadCanHoData();
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn căn hộ cần sửa!");
        }
    }

    @FXML
    private void deleteCanHo() {
        CanHo selected = quanlycanho.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xóa căn hộ");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa căn hộ này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Xóa căn hộ: " + selected.getMaCanHo());
                // SQL xử lý: DELETE FROM CanHo WHERE macanho = ?
                showAlert("Xóa thành công", "Căn Hộ đã được xóa khỏi hệ thống!");
                // loadCanHoData();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn căn hộ cần xóa!");
        }
    }

    // Xem chi tiết
    @FXML
    public void viewCanHoDetails() {
        // Lấy căn hộ được chọn từ TableView
        CanHo selectedCanHo = quanlycanho.getSelectionModel().getSelectedItem();
        if (selectedCanHo != null) {
            // Hiển thị thông tin chi tiết của căn hộ (dùng Alert làm ví dụ)
            String details = "Mã căn hộ: " + selectedCanHo.getMaCanHo() + "\n"
                    + "Mã hộ khẩu: " + selectedCanHo.getMaHoKhau() + "\n"
                    + "Tên căn hộ: " + selectedCanHo.getTenCanHo() + "\n"
                    + "Tầng: " + selectedCanHo.getTang() + "\n"
                    + "Diện tích: " + selectedCanHo.getDienTich() + "\n"
                    + "Mô tả: " + selectedCanHo.getMoTa();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết căn hộ");
            alert.setHeaderText("Thông tin chi tiết căn hộ");
            alert.setContentText(details);
            alert.showAndWait();
        } else {
            // Thông báo nếu không có căn hộ nào được chọn
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không có căn hộ nào được chọn");
            alert.setContentText("Vui lòng chọn một căn hộ từ bảng để xem chi tiết.");
            alert.showAndWait();
        }
    }

    private Dialog<Pair<String, String[]>> createCanHoDialog(String title, CanHo canhoData) {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType okButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtmacanho = new TextField();
        TextField txtmahokhau = new TextField();
        TextField txttencanho = new TextField();
        TextField txttang = new TextField();
        TextField txtdientich = new TextField();
        TextField txtmota = new TextField();

        grid.add(new Label("Mã Căn Hộ:"), 0, 0);
        grid.add(txtmacanho, 1, 0);
        grid.add(new Label("Mã Hộ Khẩu:"), 0, 1);
        grid.add(txtmahokhau, 1, 1);
        grid.add(new Label("Tầng:"), 0, 2);
        grid.add(txttang, 1, 2);
        grid.add(new Label("Diện tích:"), 0, 3);
        grid.add(txtdientich, 1, 3);
        grid.add(new Label("Tên Căn Hộ:"), 0, 4);
        grid.add(txttencanho, 1, 4);
        grid.add(new Label("Mô tả:"), 0, 5);
        grid.add(txtmota, 1, 5);

        if (canhoData != null) {
            txtmacanho.setText(canhoData.getMaCanHo());
            txtmahokhau.setText(canhoData.getMaHoKhau());
            txttencanho.setText(canhoData.getTenCanHo());
            txttang.setText(String.valueOf(canhoData.getTang()));
            txtdientich.setText(String.valueOf(canhoData.getDienTich()));
            txtmota.setText(canhoData.getMoTa());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(txtmacanho.getText(), new String[]{
                        txtmota.getText(),
                        txtmahokhau.getText(),
                        txttencanho.getText(),
                        txttang.getText(),
                        txtdientich.getText(),
                });
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void menuClick() {
        if (sidebar != null) {
            double currentPosition = sidebar.getLayoutX();
            double sidebarWidth = sidebar.getPrefWidth();
            if (currentPosition < 0) {
                sidebar.setLayoutX(0);
            } else {
                sidebar.setLayoutX(-sidebarWidth);
            }
        } else {
            System.out.println("Sidebar chưa được khởi tạo!");
        }
    }

    @FXML
    private void signout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setContentText("Bạn muốn đăng xuất?");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK){
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
        App.setRoot("secondary");
    }

    @FXML
    private void switchToSignIn(){
        // Chuyển sang màn hình đăng nhập
    }

    @FXML
    private void switchToAccount() {
        try {
            App.setRoot("account");
        } catch (IOException e) {
            System.out.println("Lỗi: Không thể chuyển sang màn hình tài khoản.");
            e.printStackTrace();
        }
    }
}