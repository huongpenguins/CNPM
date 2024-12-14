module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;


    opens com.example.Entities to javafx.base;
    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
    exports com.example.manager; //New by Phong
}