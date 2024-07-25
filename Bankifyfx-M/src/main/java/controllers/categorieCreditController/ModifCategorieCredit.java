package controllers.categorieCreditController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceCategorieCredit;
import models.CategorieCredit;

import java.sql.SQLException;
public class ModifCategorieCredit {
    private final ServiceCategorieCredit service = new ServiceCategorieCredit();
    private CategorieCredit categorie;

    @FXML
    private Label categorieLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField minMontantField;

    @FXML
    private TextField maxMontantField;

    @FXML
    private Button modifierButton;

    public void initData(CategorieCredit categorie) {
        this.categorie = categorie;
        nomField.setText(String.valueOf(categorie.getNom()));
        minMontantField.setText(String.valueOf(categorie.getMinMontant()));
        maxMontantField.setText(String.valueOf(categorie.getMaxMontant()));
        categorieLabel.setText("Modification de la categorie d'ID: " + categorie.getId());
    }

    @FXML
    void modifierCategorie(ActionEvent event) throws SQLException {
        if (nomField.getText().isEmpty()) {
            System.out.println("Pas de nom saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un nom");
            alert.showAndWait();
        } else if (minMontantField.getText().isEmpty() || maxMontantField.getText().isEmpty()) {
            System.out.println("Pas de montant saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant");
            alert.showAndWait();
        } else if (!maxMontantField.getText().matches("[0-9.]+") || !minMontantField.getText().matches("[0-9.]+")) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être en chiffre");
            alert.showAndWait();
        } else if (Double.parseDouble(maxMontantField.getText()) < 0 || Double.parseDouble(minMontantField.getText()) < 0) {
            System.out.println("Montant négatif");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant positive");
            alert.showAndWait();
        } else if (service.modifNom(nomField.getText(),categorie.getId())) {
            System.out.println("Categorie existe");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Cette categorie existe déjà");
            alert.showAndWait();
        } else if (Double.parseDouble(maxMontantField.getText()) < Double.parseDouble(minMontantField.getText())) {
            System.out.println("Intervalle invalide");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant maximale doit être supérieur au montant minimale");
            alert.showAndWait();
        } else {
            try {
                categorie.setNom(nomField.getText());
                categorie.setMinMontant(Double.parseDouble(minMontantField.getText()));
                categorie.setMaxMontant(Double.parseDouble(maxMontantField.getText()));
                service.edit(categorie);
                Stage stage = (Stage) modifierButton.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
