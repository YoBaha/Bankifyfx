package controllers.Assurance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.CategorieAssurance;
import utils.ExcelGenerator;
import utils.PDFGenerator1;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class CategorieAssuranceController {

    @FXML
    private TextField agenceresponsableTF;

    @FXML
    private TableView<CategorieAssurance> categorieTable;

    @FXML
    private TextField decripTF;

    @FXML
    private Button goassurance;

    @FXML
    private TextField nomcategorieTF;

    @FXML
    private TextField typecouvertureTF;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";



    @FXML
    void addcategorie(ActionEvent event) {
        String nomCategorie = nomcategorieTF.getText();
        String description = decripTF.getText();
        String typeCouverture = typecouvertureTF.getText();
        String agenceResponsable = agenceresponsableTF.getText();

        // Input validation
        if (nomCategorie.length() < 3 || description.length() < 3 || typeCouverture.length() < 3 || agenceResponsable.length() < 3) {
            showAlert("All fields must have at least 3 characters.");
            return;
        }

        // Check if the data already exists
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM categorie_assurance WHERE nom_categorie = ? AND description = ? AND TypeCouverture = ? AND agenceResponsable = ?")) {

            stmt.setString(1, nomCategorie);
            stmt.setString(2, description);
            stmt.setString(3, typeCouverture);
            stmt.setString(4, agenceResponsable);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                showAlert("This category already exists!");
                return;
            }
        } catch (SQLException e) {
            showAlert("Error checking for existing category: " + e.getMessage());
            return;
        }

        // If data does not exist, proceed to add
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO categorie_assurance (nom_categorie, description, TypeCouverture, agenceResponsable) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, nomCategorie);
            stmt.setString(2, description);
            stmt.setString(3, typeCouverture);
            stmt.setString(4, agenceResponsable);

            stmt.executeUpdate();

            showAlert("Category added successfully!");

            showcategorie(null); // Refresh Table

        } catch (SQLException e) {
            showAlert("Error adding category: " + e.getMessage());
        }
    }




    @FXML
    void deletecategorie(ActionEvent event) {
        CategorieAssurance selectedCategorie = categorieTable.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Supprimer Categorie");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer categorie?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM categorie_assurance WHERE id_categorie = ?")) {

                    stmt.setInt(1, selectedCategorie.getIdCategorie());
                    stmt.executeUpdate();

                    showAlert("Categorie deleted successfully!");
                    showcategorie(null); // Refresh the TableView

                } catch (SQLException e) {
                    showAlert("Error deleting categorie: " + e.getMessage());
                }
            }
        } else {
            showAlert("Please select a categorie.");
        }
    }

    @FXML
    void showcategorie(ActionEvent event) {
        ObservableList<CategorieAssurance> categorieList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM categorie_assurance")) {

            while (rs.next()) {
                CategorieAssurance categorie = new CategorieAssurance(
                        rs.getInt("id_categorie"),
                        rs.getString("nom_categorie"),
                        rs.getString("description"),
                        rs.getString("TypeCouverture"),
                        rs.getString("agenceResponsable")
                );
                categorieList.add(categorie);
            }

            categorieTable.setItems(categorieList);

        } catch (SQLException e) {
            showAlert("Error fetching categories: " + e.getMessage());
        }
    }


    @FXML
    void updatecategorie(ActionEvent event) {
        CategorieAssurance selectedCategorie = categorieTable.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            String nomCategorie = nomcategorieTF.getText();
            String description = decripTF.getText();
            String typeCouverture = typecouvertureTF.getText();
            String agenceResponsable = agenceresponsableTF.getText();

            // Input vald
            if (nomCategorie.length() < 3 || description.length() < 3 || typeCouverture.length() < 3 || agenceResponsable.length() < 3) {
                showAlert("All fields must have at least 3 characters.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE categorie_assurance SET nom_categorie = ?, description = ?, TypeCouverture = ?, agenceResponsable = ? WHERE id_categorie = ?")) {

                stmt.setString(1, nomCategorie);
                stmt.setString(2, description);
                stmt.setString(3, typeCouverture);
                stmt.setString(4, agenceResponsable);
                stmt.setInt(5, selectedCategorie.getIdCategorie());

                stmt.executeUpdate();

                showAlert("Categorie updated successfully!");
                showcategorie(null); // Refresh the TableView

            } catch (SQLException e) {
                showAlert("Error updating categorie: " + e.getMessage());
            }
        } else {
            showAlert("Please select a categorie.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void goToAssuranceButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AssuranceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }
    }

    public void goToAgenceeButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AgenceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("bACK aGENCE");
            stage.setScene(new Scene(root));


            stage.show();


            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading FrontCategorieGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void goToFrontCATEGORIEButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/FrontCategorieGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Categorie Form");
            stage.setScene(new Scene(root));


            stage.show();


            ((Node) actionEvent.getSource()).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void generatePDF(ActionEvent actionEvent) {
        PDFGenerator1 pdfGenerator1 = new PDFGenerator1();
        pdfGenerator1.generatePDF(actionEvent, categorieTable, "Categorie Table");
    }

    @FXML
    void generateEXCEL(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                ExcelGenerator.generateExcel(categorieTable.getItems(), file);
                showAlert("Excel file generated successfully!");
                Notifications.create().title("Notification").text("Excel file generated successfully!").showInformation();
            }
        } catch (IOException e) {
            showAlert("Error generating Excel file: " + e.getMessage());
        }
    }

}


