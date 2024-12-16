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
    private TableColumn<Household, String> colHouseholdName;
    @FXML
    private TableColumn<Household, String> colAddress;

    private ObservableList<Household> masterData = FXCollections.observableArrayList();
    private FilteredList<Household> filteredData;

    public void initialize() {
        // Đặt sidebar ra ngoài màn hình khi khởi chạy
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // Thiết lập cellValueFactory cho các cột
        colHouseholdId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colHouseholdName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOwnerName()));
        colAddress.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));

        // Tải dữ liệu hộ khẩu
        loadHouseholdData();

        // Ban đầu vô hiệu hóa nút Sửa, Xóa và Xem chi tiết khi chưa chọn hàng
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnViewDetails.setDisable(true);

        // Theo dõi sự thay đổi lựa chọn trên bảng
        tableHouseholds.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
            btnViewDetails.setDisable(!isSelected);
        });

        // Lọc dữ liệu theo giá trị ô tìm kiếm
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHouseholds(newValue);
        });

        // Gắn sự kiện cho các nút
        btnSearch.setOnAction(event -> searchHouseholds());
        btnAdd.setOnAction(event -> addHousehold());
        btnEdit.setOnAction(event -> editHousehold());
        btnDelete.setOnAction(event -> deleteHousehold());
        btnViewDetails.setOnAction(event -> viewHouseholdDetails());
    }

    private void loadHouseholdData() {
        masterData.clear();
        try {
            // SQL xử lý: Kết nối CSDL và truy vấn danh sách hộ khẩu
            // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "user", "pass");
            // PreparedStatement pstmt = conn.prepareStatement("SELECT household_id, owner_name, address FROM households");
            // ResultSet rs = pstmt.executeQuery();
            // while(rs.next()) {
            //     masterData.add(new Household(
            //         rs.getString("household_id"),
            //         rs.getString("owner_name"),
            //         rs.getString("address")
            //     ));
            // }
            // conn.close();

            // Dữ liệu giả lập
            masterData.addAll(
                    new Household("HK001", "Nguyễn Văn D", "Hà Nội"),
                    new Household("HK002", "Trần Thị E", "TP.HCM"),
                    new Household("HK003", "Lê Văn F", "Đà Nẵng")
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
                    hh.getId().toLowerCase().contains(lowerCaseKeyword) ||
                            hh.getOwnerName().toLowerCase().contains(lowerCaseKeyword)
            );
        }
    }

    // Thêm hộ khẩu
    @FXML
    private void addHousehold() {
        Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Thêm hộ khẩu", null);
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String householdId = data.getKey();
            String[] details = data.getValue();
            String ownerName = details[0];
            String address = details[1];

            System.out.println("Thêm hộ khẩu: Mã = " + householdId + ", Chủ hộ = " + ownerName + ", Địa chỉ = " + address);
            // SQL xử lý: INSERT INTO households (household_id, owner_name, address) VALUES (?, ?, ?)

            showAlert("Thêm thành công", "Hộ khẩu mới đã được thêm!");
            // loadHouseholdData();
        });
    }

    // Sửa hộ khẩu
    @FXML
    private void editHousehold() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Pair<String, String[]>> dialog = createHouseholdDialog("Sửa hộ khẩu", selected);
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String householdId = data.getKey();
                String[] details = data.getValue();
                String ownerName = details[0];
                String address = details[1];

                System.out.println("Cập nhật hộ khẩu: Mã = " + householdId + ", Chủ hộ = " + ownerName + ", Địa chỉ = " + address);
                // SQL xử lý: UPDATE households SET owner_name=?, address=? WHERE household_id=?
                showAlert("Sửa thành công", "Thông tin hộ khẩu đã được cập nhật!");
                // loadHouseholdData();
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần sửa!");
        }
    }

    // Xóa hộ khẩu
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
                System.out.println("Xóa hộ khẩu: " + selected.getId());
                // SQL xử lý: DELETE FROM households WHERE household_id=?
                showAlert("Xóa thành công", "Hộ khẩu đã được xóa khỏi hệ thống!");
                // loadHouseholdData();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn hộ khẩu cần xóa!");
        }
    }

    // Xem chi tiết hộ khẩu
    // Tạm thời chỉ hiển thị thông tin hộ khẩu trong 1 dialog.
    // Sau này, nếu muốn hiển thị thành viên, cần bổ sung logic gọi SQL lấy danh sách cư dân theo household_id.
    @FXML
    private void viewHouseholdDetails() {
        Household selected = tableHouseholds.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Hiển thị dialog chi tiết
            Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
            detailAlert.setTitle("Chi tiết hộ khẩu");
            detailAlert.setHeaderText("Mã: " + selected.getId());
            detailAlert.setContentText("Chủ hộ: " + selected.getOwnerName() + "\nĐịa chỉ: " + selected.getAddress());
            // Trong tương lai, thêm code SQL:
            // // SQL xử lý: SELECT * FROM residents WHERE household_id = selected.getId()
            // và hiển thị danh sách cư dân...
            detailAlert.showAndWait();
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

        TextField txtId = new TextField();
        TextField txtOwnerName = new TextField();
        TextField txtAddress = new TextField();

        grid.add(new Label("Mã hộ khẩu:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Tên chủ hộ:"), 0, 1);
        grid.add(txtOwnerName, 1, 1);
        grid.add(new Label("Địa chỉ:"), 0, 2);
        grid.add(txtAddress, 1, 2);

        if (householdData != null) {
            txtId.setText(householdData.getId());
            txtOwnerName.setText(householdData.getOwnerName());
            txtAddress.setText(householdData.getAddress());
            // Nếu không cho sửa ID, có thể disable:
            // txtId.setDisable(true);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(txtId.getText(), new String[]{
                        txtOwnerName.getText(),
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
        // chuyển về màn hình đăng nhập
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
