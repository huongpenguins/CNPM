package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

public class FamiliesManagerController  {

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
    private FilteredList<Household> filteredData;

    public void initialize() {
        // Đặt sidebar ra ngoài màn hình khi khởi tạo
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Thiết lập cellValueFactory
        colHouseholdId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHouseholdId()));
        colApartmentId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApartmentId()));
        colResidentId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getResidentId()));
        colVehicleId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicleId()));
        colIssueDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIssueDate()));
        colOwnerName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOwnerName()));
        colPhoneNumber.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        loadHouseholdData();

        // Vô hiệu hóa Sửa, Xóa, Xem chi tiết khi chưa chọn
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnViewDetails.setDisable(true);

        tableHouseholds.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
            btnViewDetails.setDisable(!isSelected);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHouseholds(newValue);
        });

        // Gắn sự kiện cho các nút (nếu chưa có trong FXML)
        btnSearch.setOnAction(event -> searchHouseholds());
        btnAdd.setOnAction(event -> addHousehold());
        btnEdit.setOnAction(event -> editHousehold());
        btnDelete.setOnAction(event -> deleteHousehold());
        btnViewDetails.setOnAction(event -> viewHouseholdDetails());
    }

    private void loadHouseholdData() {
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
                    new Household("HK001", "CH001", "NK001", "XE001", "01/01/2020", "Nguyễn Văn D", "0123456789"),
                    new Household("HK002", "CH002", "NK002", "XE002", "15/05/2021", "Trần Thị E", "0987654321"),
                    new Household("HK003", "CH003", "NK003", "XE003", "22/12/2022", "Lê Văn F", "0934567890")
            );

            filteredData = new FilteredList<>(masterData, p -> true);
            tableHouseholds.setItems(filteredData);

        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    @FXML
    private void searchHouseholds() {
        filterHouseholds(txtSearch.getText());
    }

    private void filterHouseholds(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            String lowerCaseKeyword = keyword.toLowerCase();
            filteredData.setPredicate(hh ->
                    hh.getHouseholdId().toLowerCase().contains(lowerCaseKeyword) ||
                            hh.getOwnerName().toLowerCase().contains(lowerCaseKeyword)
            );
        }
    }

    @FXML
    private void addHousehold() {
        Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Thêm hộ khẩu", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String householdId = data.getKey();
            String[] details = data.getValue();
            String apartmentId = details[0];
            String residentId = details[1];
            String vehicleId = details[2];
            String issueDate = details[3];
            String ownerName = details[4];
            String phoneNumber = details[5];

            System.out.println("Thêm hộ khẩu: " + householdId + ", " + apartmentId + ", " + residentId + ", " + vehicleId + ", " + issueDate + ", " + ownerName + ", " + phoneNumber);
            // SQL xử lý: INSERT INTO households VALUES(...)

            showAlert("Thành công", "Hộ khẩu mới đã được thêm!");
            // loadHouseholdData();
        });
    }

    @FXML
    private void editHousehold() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Sửa hộ khẩu", selected);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String householdId = data.getKey();
                String[] details = data.getValue();
                String apartmentId = details[0];
                String residentId = details[1];
                String vehicleId = details[2];
                String issueDate = details[3];
                String ownerName = details[4];
                String phoneNumber = details[5];

                System.out.println("Cập nhật hộ khẩu: " + householdId + ", " + apartmentId + ", " + residentId + ", " + vehicleId + ", " + issueDate + ", " + ownerName + ", " + phoneNumber);
                // SQL xử lý: UPDATE households SET ... WHERE household_id = ?
                showAlert("Sửa thành công", "Thông tin hộ khẩu đã được cập nhật!");
                // loadHouseholdData();
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần sửa!");
        }
    }

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
                System.out.println("Xóa hộ khẩu: " + selected.getHouseholdId());
                // SQL xử lý: DELETE FROM households WHERE household_id = ?
                showAlert("Xóa thành công", "Hộ khẩu đã được xóa khỏi hệ thống!");
                // loadHouseholdData();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần xóa!");
        }
    }

    // Xem chi tiết: hiển thị các thành viên cùng Mã Hộ Gia Đình
    @FXML
    private void viewHouseholdDetails() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String householdId = selected.getHouseholdId();

            // SQL xử lý: SELECT * FROM residents WHERE household_id = householdId
            // Kết quả là danh sách Resident
            // Dưới đây là dữ liệu giả lập
            List<Resident> familyMembers = new ArrayList<>();
            // Giả lập một số cư dân cùng householdId
            familyMembers.add(new Resident("NK001", householdId, "Nguyễn Văn A", "123456789012", "01/01/1990", "Hà Nội", "Kinh", "Công nhân", "Chủ hộ"));
            familyMembers.add(new Resident("NK002", householdId, "Nguyễn Thị B", "123456789013", "15/05/1992", "Hà Nội", "Kinh", "Giáo viên", "Vợ"));

            // Tạo một dialog để hiển thị danh sách thành viên
            Dialog<Void> detailDialog = new Dialog<>();
            detailDialog.setTitle("Chi tiết hộ gia đình - Mã: " + householdId);

            ButtonType closeButtonType = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
            detailDialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

            TableView<Resident> memberTable = new TableView<>();
            TableColumn<Resident, String> colMemberId = new TableColumn<>("Mã Nhân Khẩu");
            TableColumn<Resident, String> colMemberName = new TableColumn<>("Họ và Tên");
            TableColumn<Resident, String> colMemberRelation = new TableColumn<>("Quan Hệ");

            colMemberId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
            colMemberName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
            colMemberRelation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRelationship()));

            memberTable.getColumns().addAll(colMemberId, colMemberName, colMemberRelation);
            memberTable.setItems(FXCollections.observableArrayList(familyMembers));

            detailDialog.getDialogPane().setContent(memberTable);
            detailDialog.showAndWait();

        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu để xem chi tiết!");
        }
    }

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
        TextField txtResidentId = new TextField();
        TextField txtVehicleId = new TextField();
        TextField txtIssueDate = new TextField();
        TextField txtOwnerName = new TextField();
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
