package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.util.Pair;
import java.util.Optional;

public class ResidentsManagerController {
    @FXML
    private TableView<Resident> tableResidents; // Bảng cư dân
    @FXML
    private Button btnAdd, btnEdit, btnDelete; // Nút Thêm, Sửa, Xóa
    @FXML
    private TextField txtSearch; // Ô tìm kiếm

    public void initialize() {
        // Đặt sidebar ra ngoài màn hình khi khởi chạy
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }
        // 1. Tải dữ liệu cư dân vào bảng
        loadResidentData();

        // 2. Thiết lập trạng thái ban đầu của các nút (vô hiệu hóa Sửa và Xóa)
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        // 3. Gắn sự kiện để theo dõi lựa chọn trong bảng
        tableResidents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });

        // 4. Gắn sự kiện tìm kiếm theo từ khóa nhập vào
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchResidents(newValue);
        });

        // 5. Gắn sự kiện cho các nút Thêm, Sửa và Xóa
        btnAdd.setOnAction(event -> addResident());
        btnEdit.setOnAction(event -> editResident());
        btnDelete.setOnAction(event -> deleteResident());

    }

    private ObservableList<Resident> masterData = FXCollections.observableArrayList();
    private FilteredList<Resident> filteredData;
    private void loadResidentData() {
        masterData.clear(); // Xóa dữ liệu hiện tại
        try {
            // SQL xử lý: Kết nối đến cơ sở dữ liệu và lấy dữ liệu
            // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "username", "password");
            // String sql = "SELECT id, name, dob, phone, address FROM residents";
            // PreparedStatement pstmt = conn.prepareStatement(sql);
            // ResultSet rs = pstmt.executeQuery();

            // SQL xử lý: Lấy dữ liệu từ ResultSet và thêm vào masterData
            // while (rs.next()) {
            //     masterData.add(new Resident(
            //         rs.getString("id"),
            //         rs.getString("name"),
            //         rs.getString("dob"),
            //         rs.getString("phone"),
            //         rs.getString("address")
            //     ));
            // }

            // Giả lập dữ liệu tĩnh nếu không sử dụng SQL
            masterData.addAll(
                    new Resident("001", "Nguyễn Văn A", "01/01/1990", "0123456789", "Hà Nội"),
                    new Resident("002", "Trần Thị B", "15/05/1985", "0987654321", "TP.HCM"),
                    new Resident("003", "Lê Văn C", "22/12/1992", "0934567890", "Đà Nẵng")
            );

            // conn.close();

            // Khởi tạo FilteredList từ masterData
            filteredData = new FilteredList<>(masterData, p -> true);

            // Gán dữ liệu đã lọc vào bảng
            tableResidents.setItems(filteredData);

        } catch (Exception e) {
            // SQL xử lý: Báo lỗi khi không thể kết nối hoặc truy vấn
            // e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }


    // Hàm thêm cư dân mới
    @FXML
    private void addResident() {
        Dialog<Pair<String, String[]>> dialog = createResidentDialog("Thêm cư dân", null);

        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String residentId = data.getKey();
            String[] details = data.getValue();
            System.out.println("Thêm cư dân: Mã = " + residentId + ", Tên = " + details[0] +
                    ", Ngày sinh = " + details[1] + ", Số phone = " + details[2] + ", Địa chỉ = " + details[3]);
            // SQL xử lý: INSERT INTO residents VALUES (residentId, details[0], details[1], details[2], details[3]);
            showAlert("Thêm thành công", "Cư dân mới đã được thêm!");
        });
    }

    // Hàm sửa thông tin cư dân
    @FXML
    private void editResident() {
        Object selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            Dialog<Pair<String, String[]>> dialog = createResidentDialog("Sửa cư dân", selectedResident);

            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String residentId = data.getKey();
                String[] details = data.getValue();
                System.out.println("Cập nhật cư dân: Mã = " + residentId + ", Tên = " + details[0] +
                        ", Ngày sinh = " + details[1] + ", Số phone = " + details[2] + ", Địa chỉ = " + details[3]);
                // SQL xử lý: UPDATE residents SET name = details[0], dob = details[1], phone = details[2], address = details[3] WHERE id = residentId;
                showAlert("Sửa thành công", "Thông tin cư dân đã được cập nhật!");
            });
        } else {
            showAlert("Lỗi", "Vui lòng chọn cư dân cần sửa!");
        }
    }

    // Hàm xóa cư dân
    @FXML
    private void deleteResident() {
        Object selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xóa cư dân");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa cư dân này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Xóa cư dân: " + selectedResident.toString());
                // SQL xử lý: DELETE FROM residents WHERE id = selectedResidentId;
                showAlert("Xóa thành công", "Cư dân đã được xóa khỏi hệ thống!");
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn cư dân cần xóa!");
        }
    }

    // Tạo dialog cho việc thêm/sửa thông tin cư dân
    private Dialog<Pair<String, String[]>> createResidentDialog(String title, Object residentData) {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType okButtonType = new ButtonType("Lưu", ButtonData.OK_DONE);
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

        dialog.getDialogPane().setContent(grid);

        // Điền dữ liệu hiện tại nếu là sửa
        if (residentData != null) {
            txtId.setText("123ABC"); // Chỗ này thay thế bằng dữ liệu residentData
            txtName.setText("Nguyễn Văn A");
            txtDob.setText("01/01/1990");
            txtPhone.setText("0123456789");
            txtAddress.setText("Hà Nội");
        }

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

    private void searchResidents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Nếu không có từ khóa, hiển thị toàn bộ dữ liệu
            filteredData.setPredicate(p -> true);
        } else {
            String lowerCaseKeyword = keyword.toLowerCase();

            // Lọc dữ liệu dựa trên Mã cư dân hoặc Họ tên
            filteredData.setPredicate(resident ->
                    resident.getId().toLowerCase().contains(lowerCaseKeyword) ||
                            resident.getName().toLowerCase().contains(lowerCaseKeyword)
            );
        }
    }




    // Hàm hiển thị thông báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    VBox sidebar;
    @FXML
    Button menu,account,giadinh,dancu,khoanthu,canho,tamtru,tamvang,trangchu;

    @FXML
    private void menuClick() {
        // Kiểm tra nếu sidebar đã được khởi tạo
        if (sidebar != null) {
            double currentPosition = sidebar.getLayoutX();
            double sidebarWidth = sidebar.getPrefWidth();

            // Nếu sidebar hiện tại ở ngoài màn hình -> kéo vào
            if (currentPosition < 0) {
                sidebar.setLayoutX(0);
            } else {
                // Nếu sidebar đã hiển thị -> đẩy ra khỏi màn hình
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
        alert.setContentText("Bạn muốn đăng xuất");
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

    }
    @FXML
    private void switchToAccount() {
        try {
            App.setRoot("account"); // Đảm bảo "account.fxml" tồn tại
        } catch (IOException e) {
            System.out.println("Lỗi: Không thể chuyển sang màn hình tài khoản. Kiểm tra đường dẫn của account.fxml.");
            e.printStackTrace();
        }
    }
}
