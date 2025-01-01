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

/**
 * Controller cho màn hình Quản lí Nhân Khẩu (Residents).
 * - Có các chức năng: Tải dữ liệu (loadResidentData), Thêm, Sửa, Xóa, Tìm kiếm.
 * - Giao diện tương tự như FamiliesManagerController, chỉ khác dữ liệu nhân khẩu.
 */
public class ResidentsManagerController {

    //region FXML components
    @FXML
    private TableView<Resident> tableResidents; // Bảng hiển thị danh sách nhân khẩu

    // Các cột tương ứng với trường trong lớp Resident
    @FXML
    private TableColumn<Resident, String> colResidentId;       // Mã Nhân Khẩu
    @FXML
    private TableColumn<Resident, String> colHouseholdId;      // Mã Hộ Gia Đình
    @FXML
    private TableColumn<Resident, String> colResidentName;     // Họ Tên
    @FXML
    private TableColumn<Resident, String> colIdentityCard;     // CCCD
    @FXML
    private TableColumn<Resident, String> colDateOfBirth;      // Ngày Sinh
    @FXML
    private TableColumn<Resident, String> colPlaceOfBirth;     // Nơi Sinh
    @FXML
    private TableColumn<Resident, String> colEthnicity;        // Dân Tộc
    @FXML
    private TableColumn<Resident, String> colOccupation;       // Nghề Nghiệp
    @FXML
    private TableColumn<Resident, String> colRelationship;     // Quan Hệ

    @FXML
    private Button btnAdd, btnEdit, btnDelete; // Các nút Thêm, Sửa, Xóa
    @FXML
    private TextField txtSearch;               // Ô tìm kiếm
    @FXML
    private VBox sidebar;                     // Thanh sidebar
    @FXML
    private Button menu, account, giadinh, dancu, khoanthu, canho, tamtru, tamvang, trangchu;
    //endregion

    //region Khai báo biến
    private ObservableList<Resident> masterData = FXCollections.observableArrayList();
    private NhanKhauDAL nhanKhauDal = new NhanKhauDAL(); // Đối tượng DAL để truy vấn Nhân Khẩu
    //endregion

