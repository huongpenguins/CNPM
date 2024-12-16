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
import java.util.Optional;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

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
    private FilteredList<Resident> filteredData;

    public void initialize() {
        // Thiết lập cellValueFactory cho các cột theo các trường mới
        colResidentId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colHouseholdId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHouseholdId()));
        colResidentName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colIdentityCard.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdentityCard()));
        colDateOfBirth.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateOfBirth()));
        colPlaceOfBirth.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlaceOfBirth()));
        colEthnicity.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEthnicity()));
        colOccupation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOccupation()));
        colRelationship.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRelationship()));

        // Ẩn sidebar khi khởi động
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Tải dữ liệu
        loadResidentData();

        // Ban đầu disable nút Sửa, Xóa
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        // Theo dõi lựa chọn trên bảng
        tableResidents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });

        // Tìm kiếm khi thay đổi txtSearch
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterResidents(newValue);
        });

        // Nút thêm, sửa, xóa
        btnAdd.setOnAction(event -> addResident());
        btnEdit.setOnAction(event -> editResident());
        btnDelete.setOnAction(event -> deleteResident());
    }

    private void loadResidentData() {
        masterData.clear();
        try {
            // SQL: Lấy dữ liệu từ CSDL
            // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "username", "password");
            // String sql = "SELECT id, household_id, name, identity_card, date_of_birth, place_of_birth, ethnicity, occupation, relationship FROM residents";
            // PreparedStatement pstmt = conn.prepareStatement(sql);
            // ResultSet rs = pstmt.executeQuery();
            // while (rs.next()) {
            //     masterData.add(new Resident(
            //         rs.getString("id"),
            //         rs.getString("household_id"),
            //         rs.getString("name"),
            //         rs.getString("identity_card"),
            //         rs.getString("date_of_birth"),
            //         rs.getString("place_of_birth"),
            //         rs.getString("ethnicity"),
            //         rs.getString("occupation"),
            //         rs.getString("relationship")
            //     ));
            // }
            // conn.close();

            // Dữ liệu giả lập
            masterData.addAll(
                    new Resident("NK001", "HK001", "Nguyễn Văn A", "123456789012", "01/01/1990", "Hà Nội", "Kinh", "Công nhân", "Chủ hộ"),
                    new Resident("NK002", "HK001", "Nguyễn Thị B", "123456789013", "15/05/1992", "Hà Nội", "Kinh", "Giáo viên", "Vợ"),
                    new Resident("NK003", "HK002", "Trần Văn C", "123456789014", "22/12/1985", "TP.HCM", "Kinh", "Kỹ sư", "Chủ hộ")
            );

            filteredData = new FilteredList<>(masterData, p -> true);
            tableResidents.setItems(filteredData);

        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    @FXML
    private void searchResidents() {
        filterResidents(txtSearch.getText());
    }

    private void filterResidents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            String lowerCaseKeyword = keyword.toLowerCase();
            filteredData.setPredicate(resident ->
                    resident.getId().toLowerCase().contains(lowerCaseKeyword) ||
                            resident.getName().toLowerCase().contains(lowerCaseKeyword)
            );
        }
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

            System.out.println("Thêm cư dân: Mã = " + residentId + ", Hộ gia đình = " + householdId + ", Tên = " + name +
                    ", CCCD = " + identityCard + ", Ngày sinh = " + dob + ", Nơi sinh = " + placeOfBirth +
                    ", Dân tộc = " + ethnicity + ", Nghề nghiệp = " + occupation + ", Quan hệ = " + relationship);
            // SQL xử lý: INSERT INTO residents (...)
            showAlert("Thêm thành công", "Cư dân mới đã được thêm!");
            // loadResidentData();
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

                System.out.println("Cập nhật cư dân: Mã = " + residentId + ", Hộ gia đình = " + householdId + ", Tên = " + name +
                        ", CCCD = " + identityCard + ", Ngày sinh = " + dob + ", Nơi sinh = " + placeOfBirth +
                        ", Dân tộc = " + ethnicity + ", Nghề nghiệp = " + occupation + ", Quan hệ = " + relationship);
                // SQL xử lý: UPDATE residents SET ...
                showAlert("Sửa thành công", "Thông tin cư dân đã được cập nhật!");
                // loadResidentData();
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
                System.out.println("Xóa cư dân: " + selectedResident.getId());
                // SQL xử lý: DELETE FROM residents WHERE id = ?
                showAlert("Xóa thành công", "Cư dân đã được xóa khỏi hệ thống!");
                // loadResidentData();
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

        // Thêm label và textfield vào dialog
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

        // Nếu là sửa, điền dữ liệu cũ
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
