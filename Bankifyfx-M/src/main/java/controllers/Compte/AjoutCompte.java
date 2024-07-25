package controllers.Compte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;
import models.CompteClient;
import services.CompteClientService;
import java.sql.SQLException;

public class AjoutCompte {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField ribTF;

    @FXML
    private TextField mailTF;

    @FXML
    private TextField telTF;

    @FXML
    private TextField soldeTF;

    @FXML
    private ComboBox<String> genderComboBox; // Inject the ComboBox for gender

    @FXML
    private Button confirmButton;

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        try {
            String nom = nomTF.getText();
            String prenom = prenomTF.getText();
            String rib = ribTF.getText();
            String mail = mailTF.getText();
            String tel = telTF.getText();
            String sexe = genderComboBox.getValue(); // Retrieve the selected gender

            // Validating non-empty fields
            if (nom.isEmpty() || prenom.isEmpty() || rib.isEmpty() || mail.isEmpty() || tel.isEmpty() || soldeTF.getText().isEmpty() || sexe == null){
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            // Validating nom and prenom fields (only letters)
            if (!nom.matches("[a-zA-Z]+") || !prenom.matches("[a-zA-Z]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et le prénom doivent contenir seulement des lettres !");
                return;
            }

            // Validating rib (only digits and length = 16)
            if (!rib.matches("\\d{16}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le RIB doit contenir exactement 16 chiffres !");
                return;
            }

            // Validating email format
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", mail)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'adresse e-mail invalide !");
                return;
            }

            // Validating tel (only digits and length = 8)
            if (!tel.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
                return;
            }

            // Parsing the float value for solde
            float solde = Float.parseFloat(soldeTF.getText());

            // Adding the new compteClient
            CompteClient compteClient = new CompteClient(nom, prenom, rib, mail, tel, solde, sexe);
            compteClientService.add(compteClient);
            System.out.println("Compte client ajouté avec succès !");

            // Show a success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte client ajouté avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("Invalid solde format");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de solde invalide !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
