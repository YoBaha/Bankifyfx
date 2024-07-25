package controllers.Assurance;
import javafx.scene.control.*;
import org.controlsfx.control.Notifications;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import models.Agence;
import utils.PDFGenerator1;

import java.io.IOException;
import java.sql.*;

import javafx.scene.chart.PieChart;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import models.EmailSender;

import javax.mail.MessagingException;


public class AgenceController {

    public TextField recipientTextField;
    public TextArea bodyTextArea;
    @FXML
    private PieChart agencePieChart;
    @FXML
    private TableView<Agence> agenceTable;


    @FXML
    private TextField nomAgenceTF;

    @FXML
    private TextField emailAgenceTF;

    @FXML
    private TextField telAgenceTF;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    void initialize() {
        showAgence(null);
        loadAgenceTypeDistribution();
    }

    private void loadAgenceTypeDistribution() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT nom_agence, COUNT(*) AS frequency FROM agence GROUP BY nom_agence";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nomAgence = rs.getString("nom_agence");
                int frequency = rs.getInt("frequency");
                pieChartData.add(new PieChart.Data(nomAgence, frequency));
            }

            agencePieChart.setData(pieChartData);
            agencePieChart.setPrefSize(350, 400);
        } catch (SQLException e) {
            showAlert("Error loading pie chart data: " + e.getMessage());
        }
    }

    @FXML
    void AddAgence(ActionEvent event) {
        String nomAgence = nomAgenceTF.getText();
        String emailAgence = emailAgenceTF.getText();
        String telAgence = telAgenceTF.getText();

        // Input validation
        if (nomAgence.length() < 3 || emailAgence.length() < 3 || telAgence.length() < 3) {
            showAlert("All fields must have at least 3 characters.");
            return;
        }

        if (!telAgence.matches("\\d+")) { // Check if telAgence contains only numbers
            showAlert("Tel Agence should contain only numbers.");
            return;
        }

        if (!isValidEmail(emailAgence)) { // Check if emailAgence is a valid email
            showAlert("Invalid email format.");
            return;

        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO agence (nom_agence, email_agence, tel_agence) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nomAgence);
            stmt.setString(2, emailAgence);
            stmt.setString(3, telAgence);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                Agence newAgence = new Agence(id, nomAgence, emailAgence, telAgence);
                agenceTable.getItems().add(newAgence);
                loadAgenceTypeDistribution();
            }
            showNotification("AGENCE AJOUTER AVEC SUCCES!!!");
            clearFields();

        } catch (SQLException e) {
            showAlert("Error adding record: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }
    private void showNotification(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showInformation();
    }


    @FXML
    void deleteAgence(ActionEvent event) {
        Agence selectedAgence = agenceTable.getSelectionModel().getSelectedItem();
        if (selectedAgence != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Supprimer Agence");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette Agence ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM agence WHERE id = ?")) {

                    stmt.setInt(1, selectedAgence.getId());
                    stmt.executeUpdate();

                    showAgence(null);

                } catch (SQLException e) {
                    showAlert("Error deleting record: " + e.getMessage());
                }
            }
        } else {
            showAlert("Please select an agence.");
        }
        loadAgenceTypeDistribution();
    }


    @FXML
    void showAgence(ActionEvent event) {
        ObservableList<Agence> agenceList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM agence")) {

            while (rs.next()) {
                Agence agence = new Agence(
                        rs.getInt("id"),
                        rs.getString("nom_agence"),
                        rs.getString("email_agence"),
                        rs.getString("tel_agence")
                );
                agenceList.add(agence);
            }

            agenceTable.setItems(agenceList);



        } catch (SQLException e) {
            showAlert("Error fetching records: " + e.getMessage());
        }
    }




    @FXML
    void updateAgence(ActionEvent event) {
        Agence selectedAgence = agenceTable.getSelectionModel().getSelectedItem();
        if (selectedAgence != null) {
            String nomAgence = nomAgenceTF.getText();
            String emailAgence = emailAgenceTF.getText();
            String telAgence = telAgenceTF.getText();

            // Input validation
            if (nomAgence.length() < 3 || emailAgence.length() < 3 || telAgence.length() < 3) {
                showAlert("All fields must have at least 3 characters.");
                return;
            }

            if (!telAgence.matches("\\d+")) { // Check if telAgence contains only numbers
                showAlert("Tel Agence should contain only numbers.");
                return;
            }

            if (!isValidEmail(emailAgence)) { // Check if emailAgence is a valid email
                showAlert("Invalid email format.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE agence SET nom_agence = ?, email_agence = ?, tel_agence = ? WHERE id = ?")) {

                stmt.setString(1, nomAgence);
                stmt.setString(2, emailAgence);
                stmt.setString(3, telAgence);
                stmt.setInt(4, selectedAgence.getId());  //  getId() returns  id

                stmt.executeUpdate();

                showAgence(null);
                clearFields();

            } catch (SQLException e) {
                showAlert("Error updating record: " + e.getMessage());
            }
        } else {
            showAlert("Please select an agence.");
        }
        loadAgenceTypeDistribution();}



    private void clearFields() {
        nomAgenceTF.clear();
        emailAgenceTF.clear();
        telAgenceTF.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    void goToCategorieButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/CategorieAssuranceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading CategorieAssuranceGUI: " + e.getMessage());
        }
    }

    public void goToAssuranceeButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AssuranceGUI.fxml"));
            Parent root = loader.load();

            // Get  stage  f action event
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }
    }

    public void goToFrontAGENCEButtonClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/FrontAgenceGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agence Form");
            stage.setScene(new Scene(root));

            // Close the new window only when the main application is closed
            stage.setOnCloseRequest(event -> {
                // Handle close request if needed
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void generatePDF(ActionEvent actionEvent) {
        PDFGenerator1 pdfGenerator1 = new PDFGenerator1();
        pdfGenerator1.generatePDF(actionEvent, agenceTable, "Bankify Agence Table");
    }


    @FXML
    void sendEmailButtonClicked(ActionEvent event) {
        String recipient = recipientTextField.getText();
        String subject = "Subject of the email"; // You can define subject dynamically if needed
        String body = bodyTextArea.getText();

        try {
            EmailSender.sendEmail(recipient, subject, body);
            showAlert("Email sent successfully.");
        } catch (MessagingException e) {
            showAlert("Error sending email: " + e.getMessage());
        }
    }

    public void gotosendemail(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/emailGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Email Form");
            stage.setScene(new Scene(root));

            // Close the new window only when the main application is closed
            stage.setOnCloseRequest(event -> {
                // Handle close request if needed
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


