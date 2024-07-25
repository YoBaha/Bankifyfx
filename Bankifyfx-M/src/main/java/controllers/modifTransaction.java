package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.TransactionService;
import models.Transaction;

import java.sql.SQLException;

public class modifTransaction {

    private final TransactionService transactionService = new TransactionService();
    private Transaction transaction;

    @FXML
    private Label transactionLabel;

    @FXML
    private TextField montantField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField statutField;

    @FXML
    private Button modifierButton;

    /**
     * Initialise les champs avec les valeurs de la transaction à modifier.
     */
    public void initData(Transaction transaction) {
        this.transaction = transaction;
        // Afficher les détails de la transaction dans les champs de texte
        montantField.setText(String.valueOf(transaction.getMontant()));
        dateField.setText(transaction.getDate_t().toString());
        typeField.setText(transaction.getType_t());
        statutField.setText(transaction.getStatut_t());
        transactionLabel.setText("Modification de la transaction ID: " + transaction.getId());
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Modifier".
     */
    @FXML
    void modifierTransaction(ActionEvent event) {
        try {
            // Mettre à jour les valeurs de la transaction avec les valeurs modifiées
            transaction.setMontant(Double.parseDouble(montantField.getText()));
            transaction.setDate_t(java.sql.Date.valueOf(dateField.getText()));
            transaction.setType_t(typeField.getText());
            transaction.setStatut_t(statutField.getText());

            // Appeler le service pour modifier la transaction
            transactionService.update(transaction);

            // Afficher un message de succès à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Transaction modifiée avec succès !");

            // Fermer la fenêtre actuelle après la modification réussie
            closeWindow();

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la transaction !");
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte à l'utilisateur.
     *
     * @param alertType le type d'alerte (INFORMATION, WARNING, ERROR)
     * @param title le titre de l'alerte
     * @param message le message de l'alerte
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Ferme la fenêtre actuelle.
     */
    private void closeWindow() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }
}
