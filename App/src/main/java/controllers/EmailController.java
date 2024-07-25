package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import models.Agence;
import models.EmailSender;
import models.EmailConfig;
import org.controlsfx.control.Notifications;
import utils.PDFGenerator;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.Alert;
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



