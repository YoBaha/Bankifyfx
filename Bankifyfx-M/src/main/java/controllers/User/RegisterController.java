package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;
import services.User.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private ChoiceBox<String> choiceGenre;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private Button btnRegister;

    @FXML
    private Button switchToLogin;

    private final ServiceUser serviceUser = new ServiceUser();

    private final String PROMPT_TEXT = "Select Gender";

    @FXML
    private void initialize() {
        // Populate the genre choice box
        choiceGenre.getItems().addAll(PROMPT_TEXT, "Male", "Female");

        // Set the initial selection to the prompt text
        choiceGenre.getSelectionModel().selectFirst();

        // Example event handler for the register button
        btnRegister.setOnAction(event -> register());
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) switchToLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void register() {
        // Retrieve user inputs
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String genre = choiceGenre.getValue();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || dateNaissance == null ){
            showAlert("Error", "Veuillez remplir tous les champs.");
        } else if (!isValidEmail(email)) {
            showAlert("Error", "Veuillez entrer une adresse email valide.");
        } else if (!isValidPassword(password)) {
            showAlert("Error", "Le mot de passe doit contenir au moins 8 caractères et inclure au moins un chiffre, une lettre majuscule, une lettre minuscule ");
        } else if (PROMPT_TEXT.equals(genre)) {
            showAlert("Error", "Veuillez choisir un genre.");
        } else if (serviceUser.emailExists(email)) {
            showAlert("Error", "Cette adresse email est déjà utilisée.");
        } else {
        // Create a new User object
        User user = new User(nom, prenom, email, password, java.sql.Date.valueOf(dateNaissance), genre);

        // Add the user to the database using the service
        serviceUser.Ajouter(user);

        // Show alert
        showAlert(AlertType.INFORMATION, "Registration Successful", "User registered successfully!");

        // Navigate to login.fxml
        navigateToLogin();}
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
