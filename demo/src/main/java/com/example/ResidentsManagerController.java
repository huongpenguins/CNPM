package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResidentsManagerController {

    @FXML
    private TableView<Resident> tableResidents;
    @FXML
    private TableColumn<Resident, String> colResidentId;
    @FXML
    private TableColumn<Resident, String> colResidentName;
    @FXML
    private TableColumn<Resident, String> colDateOfBirth;
    @FXML
    private TableColumn<Resident, String> colPhone;
    @FXML
    private TableColumn<Resident, String> colAddress;

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
        // Thiết lập cellValueFactory với PropertyValueFactory
        colResidentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colResidentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Sidebar ẩn khi khởi tạo
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Tải dữ liệu
        loadResidentData();

        // Disable nút Sửa và Xóa ban đầu
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        // Theo dõi lựa chọn
        tableResidents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });

        // Gắn listener cho ô tìm kiếm (khi gõ tự động lọc)
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterResidents(newValue);
        });
    }

    private void loadResidentData() {
        masterData.clear();
        try {
            // Chỗ này dành cho kết nối CSDL, hiện để comment
            // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "username", "password");
            // PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, dob, phone, address FROM residents");
            // ResultSet rs = pstmt.executeQuery();
            // while(rs.next()) {
            //     masterData.add(new Resident(
            //       rs.getString("id"),
            //       rs.getString("name"),
            //       rs.getString("dob"),
            //       rs.getString("phone"),
            //       rs.getString("address")
            //     ));
            // }
            // conn.close();

            // Dữ liệu giả lập
            masterData.addAll(
                    new Resident("001", "Nguyễn Văn A", "01/01/1990", "0123456789", "Hà Nội"),
                    new Resident("002", "Trần Thị B", "15/05/1985", "0987654321", "TP.HCM"),
                    new Resident("003", "Lê Văn C", "22/12/1992", "0934567890", "Đà Nẵng")
            );

            filteredData = new FilteredList<>(masterData, p -> true);
            tableResidents.setItems(filteredData);

        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    // Hàm searchResidents gọi khi bấm nút (lấy text từ txtSearch):
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
            System.out.println("Thêm cư dân: Mã = " + residentId + ", Tên = " + details[0] +
                    ", Ngày sinh = " + details[1] + ", Số phone = " + details[2] + ", Địa chỉ = " + details[3]);
            // SQL: INSERT ...
            showAlert("Thêm thành công", "Cư dân mới đã được thêm!");
            // Sau khi thêm xong, reload dữ liệu
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
                System.out.println("Cập nhật cư dân: Mã = " + residentId + ", Tên = " + details[0] +
                        ", Ngày sinh = " + details[1] + ", Số phone = " + details[2] + ", Địa chỉ = " + details[3]);
                // SQL: UPDATE ...
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
                // SQL: DELETE ...
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
        TextField txtName = new TextField();
        TextField txtDob = new TextField();
        TextField txtPhone = new TextField();
        TextField txtAddress = new TextField();

        grid.add(new Label("Mã cư dân:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Họ và Tên:"), 0, 1);
        grid.add(txtName, 1, 1);
        grid.add(new Label("Ngày sinh:"), 0, 2);
        grid.add(txtDob, 1, 2);
        grid.add(new Label("Số điện thoại:"), 0, 3);
        grid.add(txtPhone, 1, 3);
        grid.add(new Label("Địa chỉ:"), 0, 4);
        grid.add(txtAddress, 1, 4);

        if (residentData != null) {
            txtId.setText(residentData.getId());
            txtName.setText(residentData.getName());
            txtDob.setText(residentData.getDateOfBirth());
            txtPhone.setText(residentData.getPhone());
            txtAddress.setText(residentData.getAddress());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(txtId.getText(), new String[]{
                        txtName.getText(),
                        txtDob.getText(),
                        txtPhone.getText(),
                        txtAddress.getText()
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
