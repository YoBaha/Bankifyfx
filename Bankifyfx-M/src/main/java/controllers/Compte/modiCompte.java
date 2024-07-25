package controllers.Compte;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.CompteClient;
import services.CompteClientService;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class modiCompte {

    @FXML
    private Label compteClientLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField ribField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField telField;

    @FXML
    private TextField soldeField;

    @FXML
    private ComboBox<String> sexeComboBox;

    private final CompteClientService compteClientService = new CompteClientService();
    private CompteClient compteClient;

    public void initialize() {
        // Populate the sexeComboBox with gender options
        sexeComboBox.getItems().addAll("Male", "Female", "Other");
    }

    public void initData(CompteClient compteClient) {
        this.compteClient = compteClient;

        // Set the compteClientLabel text
        compteClientLabel.setText("ID du compte client à modifier : " + compteClient.getId());

        // Populate the text fields with the compteClient's data
        nomField.setText(compteClient.getNom());
        prenomField.setText(compteClient.getPrenom());
        ribField.setText(compteClient.getRib());
        mailField.setText(compteClient.getMail());
        telField.setText(compteClient.getTel());
        soldeField.setText(String.valueOf(compteClient.getSolde()));

        // Set the selected value of sexeComboBox based on the compteClient's gender
        sexeComboBox.setValue(compteClient.getSexe());
    }

    @FXML
    void modifierCompteClient() {
        try {
            // Get the values from text fields and combo box
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String rib = ribField.getText();
            String mail = mailField.getText();
            String tel = telField.getText();
            String sexe = sexeComboBox.getValue();
            float solde = Float.parseFloat(soldeField.getText());

            // Validate non-empty fields
            if (nom.isEmpty() || prenom.isEmpty() || rib.isEmpty() || mail.isEmpty() || tel.isEmpty() || soldeField.getText().isEmpty() || sexe == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            // Validate nom and prenom fields (only letters)
            if (!nom.matches("[a-zA-Z]+") || !prenom.matches("[a-zA-Z]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et le prénom doivent contenir seulement des lettres !");
                return;
            }

            // Validate rib (only digits and length = 16)
            if (!rib.matches("\\d{16}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le RIB doit contenir exactement 16 chiffres !");
                return;
            }

            // Validate email format
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", mail)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'adresse e-mail invalide !");
                return;
            }

            // Validate tel (only digits and length = 8)
            if (!tel.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
                return;
            }

            // Update the compteClient object with modified values
            compteClient.setNom(nom);
            compteClient.setPrenom(prenom);
            compteClient.setRib(rib);
            compteClient.setMail(mail);
            compteClient.setTel(tel);
            compteClient.setSolde(solde);
            compteClient.setSexe(sexe);

            // Call the service to update the compteClient
            compteClientService.update(compteClient);

            // Show success message to user
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte client modifié avec succès !");

            // Close the current window after successful modification
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de solde invalide !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du compte client !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}
