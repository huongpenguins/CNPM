module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
<<<<<<< HEAD
    requires javafx.graphics;
    requires javafx.base;

    opens Entities to javafx.base;
=======
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;

>>>>>>> f4810009717b2cf856026c2b087e03ff7c8fd05e
    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
    exports com.example.manager; //New by Phong
}