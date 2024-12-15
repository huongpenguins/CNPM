module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires javafx.graphics;
    requires javafx.base;

    opens Entities to javafx.base;

    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;


    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
    exports com.example.manager;
    exports com.example.connect;
    opens com.example.connect to javafx.fxml;
    opens com.example.Entities to javafx.base; //New by Phong
}