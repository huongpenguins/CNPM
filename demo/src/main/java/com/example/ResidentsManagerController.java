package com.example;

import com.example.dal.NhanKhauDAL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ResidentsManagerController {

    @FXML
    private TableView<Resident> tableResidents;
    @FXML
    private TableColumn<Resident, String> colResidentId;
    @FXML
    private TableColumn<Resident, String> colHouseholdId;
    @FXML
    private TableColumn<Resident, String> colResidentName;
    @FXML
    private TableColumn<Resident, String> colIdentityCard;
    @FXML
    private TableColumn<Resident, String> colDateOfBirth;
    @FXML
    private TableColumn<Resident, String> colPlaceOfBirth;
    @FXML
    private TableColumn<Resident, String> colEthnicity;
    @FXML
    private TableColumn<Resident, String> colOccupation;
    @FXML
    private TableColumn<Resident, String> colRelationship;

    @FXML
    private Button btnAdd, btnEdit, btnDelete;
    @FXML
    private TextField txtSearch;
    @FXML
    private VBox sidebar;
    @FXML
    private Button menu, account, giadinh, dancu, khoanthu, canho, tamtru, tamvang, trangchu;

    private ObservableList<Resident> masterData = FXCollections.observableArrayList();
    private NhanKhauDAL nhanKhauDal = new NhanKhauDAL(); // DAL cho NhanKhau

    public void initialize() {
        colResidentId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colHouseholdId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHouseholdId()));
        colResidentName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colIdentityCard.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdentityCard()));
        colDateOfBirth.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateOfBirth()));
        colPlaceOfBirth.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlaceOfBirth()));
        colEthnicity.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEthnicity()));
        colOccupation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOccupation()));
        colRelationship.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRelationship()));

        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Tải dữ liệu từ CSDL
        loadResidentData();

        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        tableResidents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchResidents();
        });
    }

    private void loadResidentData() {
        masterData.clear();
        ArrayList<Object[]> results = nhanKhauDal.searchNhanKhau("NhanKhau", null, null);
        if (results != null) {
            for (Object[] row : results) {
                String id = (String) row[0];
                String householdId = (String) row[1];
                String name = (String) row[2];
                String identityCard = (String) row[3];
                String dob = (String) row[4];
                String placeOfBirth = (String) row[5];
                String ethnicity = (String) row[6];
                String occupation = (String) row[7];
                String relationship = (String) row[8];

                masterData.add(new Resident(id, householdId, name, identityCard, dob, placeOfBirth, ethnicity, occupation, relationship));
            }
        }
        tableResidents.setItems(masterData);
    }

    @FXML
    private void searchResidents() {
        String keyword = txtSearch.getText().trim();
        ArrayList<Object[]> results;

        if (keyword.isEmpty()) {
            results = nhanKhauDal.searchNhanKhau("NhanKhau", null, null);
        } else {
            if (keyword.matches("\\d+")) {
                results = nhanKhauDal.searchNhanKhau("NhanKhau", "MaNhanKhau", keyword);
                if (results == null || results.isEmpty()) {
                    results = nhanKhauDal.searchNhanKhau("NhanKhau", "HoTen", keyword);
                }
            } else {
                results = nhanKhauDal.searchNhanKhau("NhanKhau", "HoTen", keyword);
            }
        }

        masterData.clear();
        if (results != null) {
            for (Object[] row : results) {
                String id = (String) row[0];
                String householdId = (String) row[1];
                String name = (String) row[2];
                String identityCard = (String) row[3];
                String dob = (String) row[4];
                String placeOfBirth = (String) row[5];
                String ethnicity = (String) row[6];
                String occupation = (String) row[7];
                String relationship = (String) row[8];
                masterData.add(new Resident(id, householdId, name, identityCard, dob, placeOfBirth, ethnicity, occupation, relationship));
            }
        }
        tableResidents.setItems(masterData);
    }

    @FXML
    private void addResident() {
        Dialog<Pair<String, String[]>> dialog = createResidentDialog("Thêm cư dân", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String residentId = data.getKey();
            String[] details = data.getValue();
            String householdId = details[0];
            String name = details[1];
            String identityCard = details[2];
            String dob = details[3];
            String placeOfBirth = details[4];
            String ethnicity = details[5];
            String occupation = details[6];
            String relationship = details[7];

            String[] columns = {"MaNhanKhau","MaHoGiaDinh","HoTen","CCCD","NgaySinh","NoiSinh","DanToc","NgheNghiep","QuanHe"};
            String[] values = {residentId, householdId, name, identityCard, dob, placeOfBirth, ethnicity, occupation, relationship};

            boolean success = nhanKhauDal.insertNhanKhau(null, columns, values);
            if (success) {
                showAlert("Thêm thành công", "Cư dân mới đã được thêm!");
                loadResidentData();
            } else {
                showAlert("Lỗi", "Không thể thêm cư dân mới!");
            }
        });
    }

    @FXML
    private void editResident() {
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            Dialog<Pair<String, String[]>> dialog = createResidentDialog("Sửa cư dân", selectedResident);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String residentId = data.getKey();
                String[] details = data.getValue();
                String householdId = details[0];
                String name = details[1];
                String identityCard = details[2];
                String dob = details[3];
                String placeOfBirth = details[4];
                String ethnicity = details[5];
                String occupation = details[6];
                String relationship = details[7];

                boolean allSuccess = true;

                // Sử dụng try-catch cho các câu lệnh update vì updateNhanKhau ném ra SQLException
                try {
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "MaHoGiaDinh", householdId, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "HoTen", name, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "CCCD", identityCard, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "NgaySinh", dob, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "NoiSinh", placeOfBirth, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "DanToc", ethnicity, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "NgheNghiep", occupation, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("NhanKhau", "QuanHe", relationship, "MaNhanKhau", residentId);
                } catch (SQLException e) {
                    e.printStackTrace();
                    allSuccess = false;
                }

                if (allSuccess) {
                    showAlert("Sửa thành công", "Thông tin cư dân đã được cập nhật!");
                    loadResidentData();
                } else {
                    showAlert("Lỗi", "Không thể cập nhật thông tin cư dân!");
                }
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn cư dân cần sửa!");
        }
    }

    @FXML
    private void deleteResident() {
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xóa cư dân");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa cư dân này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác.");
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = nhanKhauDal.deleteNhanKhau("MaNhanKhau", selectedResident.getId());
                if (success) {
                    showAlert("Xóa thành công", "Cư dân đã được xóa khỏi hệ thống!");
                    loadResidentData();
                } else {
                    showAlert("Lỗi", "Không thể xóa cư dân!");
                }
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn cư dân cần xóa!");
        }
    }

    private Dialog<Pair<String, String[]>> createResidentDialog(String title, Resident residentData) {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType okButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtId = new TextField();
        TextField txtHouseholdId = new TextField();
        TextField txtName = new TextField();
        TextField txtIdentityCard = new TextField();
        TextField txtDob = new TextField();
        TextField txtPlaceOfBirth = new TextField();
        TextField txtEthnicity = new TextField();
        TextField txtOccupation = new TextField();
        TextField txtRelationship = new TextField();

        grid.add(new Label("Mã Nhân Khẩu:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Mã Hộ Gia Đình:"), 0, 1);
        grid.add(txtHouseholdId, 1, 1);
        grid.add(new Label("Họ và Tên:"), 0, 2);
        grid.add(txtName, 1, 2);
        grid.add(new Label("CCCD:"), 0, 3);
        grid.add(txtIdentityCard, 1, 3);
        grid.add(new Label("Ngày Sinh:"), 0, 4);
        grid.add(txtDob, 1, 4);
        grid.add(new Label("Nơi Sinh:"), 0, 5);
        grid.add(txtPlaceOfBirth, 1, 5);
        grid.add(new Label("Dân Tộc:"), 0, 6);
        grid.add(txtEthnicity, 1, 6);
        grid.add(new Label("Nghề Nghiệp:"), 0, 7);
        grid.add(txtOccupation, 1, 7);
        grid.add(new Label("Quan Hệ:"), 0, 8);
        grid.add(txtRelationship, 1, 8);

        if (residentData != null) {
            txtId.setText(residentData.getId());
            txtHouseholdId.setText(residentData.getHouseholdId());
            txtName.setText(residentData.getName());
            txtIdentityCard.setText(residentData.getIdentityCard());
            txtDob.setText(residentData.getDateOfBirth());
            txtPlaceOfBirth.setText(residentData.getPlaceOfBirth());
            txtEthnicity.setText(residentData.getEthnicity());
            txtOccupation.setText(residentData.getOccupation());
            txtRelationship.setText(residentData.getRelationship());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(txtId.getText(), new String[]{
                        txtHouseholdId.getText(),
                        txtName.getText(),
                        txtIdentityCard.getText(),
                        txtDob.getText(),
                        txtPlaceOfBirth.getText(),
                        txtEthnicity.getText(),
                        txtOccupation.getText(),
                        txtRelationship.getText()
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
    private void switchToAccount() {
        try {
            App.setRoot("account");
        } catch (IOException e) {
            System.out.println("Lỗi: Không thể chuyển sang màn hình tài khoản.");
            e.printStackTrace();
        }
    }
     @FXML
    private void switchToSignIn(){
        // Chuyển sang màn hình đăng nhập
    }
}
