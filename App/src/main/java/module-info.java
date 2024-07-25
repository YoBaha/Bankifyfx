module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    //requires mysql.connector.java;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires java.mail;
    requires org.controlsfx.controls;
    requires javafx.web;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens controllers to javafx.fxml, javafx.base;
    opens models to javafx.base;




    exports com.example.app;
}
