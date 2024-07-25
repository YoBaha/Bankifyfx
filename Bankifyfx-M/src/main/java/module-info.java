module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires com.google.gson;
    requires java.persistence;
    requires org.apache.pdfbox;
    requires twilio;
    requires com.google.zxing;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.controlsfx.controls;
    requires java.mail;


    opens models to javafx.base;


    exports controllers;
    opens controllers to javafx.fxml;

    exports controllers.Cheques;
    opens controllers.Cheques to javafx.fxml;


    opens controllers.User to javafx.fxml;
    opens controllers.Compte to
 javafx.fxml;
    opens controllers.creditController to javafx.fxml;
    opens controllers.categorieCreditController to javafx.fxml;
    opens controllers.remboursementController to javafx.fxml;
    opens controllers.Assurance to javafx.base, javafx.fxml;



    exports org.example;



}