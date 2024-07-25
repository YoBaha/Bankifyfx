package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.User;
import services.User.ServiceUser;

import java.time.LocalDate;
import java.util.Date;

public class ModifierController {

    @FXML
    private Button btnModifier;

    @FXML
    private ChoiceBox<String> choiceGenre;

    private OnUserModifiedListener onUserModifiedListener;

    @FXML
    private DatePicker txtDateNaissance;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    private ServiceUser serviceUser;
    // Declare serviceUser

    public ModifierController() {
        // Initialize serviceUser
        serviceUser = new ServiceUser();
    }

    public void setOnUserModifiedListener(OnUserModifiedListener listener) {
        this.onUserModifiedListener = listener;
    }


    private User user; // User to be modified

    public void setUser(User user) {
        this.user = user;
        txtNom.setText(user.getNom());
        txtPrenom.setText(user.getPrenom());
        txtEmail.setText(user.getEmail());
        // Set genre
        choiceGenre.setValue(user.getGenre());
        // Set dateNaissance
        if (user.getDateNaissance() != null) {
            txtDateNaissance.setValue(LocalDate.parse(user.getDateNaissance().toString()));
        } else {
            // Handle the case when dateNaissance is null
            txtDateNaissance.setValue(null);
        }
    }
    public interface OnUserModifiedListener {
        void onUserModified(User modifiedUser);
    }


    @FXML
    void modifier(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String genre = choiceGenre.getValue();
        LocalDate dateNaissance = txtDateNaissance.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || genre.isEmpty() || dateNaissance == null) {
            showAlert("Error", "Veuillez remplir tous les champs.");
        } else if (!isValidEmail(email)) {
            showAlert("Error", "Veuillez entrer une adresse email valide.");
        } else {
            // Convert LocalDate to java.util.Date
            Date utilDate = java.sql.Date.valueOf(dateNaissance);

            // Update user object with new values
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setGenre(genre);
            user.setDateNaissance(utilDate);

            // Update the user using the service
            serviceUser.Modifier1(user);
            // Call the listener to notify about user modification
            if (onUserModifiedListener != null) {
                onUserModifiedListener.onUserModified(user);
            }

            // Show success message
            showAlert("Success", "Utilisateur modifié avec succès.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        // Simple email validation using a regular expression
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}
