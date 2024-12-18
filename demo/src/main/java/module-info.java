module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires transitive javafx.graphics;
    requires javafx.base;
    //opens Entities to javafx.base;

    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
    requires org.controlsfx.controls;
    requires java.mail;


    opens com.example.Entities to javafx.base;
    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.login;
    opens com.example.login to javafx.fxml;
    exports com.example.manager;
    exports com.example.connect;
    opens com.example.connect to javafx.fxml; //New by Phong
}