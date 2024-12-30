package com.example;

import com.example.dal.CanHoDAL;
import com.example.Entities.CanHo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ApartmentController {

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
    private CanHoDAL canhoDal = new CanHoDAL(); // DAL cho CanHo


    public void initialize() {
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        macanho.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaCanHo()));
        mahokhau.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaHoKhau()));
        tencanho.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTenCanHo()));
        tang.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getTang())));
        dientich.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDienTich())));
        mota.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMoTa()));


        loadCanHoData();
/*
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnViewDetails.setDisable(true);

 */

        quanlycanho.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
            btnViewDetails.setDisable(!isSelected);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCanHo();
        });
    }

    private void loadCanHoData() {
        Admin admin = new Admin(); // Tạo đối tượng Admin
        if (admin.getConnectionAdmin() == null) {
            System.err.println("Kết nối cơ sở dữ liệu chưa được khởi tạo.");
            return;  // Trả về danh sách rỗng nếu không có kết nối
        }
        masterData.clear();
        ArrayList<Object[]> results = canhoDal.searchCanHo("canhotbl", null, null);
        if (results != null) {

            for (Object[] row : results) {
                // Giả định thứ tự cột trong CSDL: MaCanHo, MaHoKhau, TenCanHo, Tang, DienTich, Mota
                String MaCanHo = (String) row[0];
                String MaHoKhau = (String) row[1];
                String TenCanHo = (String) row[2];
                int Tang = (int) row[3];
                float DienTich = (float) row[4];
                String MoTa = (String) row[5];


                masterData.add(new CanHo(MaCanHo, MaHoKhau, TenCanHo, Tang, DienTich, MoTa));
            }
        }
        quanlycanho.setItems(masterData);
    }


    @FXML
    private void searchCanHo() {
        String keyword = txtSearch.getText().trim();
        ArrayList<Object[]> results;
        if (keyword.isEmpty()) {
            results = canhoDal.searchCanHo("canhotbl", null, null);
        } else {
            if (keyword.matches("\\d+")) {
                results = canhoDal.searchCanHo("canhotbl", "MaCanHo", keyword);
                 if (results == null || results.isEmpty()) {
                     results = canhoDal.searchCanHo("canhotbl", "TenCanHo", keyword);
                 }
            } else {
                results = canhoDal.searchCanHo("canhotbl", "TenCanHo", keyword);
            }
        }

        masterData.clear();
        if (results != null) {
            for (Object[] row : results) {
                String MaCanHo = (String) row[0];
                String MaHoKhau = (String) row[1];
                String TenCanHo = (String) row[2];
                int Tang = (int) row[3];
                float DienTich = (float) row[4];
                String MoTa = (String) row[5];
                masterData.add(new CanHo(MaCanHo, MaHoKhau, TenCanHo, Tang, DienTich, MoTa));
            }
        }
        quanlycanho.setItems(masterData);
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
            /*
            String[] columns = {"MaCanHo","MaHoKhau","TenCanHo","Tang","DienTich","MoTa"};
            String[] values = {macanho, mahokhau, tencanho, tang, dientich, mota};

             */
            CanHo newCanHo = new CanHo(macanho, mahokhau, tencanho, Integer.parseInt(tang), Float.parseFloat(dientich), mota);
            boolean success = canhoDal.insertCanHo(newCanHo);
            if (success) {
                showAlert("Thành công", "Căn hộ mới đã được thêm!");
                loadCanHoData();
            } else {
                showAlert("Lỗi", "Không thể thêm căn hộ!");
            }
        });
    }

    @FXML
    private void updateCanHo() {
        CanHo selected = quanlycanho.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Pair<String, String[]>> dialog = createCanHoDialog("Sửa căn hộ", selected);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String macanho = data.getKey();
                String[] details = data.getValue();
                String mahokhau = details[0];
                String tencanho = details[1];
                String tang = details[2];
                String dientich = details[3];
                String mota = details[4];

                boolean allSuccess = true;

                try {
                    allSuccess &= canhoDal.updateCanHo("canhotbl", "MaHoKhau", mahokhau, "MaCanHo", macanho);
                    allSuccess &= canhoDal.updateCanHo("canhotbl", "TenCanHo", tencanho, "MaCanHo", macanho);
                    allSuccess &= canhoDal.updateCanHo("canhotbl", "Tang", tang, "MaCanHo", macanho);
                    allSuccess &= canhoDal.updateCanHo("canhotbl", "DienTich", dientich, "MaCanHo", macanho);
                    allSuccess &= canhoDal.updateCanHo("canhotbl", "Mota", mota, "MaCanHo", macanho);
                } catch (SQLException e) {
                    e.printStackTrace();
                    allSuccess = false;
                }

                if (allSuccess) {
                    showAlert("Sửa thành công", "Thông tin căn hộ đã được cập nhật!");
                    loadCanHoData();
                } else {
                    showAlert("Lỗi", "Không thể cập nhật căn hộ!");
                }
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
                boolean success = canhoDal.deleteCanHo("canhotbl", "MaCanHo", selected.getMaCanHo());
                if (success) {
                    showAlert("Xóa thành công", "Căn hộ đã được xóa khỏi hệ thống!");
                    loadCanHoData();
                } else {
                    showAlert("Lỗi", "Không thể xóa căn hộ!");
                }
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn căn hộ cần xóa!");
        }
    }

    @FXML
    private void viewCanHoDetails() {
        CanHo selected = quanlycanho.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String macanho = selected.getMaCanHo();

            // Hiển thị dialog chi tiết
            Dialog<Void> detailDialog = new Dialog<>();
            detailDialog.setTitle("Chi tiết căn hộ - Mã: " + macanho);

            ButtonType closeButtonType = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
            detailDialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        } else {
            showAlert("Lỗi", "Vui lòng chọn căn hộ để xem chi tiết!");
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
