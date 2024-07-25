package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainFrontFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        
       //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/back.fxml"));
      // FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Front.fxml"));




       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Back/Cheques.fxml"));
     //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Front/FrontCheques.fxml"));

        URL fxmlUrl = getClass().getResource("/User/login.fxml");
        //URL fxmlUrl = getClass().getResource("/front.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML file not found.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Bankify");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

