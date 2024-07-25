package controllers.Assurance;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.PDFGenerator1;


public class AssuranceController {

    @FXML
    private TableView<Assurance> assuranceTable;
    @FXML
    private TextField infoTF;

    @FXML
    private TextField montantTF;

    @FXML
    private TextField nomassureTF;

    @FXML
    private TextField nombenefTF;


    @FXML
    private TextField typeTF;
    @FXML
    private TableColumn<Assurance, String> typeAssuranceColumn;

    @FXML
    private TableColumn<Assurance, String> nomAssureColumn;

    @FXML
    private TableColumn<Assurance, String> nomBeneficiaireColumn;

    @FXML
    private TableColumn<Assurance, String> montantPrimeColumn;

    @FXML
    private TableColumn<Assurance, String> infoAssuranceColumn;

    @FXML
    private PieChart assurancePieChart;
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";


    @FXML
    void addButton(ActionEvent event) {
        String type = typeTF.getText();
        String nomAssure = nomassureTF.getText();
        String nomBeneficiaire = nombenefTF.getText();
        String montantPrime = montantTF.getText();
        String infoAssurance = infoTF.getText();

        // Validate input
        if (type.length() < 3 || nomAssure.length() < 3 || nomBeneficiaire.length() < 3) {
            showAlert("Fields must have at least 3 characters.");
            return;
        }

        try {
            double montant = Double.parseDouble(montantPrime);
            if (montant <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert("Montant prime must be a valid number greater than 0.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO assurance (type_assurance, nom_assure, nom_beneficiaire, montant_prime, info_assurance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, type);
            stmt.setString(2, nomAssure);
            stmt.setString(3, nomBeneficiaire);
            stmt.setDouble(4, Double.parseDouble(montantPrime));
            stmt.setString(5, infoAssurance);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Refresh TableView by updating the existing data
                assuranceTable.setItems(loadDataIntoTableView());
                updatePieChart();

                // Show notification
                showNotification("ASSURANCE AJOUTER AVEC SUCCES!!!");
            }
        } catch (SQLException e) {
            showAlert("Error adding record: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotification(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showInformation();
    }



    private ObservableList<Assurance> loadDataIntoTableView() {
        ObservableList<Assurance> data = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getString("type_assurance"),
                        rs.getString("nom_assure"),
                        rs.getString("nom_beneficiaire"),
                        rs.getString("montant_prime"),
                        rs.getString("info_assurance")
                );
                data.add(assurance);
            }


            assuranceTable.setItems(data);

        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching records: " + e.getMessage());
            alert.showAndWait();
        }
        return data;
    }


