module thidk.bluemoon {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
}