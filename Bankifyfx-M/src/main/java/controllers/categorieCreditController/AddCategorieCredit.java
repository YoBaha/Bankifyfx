package controllers.categorieCreditController;

import models.CategorieCredit;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import services.ServiceCategorieCredit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCategorieCredit {
    private final ServiceCategorieCredit service = new ServiceCategorieCredit();
    @FXML
    private TextField nomTF;

    @FXML
    private TextField minMontantTF;

    @FXML
    private TextField maxMontantTF;

    @FXML
    private Button addButton;

    @FXML
    void addCategorieCredit(ActionEvent event) throws SQLException {

        if(nomTF.getText().isEmpty())
        {
            System.out.println("Pas de nom saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un nom");
            alert.showAndWait();
        } else if(minMontantTF.getText().isEmpty() || maxMontantTF.getText().isEmpty())
        {
            System.out.println("Pas de montant saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant");
            alert.showAndWait();
        } else if (!maxMontantTF.getText().matches("[0-9.]+") || !minMontantTF.getText().matches("[0-9.]+")) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être en chiffre");
            alert.showAndWait();
        } else if (Double.parseDouble(maxMontantTF.getText())<0 || Double.parseDouble(minMontantTF.getText())<0) {
            System.out.println("Montant négatif");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant positive");
            alert.showAndWait();
        } else if (service.nomExiste(nomTF.getText())) {
            System.out.println("Categorie existe");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Cette categorie existe déjà");
            alert.showAndWait();
        } else if (Double.parseDouble(maxMontantTF.getText()) < Double.parseDouble(minMontantTF.getText())) {
            System.out.println("Intervalle invalide");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant maximale doit être supérieur au montant minimale");
            alert.showAndWait();
        } else {
            service.add(new CategorieCredit(nomTF.getText(),Double.parseDouble(minMontantTF.getText()),Double.parseDouble(maxMontantTF.getText())));
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        }
    }
}
