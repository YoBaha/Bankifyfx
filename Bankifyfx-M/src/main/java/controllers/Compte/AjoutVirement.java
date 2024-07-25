package controllers.Compte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import models.Virement;
import services.VirementService;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class AjoutVirement {

    @FXML
    private TextField compteSourceTextField;

    @FXML
    private TextField compteDestinationTextField;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField heureTextField;

    @FXML
    private void initialize() {
        // Initialize method if needed
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        // This method will be called when the confirm button is clicked
        // You can retrieve the values from the text fields and perform actions here
        String compteSource = compteSourceTextField.getText();
        String compteDestination = compteDestinationTextField.getText();
        float montant = 0.0f;
        Date date = null;
        Time heure = null;

        // Validation: Check if any field is empty
        if (compteSource.isEmpty() || compteDestination.isEmpty() || montantTextField.getText().isEmpty() || dateTextField.getText().isEmpty() || heureTextField.getText().isEmpty()) {
            showAlert("Champs obligatoires", "Tous les champs sont obligatoires!");
            return;
        }

        // Validation: Check if compte_source contains only digits and has length = 16
        if (!compteSource.matches("\\d{16}")) {
            showAlert("Erreur", "Le compte source doit contenir exactement 16 chiffres !");
            return;
        }

        // Validation: Check if compte_destination contains only digits, has length = 16, and is different from compte_source
        if (!compteDestination.matches("\\d{16}") || compteDestination.equals(compteSource)) {
            showAlert("Erreur", "Le compte destination doit contenir exactement 16 chiffres et être différent du compte source !");
            return;
        }

        // Validation: Check if montant is a valid float number
        try {
            montant = Float.parseFloat(montantTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre !");
            return;
        }

        // Parse date and heure strings to Date and Time objects
        try {
            date = Date.valueOf(dateTextField.getText());
            heure = Time.valueOf(heureTextField.getText());
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Veuillez entrer une date et une heure valides au format yyyy-mm-dd pour la date et hh:mm:ss pour l'heure !");
            return;
        }

        Virement v= new Virement(compteSource,compteDestination,montant,date,heure);
        VirementService C=new VirementService();
        C.add(v);
        // Show a confirmation dialog
        showAlert1("Succès", "Virement effectué avec succès !");
    }

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
