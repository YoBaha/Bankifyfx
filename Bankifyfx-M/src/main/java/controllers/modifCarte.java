package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CarteService;
import models.Carte;

import java.sql.SQLException;

public class modifCarte {

    private final CarteService carteService = new CarteService();
    private Carte carte;

    @FXML
    private Label carteLabel;

    @FXML
    private TextField numField;

    @FXML
    private TextField dateExpField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField statutField;

    @FXML
    private Button modifierButton;

    /**
     * Initialise les champs avec les valeurs de la carte à modifier.
     */
    public void initData(Carte carte) {
        this.carte = carte;
        // Afficher les détails de la carte dans les champs de texte
        numField.setText(carte.getNum_c());
        dateExpField.setText(carte.getDate_exp().toString());
        typeField.setText(carte.getType_c());
        statutField.setText(carte.getStatut_c());
        carteLabel.setText("Modification de la carte ID: " + carte.getId());
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Modifier".
     */
    @FXML
    void modifierCarte(ActionEvent event) {
        try {
            // Obtenir les valeurs des champs de texte
            String num_c = numField.getText();
            java.util.Date dateExp = java.sql.Date.valueOf(dateExpField.getText());
            String type = typeField.getText();
            String statut = statutField.getText();

            // Valider le type de carte
            if (!type.equalsIgnoreCase("Visa") && !type.equalsIgnoreCase("Mastercard")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le type de carte doit être Visa ou Mastercard !");
                return;
            }

            // Valider le statut de la carte
            if (!statut.equalsIgnoreCase("active") && !statut.equalsIgnoreCase("inactive") && !statut.equalsIgnoreCase("bloquee")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être active, inactive ou bloquee !");
                return;
            }

            // Mettre à jour les valeurs de la carte avec les valeurs modifiées
            carte.setNum_c(num_c);
            carte.setDate_exp(dateExp);
            carte.setType_c(type);
            carte.setStatut_c(statut);

            // Appeler le service pour modifier la carte
            carteService.update(carte);

            // Afficher un message de succès à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Carte modifiée avec succès !");

            // Fermer la fenêtre actuelle après la modification réussie
            closeWindow();

        } catch (IllegalArgumentException e) {
            System.out.println("Format de date invalide");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de date invalide !");
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la carte !");
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
