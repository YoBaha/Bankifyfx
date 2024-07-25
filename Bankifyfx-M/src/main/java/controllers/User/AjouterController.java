package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import services.User.ServiceUser;

import java.time.LocalDate;
import java.util.Date;

public class AjouterController {

    public Button btnClear1;
    @FXML
    private Button btnAjouter;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtMotDePasse;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private ChoiceBox<String> choiceGenre; // Change to ChoiceBox

    @FXML
    private DatePicker txtDateNaissance;

    private ServiceUser serviceUser;
    private OnUserAddedListener onUserAddedListener; // Listener to notify AffController

    @FXML
    public void initialize() {
        serviceUser = new ServiceUser();

        // Populate the choiceGenre with options
        choiceGenre.getItems().addAll("Male", "Female");
        choiceGenre.setValue("Male"); // Set default value
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Retrieve values from text fields, choice box, and date picker
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String motDePasse = txtMotDePasse.getText();
        String genre = choiceGenre.getValue(); // Get the selected value from the ChoiceBox
        LocalDate dateNaissance = txtDateNaissance.getValue();

        // Perform validation
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || dateNaissance == null) {
            showAlert("Error", "Veuillez remplir tous les champs.");
        } else if (!isValidEmail(email)) {
            showAlert("Error", "Veuillez entrer une adresse email valide.");
        } else if (!isValidPassword(motDePasse)) {
            showAlert("Error", "Le mot de passe doit contenir au moins 8 caractères et inclure au moins un chiffre, une lettre majuscule, une lettre minuscule ");
        } else if (serviceUser.emailExists(email)) {
            showAlert("Error", "Cette adresse email est déjà utilisée.");
        } else {
            // Convert LocalDate to java.util.Date
            Date utilDate = java.sql.Date.valueOf(dateNaissance);

            // Create a new User object
            User newUser = new User(nom, prenom, email, motDePasse, utilDate, genre);

            // Add the user using the service
            serviceUser.Ajouter(newUser);

            // Notify AffController that a user has been added
            if (onUserAddedListener != null) {
                onUserAddedListener.onUserAdded(newUser);
            }

            // Show success message
            showAlert("Success", "Utilisateur ajouté avec succès.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidPassword(String password) {
        // Password validation criteria: at least 8 characters, including at least one digit, one uppercase letter, and one lowercase letter
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email) {
        // Simple email validation using a regular expression
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public void setOnUserAddedListener(OnUserAddedListener listener) {
        this.onUserAddedListener = listener;
    }

    // Define OnUserAddedListener interface
    public interface OnUserAddedListener {
        void onUserAdded(User newUser);
    }

    @FXML
    private void clearFields() {
        txtEmail.clear();
        txtMotDePasse.clear();
        txtNom.clear();
        txtPrenom.clear();
        choiceGenre.getSelectionModel().clearSelection();
        txtDateNaissance.getEditor().clear();
    }
}
