package controllers.Assurance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import models.CategorieAssurance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.*;


public class FrontCategorieController {

    @FXML
    private TableView<CategorieAssurance> categorieTable;

    @FXML
    private ImageView frontimg;


    private ObservableList<CategorieAssurance> categories = FXCollections.observableArrayList();

    private static final String URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    public void initialize() {

        TableColumn<CategorieAssurance, Integer> idCol = new TableColumn<>("ID");
        TableColumn<CategorieAssurance, String> nomCategorieCol = new TableColumn<>("Nom Categorie");
        TableColumn<CategorieAssurance, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<CategorieAssurance, String> typeCouvertureCol = new TableColumn<>("Type Couverture");
        TableColumn<CategorieAssurance, String> agenceResponsableCol = new TableColumn<>("Agence Responsable");


        idCol.setCellValueFactory(new PropertyValueFactory<>("idCategorie"));
        nomCategorieCol.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCouvertureCol.setCellValueFactory(new PropertyValueFactory<>("typeCouverture"));
        agenceResponsableCol.setCellValueFactory(new PropertyValueFactory<>("agenceResponsable"));

        // Add columns to TableView
        categorieTable.getColumns().addAll(idCol, nomCategorieCol, descriptionCol, typeCouvertureCol, agenceResponsableCol);

        // Popu TableView data
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT * FROM categorie_assurance";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategorieAssurance categorie = new CategorieAssurance(
                        rs.getInt("id_categorie"),
                        rs.getString("nom_categorie"),
                        rs.getString("description"),
                        rs.getString("TypeCouverture"),
                        rs.getString("agenceResponsable")
                );
                categories.add(categorie);
            }

            // Closeconnections
            rs.close();
            stmt.close();
            conn.close();

            // Set items TableView
            categorieTable.setItems(categories);

        } catch (SQLException e) {
            e.printStackTrace();

        }


    }

    public void goToFrontAssuranceeButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/FrontAssuranceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Assurance Form");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close  needed

            ((Node) actionEvent.getSource()).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void goToFrontAgenceeButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/FrontAgenceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Front Agence");
            stage.setScene(new Scene(root));


            stage.show();

            // Close  window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading FrontAgenceGUI: " + e.getMessage());
            alert.showAndWait();
        }}
}
