module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
<<<<<<< HEAD
    requires javafx.graphics;
    requires javafx.base;
=======

    requires javafx.graphics;
    requires javafx.base;

    opens Entities to javafx.base;

>>>>>>> e0d1c95316e943c228b69f1cd21ed2a0a45fbada
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;


<<<<<<< HEAD
    opens com.example.Entities to javafx.base;
=======
>>>>>>> e0d1c95316e943c228b69f1cd21ed2a0a45fbada
    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
    exports com.example.manager;
    exports com.example.connect;
    opens com.example.connect to javafx.fxml; //New by Phong
}