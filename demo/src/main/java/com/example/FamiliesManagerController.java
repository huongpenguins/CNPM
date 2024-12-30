package com.example;

import com.example.dal.HoGiaDinhDAL;
import com.example.dal.NhanKhauDAL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FamiliesManagerController {

    @FXML
    private VBox sidebar;
    @FXML
    private Button menu, account, giadinh, dancu, khoanthu, canho, tamtru, tamvang, trangchu;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete, btnViewDetails;

    @FXML
    private TableView<Household> tableHouseholds;
    @FXML
    private TableColumn<Household, String> colHouseholdId;
    @FXML
    private TableColumn<Household, String> colApartmentId;
    @FXML
    private TableColumn<Household, String> colResidentId;
    @FXML
    private TableColumn<Household, String> colVehicleId;
    @FXML
    private TableColumn<Household, String> colIssueDate;
    @FXML
    private TableColumn<Household, String> colOwnerName;
    @FXML
    private TableColumn<Household, String> colPhoneNumber;

    private ObservableList<Household> masterData = FXCollections.observableArrayList();
    private HoGiaDinhDAL hoGiaDinhDal = new HoGiaDinhDAL(); // DAL cho HoGiaDinh
    private NhanKhauDAL nhanKhauDal = new NhanKhauDAL();    // Để xem chi tiết cư dân trong gia đình

    @FXML
    public void initialize() {
        // 1) Map các cột trong TableView đến các getter của Household
        //    (Đảm bảo class Household có getHouseholdId(), getApartmentId(), v.v.)
        colHouseholdId.setCellValueFactory(new PropertyValueFactory<>("householdId"));
        colApartmentId.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        colResidentId.setCellValueFactory(new PropertyValueFactory<>("residentId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // 2) Ẩn sidebar khi khởi chạy
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // 3) Tải dữ liệu ban đầu
        loadHouseholdData();

        // 4) Vô hiệu hóa Edit/Delete/View khi chưa chọn hàng
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnViewDetails.setDisable(true);

        tableHouseholds.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
            btnViewDetails.setDisable(!isSelected);
        });

        // 5) Khi gõ vào txtSearch thì tiến hành search
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchHouseholds();
        });
    }

    /**
     * Hàm load tất cả dữ liệu từ bảng hogiadinhtbl (nếu columnName=null, searchValue=null)
     */
    private void loadHouseholdData() {
        System.out.println("Bắt đầu tải dữ liệu...");
        masterData.clear();

        ArrayList<Object[]> results = hoGiaDinhDal.searchHoGiaDinh("hogiadinhtbl", null, null);

        if (results != null && !results.isEmpty()) {
            for (Object[] row : results) {
                try {
                    String householdId = row[0] != null ? row[0].toString() : "";
                    String apartmentId = row[1] != null ? row[1].toString() : "";
                    String residentId = row[2] != null ? row[2].toString() : "";
                    String vehicleId = row[3] != null ? row[3].toString() : "";
                    String issueDate = row[4] != null ? row[4].toString() : "";
                    String ownerName = row[5] != null ? row[5].toString() : "";
                    String phoneNumber = row[6] != null ? row[6].toString() : "";

                    Household household = new Household(
                            householdId, apartmentId, residentId,
                            vehicleId, issueDate, ownerName, phoneNumber
                    );
                    masterData.add(household);
                    System.out.println("Đã thêm hộ gia đình: " + householdId);
                } catch (Exception e) {
                    System.err.println("Lỗi khi xử lý dòng dữ liệu: " + e.getMessage());
                }
            }
        }

        System.out.println("Tổng số hộ gia đình đã tải: " + masterData.size());
        tableHouseholds.setItems(masterData);
    }

    /**
     * Tìm kiếm theo MaHoGiaDinh hoặc TenChuHo
     */
    @FXML
    private void searchHouseholds() {
        String keyword = txtSearch.getText().trim();
        ArrayList<Object[]> results;
        if (keyword.isEmpty()) {
            // Nếu user xóa trắng -> load all
            results = hoGiaDinhDal.searchHoGiaDinh("hogiadinhtbl", null, null);
        } else {
            // Tìm theo MaHoGiaDinh
            results = hoGiaDinhDal.searchHoGiaDinh("hogiadinhtbl", "MaHoGiaDinh", keyword);
            if (results == null || results.isEmpty()) {
                // Nếu không có => tìm theo TenChuHo
                results = hoGiaDinhDal.searchHoGiaDinh("hogiadinhtbl", "TenChuHo", keyword);
            }
        }

        masterData.clear();
        if (results != null) {
            for (Object[] row : results) {
                String householdId = (String) row[0];
                String apartmentId = (String) row[1];
                String residentId  = (String) row[2];
                String vehicleId   = (String) row[3];
                String issueDate   = String.valueOf(row[4]);
                String ownerName   = (String) row[5];
                String phoneNumber = String.valueOf(row[6]);

                masterData.add(new Household(householdId, apartmentId, residentId, vehicleId,
                        issueDate, ownerName, phoneNumber));
            }
        }
        tableHouseholds.setItems(masterData);
    }

    /**
     * Thêm 1 hộ gia đình
     */
    @FXML
    private void addHousehold() {
        Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Thêm hộ khẩu", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String householdId = data.getKey();
            String[] details   = data.getValue();
            String apartmentId = details[0];
            String residentId  = details[1];
            String vehicleId   = details[2];
            String issueDate   = details[3];
            String ownerName   = details[4];
            String phoneNumber = details[5];

            // DB cột: MaHoGiaDinh, MaCanHo, MaNhanKhau, MaXe, NgayCap, TenChuHo, SDT
            String[] columns = {
                    "MaHoGiaDinh", "MaCanHo", "MaNhanKhau", "MaXe", "NgayCap", "TenChuHo", "SDT"
            };
            String[] values = {
                    householdId, apartmentId, residentId, vehicleId, issueDate, ownerName, phoneNumber
            };

            boolean success = hoGiaDinhDal.insertHoGiaDinh("hogiadinhtbl", columns, values);
            if (success) {
                showAlert("Thành công", "Hộ khẩu mới đã được thêm!");
                loadHouseholdData();
            } else {
                showAlert("Lỗi", "Không thể thêm hộ khẩu!");
            }
        });
    }

    /**
     * Sửa thông tin hộ gia đình
     */
    @FXML
    private void editHousehold() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Sửa hộ khẩu", selected);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String householdId = data.getKey();
                String[] details   = data.getValue();
                String apartmentId = details[0];
                String residentId  = details[1];
                String vehicleId   = details[2];
                String issueDate   = details[3];
                String ownerName   = details[4];
                String phoneNumber = details[5];

                boolean allSuccess = true;
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","MaCanHo",   apartmentId,"MaHoGiaDinh",householdId);
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","MaNhanKhau",residentId, "MaHoGiaDinh",householdId);
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","MaXe",      vehicleId,  "MaHoGiaDinh",householdId);
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","NgayCap",   issueDate,  "MaHoGiaDinh",householdId);
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","TenChuHo",  ownerName,  "MaHoGiaDinh",householdId);
                allSuccess &= hoGiaDinhDal.updateHoGiaDinh("hogiadinhtbl","SDT",       phoneNumber,"MaHoGiaDinh",householdId);

                if (allSuccess) {
                    showAlert("Sửa thành công", "Thông tin hộ khẩu đã được cập nhật!");
                    loadHouseholdData();
                } else {
                    showAlert("Lỗi", "Không thể cập nhật hộ khẩu!");
                }
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần sửa!");
        }
    }

    /**
     * Xóa hộ gia đình
     */
    @FXML
    private void deleteHousehold() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xóa hộ khẩu");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa hộ khẩu này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = hoGiaDinhDal.deleteHoGiaDinh("hogiadinhtbl", "MaHoGiaDinh", selected.getHouseholdId());
                if (success) {
                    showAlert("Xóa thành công", "Hộ khẩu đã được xóa khỏi hệ thống!");
                    loadHouseholdData();
                } else {
                    showAlert("Lỗi", "Không thể xóa hộ khẩu!");
                }
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần xóa!");
        }
    }

    /**
     * Xem chi tiết các thành viên trong hộ gia đình
     */
    @FXML
    private void viewHouseholdDetails() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String householdId = selected.getHouseholdId();

            // Lấy danh sách cư dân (nhankhautbl) có MaHoGiaDinh = householdId
            ArrayList<Object[]> results = nhanKhauDal.searchNhanKhau("nhankhautbl", "MaHoGiaDinh", householdId);
            if (results == null || results.isEmpty()) {
                showAlert("Thông báo", "Không có thành viên nào trong hộ gia đình này!");
                return;
            }
            List<Resident> familyMembers = new ArrayList<>();
            for (Object[] row : results) {
                String id           = (String) row[0];
                String hoGiaDinh    = (String) row[1];
                String name         = (String) row[2];
                String identityCard = (String) row[3];
                String dob          = String.valueOf(row[4]);
                String placeOfBirth = (String) row[5];
                String ethnicity    = (String) row[6];
                String occupation   = (String) row[7];
                String relationship = (String) row[8];

                familyMembers.add(new Resident(
                        id, hoGiaDinh, name, identityCard, dob, placeOfBirth,
                        ethnicity, occupation, relationship
                ));
            }

            // Hiển thị dialog chi tiết
            Dialog<Void> detailDialog = new Dialog<>();
            detailDialog.setTitle("Chi tiết hộ gia đình - Mã: " + householdId);

            ButtonType closeButtonType = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
            detailDialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

            TableView<Resident> memberTable = new TableView<>();
            TableColumn<Resident, String> colMemberId       = new TableColumn<>("Mã Nhân Khẩu");
            TableColumn<Resident, String> colMemberName     = new TableColumn<>("Họ và Tên");
            TableColumn<Resident, String> colMemberRelation = new TableColumn<>("Quan Hệ");

            colMemberId.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getId()));
            colMemberName.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
            colMemberRelation.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getRelationship()));

            memberTable.getColumns().addAll(colMemberId, colMemberName, colMemberRelation);
            memberTable.setItems(FXCollections.observableArrayList(familyMembers));

            detailDialog.getDialogPane().setContent(memberTable);
            detailDialog.showAndWait();
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu để xem chi tiết!");
        }
    }

    /**
     * Tạo dialog (Form) nhập/ sửa dữ liệu Household
     */
    private Dialog<Pair<String, String[]>> createHouseholdDialog(String title, Household householdData) {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType okButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtHouseholdId = new TextField();
        TextField txtApartmentId = new TextField();
        TextField txtResidentId  = new TextField();
        TextField txtVehicleId   = new TextField();
        TextField txtIssueDate   = new TextField();
        TextField txtOwnerName   = new TextField();
        TextField txtPhoneNumber = new TextField();

        grid.add(new Label("Mã Hộ Gia Đình:"), 0, 0);
        grid.add(txtHouseholdId, 1, 0);
        grid.add(new Label("Mã Căn Hộ:"), 0, 1);
        grid.add(txtApartmentId, 1, 1);
        grid.add(new Label("Mã Nhân Khẩu:"), 0, 2);
        grid.add(txtResidentId, 1, 2);
        grid.add(new Label("Mã Xe:"), 0, 3);
        grid.add(txtVehicleId, 1, 3);
        grid.add(new Label("Ngày Cấp:"), 0, 4);
        grid.add(txtIssueDate, 1, 4);
        grid.add(new Label("Tên Chủ Hộ:"), 0, 5);
        grid.add(txtOwnerName, 1, 5);
        grid.add(new Label("Số Điện Thoại:"), 0, 6);
        grid.add(txtPhoneNumber, 1, 6);

        if (householdData != null) {
            txtHouseholdId.setText(householdData.getHouseholdId());
            txtApartmentId.setText(householdData.getApartmentId());
            txtResidentId.setText(householdData.getResidentId());
            txtVehicleId.setText(householdData.getVehicleId());
            txtIssueDate.setText(householdData.getIssueDate());
            txtOwnerName.setText(householdData.getOwnerName());
            txtPhoneNumber.setText(householdData.getPhoneNumber());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Key = householdId
                // Value = [apartmentId, residentId, vehicleId, issueDate, ownerName, phoneNumber]
                return new Pair<>(txtHouseholdId.getText(), new String[]{
                        txtApartmentId.getText(),
                        txtResidentId.getText(),
                        txtVehicleId.getText(),
                        txtIssueDate.getText(),
                        txtOwnerName.getText(),
                        txtPhoneNumber.getText()
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

    //region Chuyển màn hình
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
        if(alert.getResult() == ButtonType.OK){
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
    //endregion
}
