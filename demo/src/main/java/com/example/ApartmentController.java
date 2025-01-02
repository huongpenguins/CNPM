package com.example;

import com.example.dal.CanHoDAL;
import com.example.Entities.CanHo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

public class ApartmentController {

    @FXML
    private TableView<CanHo> quanlycanho;
    @FXML
    private TableColumn<CanHo, String> macanho;
    @FXML
    private TableColumn<CanHo, String> tencanho;
    @FXML
    private TableColumn<CanHo, String> tang;
    @FXML
    private TableColumn<CanHo, String> dientich;
    @FXML
    private TableColumn<CanHo, String> mota;
    @FXML
    private TableColumn<CanHo, String> mahogiadinh; // Chỉ hiển thị, lấy từ JOIN hogiadinhtbl

    @FXML
    private TextField txtSearch;
    @FXML
    private VBox sidebar;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete;

    private ObservableList<CanHo> masterData = FXCollections.observableArrayList();

    private CanHoDAL canHoDal = new CanHoDAL();

    public void initialize() {
        // Map cột
        macanho.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getMaCanHo()));
        tencanho.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getTenCanHo()));
        tang.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getTang())));
        dientich.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getDienTich())));
        mota.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getMoTa()));
        mahogiadinh.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getMaHoGiaDinh()));
        // => Chỉ hiển thị (nếu hộ nào đã gán MaCanHo)

        // Tải dữ liệu ban đầu
        loadCanHoData();

        // Xử lý selection
        quanlycanho.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            boolean isSelected = (newV != null);
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });
    }

    private void loadCanHoData() {
        // Clear dữ liệu cũ
        masterData.clear();
        // Lấy danh sách từ DAL
        ArrayList<CanHo> ds = CanHoDAL.loadAllData();
        if (ds != null && !ds.isEmpty()) {
            masterData.addAll(ds);
        }
        quanlycanho.setItems(masterData);
    }

    // Thêm
    @FXML
    private void addCanHo() {
        Dialog<Pair<String, String[]>> dialog = createCanHoDialog("Thêm căn hộ", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            // key = MaCanHo
            String maCanHo = data.getKey();
            String[] details = data.getValue(); // [TenCanHo, Tang, DienTich, MoTa]
            try {
                String tenCanHo = details[0];
                int tang = Integer.parseInt(details[1]);
                float dienTich = Float.parseFloat(details[2]);
                String moTa = details[3];

                // Tạo mảng cột
                String[] columns = {"MaCanHo","TenCanHo","Tang","DienTich","MoTa"};
                String[] values = {
                        maCanHo,
                        tenCanHo,
                        String.valueOf(tang),
                        String.valueOf(dienTich),
                        moTa
                };

                boolean ok = canHoDal.insertCanHo(columns, values);
                if (ok) {
                    showAlert("Thành công", "Đã thêm căn hộ " + maCanHo);
                    loadCanHoData();
                } else {
                    showAlert("Lỗi", "Không thể thêm căn hộ!");
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Tầng / Diện tích phải là số!");
            }
        });
    }

    // Sửa
    @FXML
    private void updateCanHo() {
        CanHo selected = quanlycanho.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Chưa chọn căn hộ cần sửa!");
            return;
        }

        // Tạo dialog, nạp data
        Dialog<Pair<String, String[]>> dialog = createCanHoDialog("Sửa căn hộ", selected);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String maCanHo = data.getKey();
            // [TenCanHo, Tang, DienTich, MoTa]
            String[] details = data.getValue();
            try {
                String tenCanHo = details[0];
                int tang = Integer.parseInt(details[1]);
                float dienTich = Float.parseFloat(details[2]);
                String moTa = details[3];

                // Thực hiện update
                boolean ok1 = canHoDal.updateCanHo("TenCanHo", tenCanHo, maCanHo);
                boolean ok2 = canHoDal.updateCanHo("Tang", String.valueOf(tang), maCanHo);
                boolean ok3 = canHoDal.updateCanHo("DienTich", String.valueOf(dienTich), maCanHo);
                boolean ok4 = canHoDal.updateCanHo("MoTa", moTa, maCanHo);

                if (ok1 && ok2 && ok3 && ok4) {
                    showAlert("Thành công", "Cập nhật căn hộ thành công!");
                    loadCanHoData();
                } else {
                    showAlert("Lỗi", "Không thể cập nhật căn hộ!");
                }

            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Tầng / Diện tích phải là số!");
            }
        });
    }

    // Xoá
    @FXML
    private void deleteCanHo() {
        CanHo sel = quanlycanho.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("Thông báo", "Chưa chọn căn hộ để xoá!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xoá căn hộ");
        confirm.setHeaderText("Xác nhận xoá?");
        confirm.setContentText("Mã căn hộ: " + sel.getMaCanHo());
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            boolean ok = canHoDal.deleteCanHo(sel.getMaCanHo());
            if (ok) {
                showAlert("Thành công", "Đã xoá căn hộ!");
                loadCanHoData();
            } else {
                showAlert("Lỗi", "Không thể xoá căn hộ!");
            }
        }
    }

    // Tìm kiếm
    @FXML
    private void searchCanHo() {
        String keyword = txtSearch.getText().trim();
        masterData.clear();
        if (keyword.isEmpty()) {
            loadCanHoData();
            return;
        }
        ArrayList<CanHo> results = canHoDal.searchCanHo(keyword);
        if (results != null && !results.isEmpty()) {
            masterData.addAll(results);
        }
        quanlycanho.setItems(masterData);
    }

    // Tạo dialog
    private Dialog<Pair<String, String[]>> createCanHoDialog(String title, CanHo data) {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType okButton = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);

        Label lblMaCanHo   = new Label("Mã Căn Hộ:");
        Label lblTenCanHo  = new Label("Tên Căn Hộ:");
        Label lblTang      = new Label("Tầng:");
        Label lblDienTich  = new Label("Diện Tích:");
        Label lblMoTa      = new Label("Mô tả:");

        TextField txtMaCanHo   = new TextField();
        TextField txtTenCanHo  = new TextField();
        TextField txtTang      = new TextField();
        TextField txtDienTich  = new TextField();
        TextField txtMoTa      = new TextField();

        gp.add(lblMaCanHo,   0, 0); gp.add(txtMaCanHo,   1, 0);
        gp.add(lblTenCanHo,  0, 1); gp.add(txtTenCanHo,  1, 1);
        gp.add(lblTang,      0, 2); gp.add(txtTang,      1, 2);
        gp.add(lblDienTich,  0, 3); gp.add(txtDienTich,  1, 3);
        gp.add(lblMoTa,      0, 4); gp.add(txtMoTa,      1, 4);

        // Nếu sửa, nạp data cũ
        if (data != null) {
            txtMaCanHo.setText(data.getMaCanHo());
            txtTenCanHo.setText(data.getTenCanHo());
            txtTang.setText(String.valueOf(data.getTang()));
            txtDienTich.setText(String.valueOf(data.getDienTich()));
            txtMoTa.setText(data.getMoTa());
        }

        dialog.getDialogPane().setContent(gp);

        // Trả kết quả
        dialog.setResultConverter(button -> {
            if (button == okButton) {
                // Pair: key=MaCanHo, value=[tenCanHo, tang, dienTich, moTa]
                return new Pair<>(txtMaCanHo.getText(), new String[]{
                        txtTenCanHo.getText(),
                        txtTang.getText(),
                        txtDienTich.getText(),
                        txtMoTa.getText()
                });
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
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
