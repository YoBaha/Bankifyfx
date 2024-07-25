package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BackController {

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    public void handleClicks(javafx.event.ActionEvent event) {
        if (event.getSource() == btnOverview) {
            // Handle Overview button click
        } else if (event.getSource() == btnOrders) {
            //  button click
        } else if (event.getSource() == btnCustomers) {
            //   button click
        } else if (event.getSource() == btnMenus) {
            // Handle Menu button click
        } else if (event.getSource() == btnPackages) {
            // Handle Pack button click
        } else if (event.getSource() == btnSettings) {
            // Handle  button click
        }
    }

    public void Signout(ActionEvent actionEvent) {
    }

    public void gotoassuranceback(ActionEvent actionEvent) {
        try {
            // Load AssuranceGUI.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AssuranceGUI.fxml"));
            Parent root = loader.load();

            // Get the stage from the action event
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set  new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }}

    private void showAlert(String s) {
    }
}