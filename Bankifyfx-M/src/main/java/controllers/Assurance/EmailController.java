package controllers.Assurance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import models.EmailSender;
import org.controlsfx.control.Notifications;

import javax.mail.MessagingException;

public class EmailController {
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Email msg");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private TextArea bodyTextArea;

    @FXML
    private TextField recipientTextField;

    @FXML
    void sendEmailButtonClicked(ActionEvent event) {
        String recipient = recipientTextField.getText();
        String subject = "Bankify Contact Agence Mail ";
        String body = bodyTextArea.getText();

        try {
            EmailSender.sendEmail(recipient, subject, body);
            showAlert("Email sent successfully.");
            showNotification("E-mail ENVOYÉ AVEC SUCCÈS");
        } catch (MessagingException e) {
            showAlert("Error sending email: " + e.getMessage());
        }
    }

    private void showNotification(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showInformation();
    }}



