package controllers.Assurance;


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AssuranceGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }}

    private void showAlert(String s) {
    }
}