    //region Phương thức initialize
    /**
     * Phương thức khởi tạo sau khi load FXML.
     * - Ánh xạ các cột với getter của lớp Resident
     * - Ẩn sidebar
     * - Tải dữ liệu nhân khẩu ban đầu
     * - Thiết lập disable cho nút Edit, Delete nếu chưa chọn dòng
     * - Thiết lập lắng nghe ô tìm kiếm để cập nhật realtime
     */
    public void initialize() {
        // 1. Thiết lập cellValueFactory cho các cột
        colResidentId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colHouseholdId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHouseholdId()));
        colResidentName.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colIdentityCard.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdentityCard()));
        colDateOfBirth.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateOfBirth()));
        colPlaceOfBirth.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlaceOfBirth()));
        colEthnicity.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEthnicity()));
        colOccupation.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOccupation()));
        colRelationship.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRelationship()));

        // 2. Ẩn sidebar khi khởi chạy (nếu có)
        if (sidebar != null) {
            sidebar.setLayoutX(-sidebar.getPrefWidth());
        }

        // 3. Tải dữ liệu ban đầu
        loadResidentData();

        // 4. Disable nút Edit, Delete nếu chưa chọn hàng
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        // 5. Lắng nghe selectionModel của bảng
        tableResidents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEdit.setDisable(!isSelected);
            btnDelete.setDisable(!isSelected);
        });

        // 6. Tìm kiếm khi thay đổi nội dung txtSearch
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchResidents();
        });
    }
    //endregion

    //region Tải dữ liệu ban đầu
    /**
     * loadResidentData() sẽ lấy toàn bộ dữ liệu từ nhankhautbl,
     * đưa vào masterData, rồi setItems cho tableResidents.
     */
    private void loadResidentData() {
        System.out.println("Bắt đầu tải dữ liệu...");
        masterData.clear();

        // Gọi DAL để search all
        ArrayList<Object[]> results = nhanKhauDal.searchNhanKhau("nhankhautbl", null, null);
        if (results != null) {
            for (Object[] row : results) {
                // Mapping cột → trường Resident
                String id           = (String) row[0];
                String householdId  = (String) row[1];
                String name         = (String) row[2];
                String identityCard = (String) row[3];

                Object dobObj = row[4];
                String dob = (dobObj != null) ? dobObj.toString() : "";

                String placeOfBirth = (String) row[5];
                String ethnicity    = (String) row[6];
                String occupation   = (String) row[7];
                String relationship = (String) row[8];

                masterData.add(
                        new Resident(id, householdId, name, identityCard,
                                dob, placeOfBirth, ethnicity, occupation, relationship)
                );
            }
        }
        // Đưa dữ liệu vào TableView
        tableResidents.setItems(masterData);
    }
    //endregion

    //region Tìm kiếm Nhân Khẩu
    /**
     * searchResidents() - Tìm kiếm theo Mã Nhân Khẩu hoặc Họ Tên
     * (logic cũ, tùy bạn tùy chỉnh)
     */
    @FXML
    private void searchResidents() {
        String keyword = txtSearch.getText().trim();
        ArrayList<Object[]> results = null;

        if (keyword.isEmpty()) {
            // Nếu ô tìm kiếm rỗng => Load all
            loadResidentData();
            results = nhanKhauDal.searchNhanKhau("nhankhautbl", null, null);
        } else {
            // Logic cũ: Nếu keyword là số, tìm theo "MaNhanKhau"
            // Nếu rỗng => tìm theo "HoTen"

                results = nhanKhauDal.searchNhanKhau("nhankhautbl", "MaNhanKhau", keyword);
                if (results == null || results.isEmpty()) {
                    results = nhanKhauDal.searchNhanKhau("nhankhautbl", "HoTen", keyword);
                }
            }


        masterData.clear();
        if (results != null) {
            for (Object[] row : results) {
                String id           = (String) row[0];
                String householdId  = (String) row[1];
                String name         = (String) row[2];
                String identityCard = (String) row[3];
                String dob          = (String) row[4];
                String placeOfBirth = (String) row[5];
                String ethnicity    = (String) row[6];
                String occupation   = (String) row[7];
                String relationship = (String) row[8];

                masterData.add(
                        new Resident(id, householdId, name, identityCard, dob,
                                placeOfBirth, ethnicity, occupation, relationship)
                );
            }
        }
        // Cập nhật TableView
        tableResidents.setItems(masterData);
    }
    //endregion

    //region Thêm Nhân Khẩu
    /**
     * addResident(): Mở Dialog để nhập thông tin, sau đó gọi DAL để insert.
     */
    @FXML
    private void addResident() {
        // 1. Tạo dialog (Form) để nhập dữ liệu
        Dialog<Pair<String, String[]>> dialog = createResidentDialog("Thêm cư dân", null);

        // 2. Hiển thị dialog
        Optional<Pair<String, String[]>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            // data.getKey() = MaNhanKhau
            String residentId = data.getKey();
            // data.getValue() = [householdId, name, identityCard, dob, placeOfBirth,
            //                    ethnicity, occupation, relationship]
            String[] details  = data.getValue();
            String householdId = details[0];
            String name        = details[1];
            String identityCard= details[2];
            String dob         = details[3];
            String placeOfBirth= details[4];
            String ethnicity   = details[5];
            String occupation  = details[6];
            String relationship= details[7];

            // Tạo mảng cột & giá trị tương ứng
            String[] columns = {
                    "MaNhanKhau", "MaHoGiaDinh", "HoTen", "CCCD",
                    "NgaySinh", "NoiSinh", "DanToc", "NgheNghiep", "QuanHe"
            };
            String[] values = {
                    residentId, householdId, name, identityCard,
                    dob, placeOfBirth, ethnicity, occupation, relationship
            };

            // 3. Gọi DAL để insert
            boolean success = nhanKhauDal.insertNhanKhau("nhankhautbl", columns, values);
            if (success) {
                // 4. Thông báo & load lại data
                showAlert("Thêm thành công", "Cư dân mới đã được thêm!");
                loadResidentData();
            } else {
                showAlert("Lỗi", "Không thể thêm cư dân mới!");
            }
        });
    }
    //endregion

    //region Sửa Nhân Khẩu
    /**
     * editResident(): Mở Dialog điền sẵn dữ liệu cư dân. Người dùng sửa xong -> update DAL.
     */
    @FXML
    private void editResident() {
        // 1. Lấy cư dân đang chọn
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            // 2. Mở dialog, điền sẵn dữ liệu
            Dialog<Pair<String, String[]>> dialog = createResidentDialog("Sửa cư dân", selectedResident);

            // 3. Nhận kết quả
            Optional<Pair<String, String[]>> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String residentId    = data.getKey();
                String[] details     = data.getValue();
                String householdId   = details[0];
                String name          = details[1];
                String identityCard  = details[2];
                String dob           = details[3];
                String placeOfBirth  = details[4];
                String ethnicity     = details[5];
                String occupation    = details[6];
                String relationship  = details[7];

                boolean allSuccess = true;

                // 4. Gọi update nhiều cột.
                // Có thể ném ra SQLException => try-catch
                try {
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "MaHoGiaDinh", householdId, "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "HoTen",       name,        "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "CCCD",       identityCard,"MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "NgaySinh",   dob,         "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "NoiSinh",    placeOfBirth,"MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "DanToc",     ethnicity,   "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "NgheNghiep", occupation,  "MaNhanKhau", residentId);
                    allSuccess &= nhanKhauDal.updateNhanKhau("nhankhautbl", "QuanHe",     relationship,"MaNhanKhau", residentId);
                } catch (SQLException e) {
                    e.printStackTrace();
                    allSuccess = false;
                }

                // 5. Thông báo & load lại
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
    //endregion

    //region Xóa Nhân Khẩu
    /**
     * deleteResident(): Xóa cư dân đang chọn.
     * Gọi DAL -> deleteNhanKhau("MaNhanKhau", selectedResident.getId());
     */
    @FXML
    private void deleteResident() throws SQLException {
        Resident selectedResident = tableResidents.getSelectionModel().getSelectedItem();
        if (selectedResident != null) {
            // Hỏi xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xóa cư dân");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa cư dân này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Gọi DAL xóa
                boolean success = nhanKhauDal.deleteNhanKhauP("MaNhanKhau", selectedResident.getId());
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
    //endregion

    //region Tạo Dialog nhập/ sửa dữ liệu cư dân
    /**
     * Tạo Dialog (Form) cho việc thêm/ sửa cư dân.
     * - Nếu residentData == null => form trống (thêm).
     * - Nếu residentData != null => form hiển thị sẵn dữ liệu (sửa).
     *
     * @return Dialog<Pair<String, String[]>>
     *   - key: Mã Nhân Khẩu
     *   - value: [householdId, name, identityCard, dob, placeOfBirth, ethnicity, occupation, relationship]
     */
    private Dialog<Pair<String, String[]>> createResidentDialog(String title, Resident residentData) {
        // 1. Tạo Dialog
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle(title);

        // 2. Nút Lưu/ Cancel
        ButtonType okButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 3. Tạo GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // 4. Tạo TextField cho từng trường
        TextField txtId           = new TextField();
        TextField txtHouseholdId  = new TextField();
        TextField txtName         = new TextField();
        TextField txtIdentityCard = new TextField();
        TextField txtDob          = new TextField();
        TextField txtPlaceOfBirth = new TextField();
        TextField txtEthnicity    = new TextField();
        TextField txtOccupation   = new TextField();
        TextField txtRelationship = new TextField();

        // 5. Thêm Label + TextField lên grid
        grid.add(new Label("Mã Nhân Khẩu:"), 0, 0);
        grid.add(txtId,            1, 0);
        grid.add(new Label("Mã Hộ Gia Đình:"), 0, 1);
        grid.add(txtHouseholdId,   1, 1);
        grid.add(new Label("Họ và Tên:"), 0, 2);
        grid.add(txtName,          1, 2);
        grid.add(new Label("CCCD:"), 0, 3);
        grid.add(txtIdentityCard,  1, 3);
        grid.add(new Label("Ngày Sinh:"), 0, 4);
        grid.add(txtDob,           1, 4);
        grid.add(new Label("Nơi Sinh:"), 0, 5);
        grid.add(txtPlaceOfBirth,  1, 5);
        grid.add(new Label("Dân Tộc:"), 0, 6);
        grid.add(txtEthnicity,     1, 6);
        grid.add(new Label("Nghề Nghiệp:"), 0, 7);
        grid.add(txtOccupation,    1, 7);
        grid.add(new Label("Quan Hệ:"), 0, 8);
        grid.add(txtRelationship,  1, 8);

        // 6. Nếu là sửa, đổ dữ liệu cũ vào
        if (residentData != null) {
            txtId.setText(           residentData.getId());
            txtHouseholdId.setText(  residentData.getHouseholdId());
            txtName.setText(         residentData.getName());
            txtIdentityCard.setText( residentData.getIdentityCard());
            txtDob.setText(          residentData.getDateOfBirth());
            txtPlaceOfBirth.setText( residentData.getPlaceOfBirth());
            txtEthnicity.setText(    residentData.getEthnicity());
            txtOccupation.setText(   residentData.getOccupation());
            txtRelationship.setText( residentData.getRelationship());
        }

        dialog.getDialogPane().setContent(grid);

        // 7. Khi bấm Lưu => trả về Pair
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
    //endregion

    //region showAlert
    /**
     * showAlert: Hiển thị thông báo chung
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    //endregion

    //region Xử lý sidebar, chuyển màn ...
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
    //endregion
}