    @FXML
    void showButton(ActionEvent event) {
        ObservableList<Assurance> data = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getString("type_assurance"),
                        rs.getString("nom_assure"),
                        rs.getString("nom_beneficiaire"),
                        rs.getString("montant_prime"),
                        rs.getString("info_assurance")
                );
                data.add(assurance);
            }

            typeAssuranceColumn.setCellValueFactory(new PropertyValueFactory<>("typeAssurance"));
            nomAssureColumn.setCellValueFactory(new PropertyValueFactory<>("nomAssure"));
            nomBeneficiaireColumn.setCellValueFactory(new PropertyValueFactory<>("nomBeneficiaire"));
            montantPrimeColumn.setCellValueFactory(new PropertyValueFactory<>("montantPrime"));
            infoAssuranceColumn.setCellValueFactory(new PropertyValueFactory<>("infoAssurance"));

            assuranceTable.setItems(data);

        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching records: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void goToCategorieeButtonClicked(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/CategorieAssuranceGUI.fxml"));
            Parent root = loader.load();

            // Create  stage
            Stage stage = new Stage();
            stage.setTitle("Categorie Assurance");
            stage.setScene(new Scene(root));


            stage.show();

            // Close  window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception properly in your application
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading FrontCategorieGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void generatePDF(ActionEvent actionEvent) {
        PDFGenerator1 pdfGenerator1 = new PDFGenerator1();
        pdfGenerator1.generatePDF(actionEvent, assuranceTable, "Assurance Table");  // Provide the title
    }



    public static class Assurance {
        private final String typeAssurance;
        private final String nomAssure;
        private final String nomBeneficiaire;
        private final String montantPrime;
        private final String infoAssurance;

        public Assurance(String typeAssurance, String nomAssure, String nomBeneficiaire, String montantPrime, String infoAssurance) {
            this.typeAssurance = typeAssurance;
            this.nomAssure = nomAssure;
            this.nomBeneficiaire = nomBeneficiaire;
            this.montantPrime = montantPrime;
            this.infoAssurance = infoAssurance;
        }

        public String getTypeAssurance() {
            return typeAssurance;
        }

        public String getNomAssure() {
            return nomAssure;
        }

        public String getNomBeneficiaire() {
            return nomBeneficiaire;
        }

        public String getMontantPrime() {
            return montantPrime;
        }

        public String getInfoAssurance() {
            return infoAssurance;
        }
    }

    @FXML
    void updateButton(ActionEvent event) {
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();
        if (selectedAssurance == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to update.");
            alert.showAndWait();
            return;
        }

        String type = typeTF.getText();
        String nomAssure = nomassureTF.getText();
        String nomBeneficiaire = nombenefTF.getText();
        String montantPrime = montantTF.getText();
        String infoAssurance = infoTF.getText();

        // Validate input
        if (type.length() < 3 || nomAssure.length() < 3 || nomBeneficiaire.length() < 3) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields must have at least 3 characters.");
            alert.showAndWait();
            return;
        }

        try {
            double montant = Double.parseDouble(montantPrime);
            if (montant <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Montant prime must be a valid number greater than 0.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE assurance SET type_assurance=?, nom_assure=?, nom_beneficiaire=?, montant_prime=?, info_assurance=? WHERE type_assurance=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, type);
            stmt.setString(2, nomAssure);
            stmt.setString(3, nomBeneficiaire);
            stmt.setDouble(4, Double.parseDouble(montantPrime));
            stmt.setString(5, infoAssurance);
            stmt.setString(6, selectedAssurance.getTypeAssurance()); // Assuming typeAssurance is unique, adjust accordingly if not

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Refresh TableView  updat existing data
                assuranceTable.setItems(loadDataIntoTableView());
                updatePieChart();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating record: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void deleteButton(ActionEvent event) {
        // Get selected item from TableView
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();

        if (selectedAssurance == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a record to delete.");
            alert.showAndWait();
            return;
        }

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Assurance");
        alert.setContentText("Are you sure you want to delete this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM assurance WHERE type_assurance = ? AND nom_assure = ? AND nom_beneficiaire = ? AND montant_prime = ? AND info_assurance = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, selectedAssurance.getTypeAssurance());
                stmt.setString(2, selectedAssurance.getNomAssure());
                stmt.setString(3, selectedAssurance.getNomBeneficiaire());
                stmt.setString(4, selectedAssurance.getMontantPrime());
                stmt.setString(5, selectedAssurance.getInfoAssurance());

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    // Remove selected item from TableView
                    assuranceTable.getItems().remove(selectedAssurance);
                    updatePieChart();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Record deleted successfully.");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Error deleting record.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error deleting record: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }


    @FXML
    public void goToAgenceButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AgenceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Agence Form");
            stage.setScene(new Scene(root));


            stage.show();

            // Close window
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading AgenceGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void goToFrontAssuranceButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/FrontAssuranceGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Front Assurance Form");
            stage.setScene(new Scene(root));


            stage.show();


            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading FrontAssuranceGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void loadAssuranceTypeDistribution() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT type_assurance, COUNT(*) AS frequency FROM assurance GROUP BY type_assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type_assurance");
                int frequency = rs.getInt("frequency");
                pieChartData.add(new PieChart.Data(type, frequency));
            }

            assurancePieChart.setData(pieChartData);
            assurancePieChart.setPrefSize(350, 400);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    @FXML
    public void initialize() {
        updatePieChart();
    }
    private void updatePieChart() {
        assurancePieChart.getData().clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT type_assurance, COUNT(*) AS count FROM assurance GROUP BY type_assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type_assurance");
                int count = rs.getInt("count");
                assurancePieChart.getData().add(new PieChart.Data(type, count));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }

    }
}

