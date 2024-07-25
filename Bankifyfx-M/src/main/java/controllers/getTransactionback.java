package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import models.Transaction;
import services.TransactionService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import utils.Exel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class getTransactionback {

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private Button btnPackages;
    @FXML
    private VBox pnItems ;
    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Long> idColumn;

    @FXML
    private TableColumn<Transaction, Double> montantColumn;

    @FXML
    private TableColumn<Transaction, java.sql.Date> dateColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> statutColumn;

    @FXML
    private TableColumn<Transaction, Void> modifyColumn;

    @FXML
    private TableColumn<Transaction, Void> deleteColumn;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;



    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_t"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_t"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut_t"));

        // Configurer la colonne "Modify"



        // Configurer la colonne "Delete"


        // Charger les données dans la table
        loadData();
    }
    @FXML
    void loadMacarte(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCarte.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the button
            Stage stage = (Stage) btnPackages.getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @FXML
    void Signout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();

            // Get the reference to the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new stage for the login screen
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();

            // Close the current stage
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            try {
                // Load aff.fxml
                FXMLLoader loader = new FXMLLoader();
                URL affFXMLUrl = getClass().getResource("/User/aff.fxml");
                loader.setLocation(affFXMLUrl);
                Pane affPane = loader.load();

                // Set the loaded pane as the background of pnlCustomer
                pnlCustomer.getChildren().setAll(affPane.getChildren());
                pnlCustomer.toFront();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/back.fxml"));
                Parent root = loader.load();

                // Get the reference to the current stage
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                // Create a new stage for the login screen
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Bankify");
                stage.show();

                // Close the current stage
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(actionEvent.getSource()==btnOrders)
        {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }


    }
    public void gotoassuranceback(ActionEvent actionEvent) {
        try {
            // Load AssuranceGUI.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assurance/AssuranceGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

        } catch (IOException e) {
            showAlert1("Error loading AssuranceGUI: " + e.getMessage());
        }}
    private void showAlert1(String s) {
    }


    @FXML
    void generateExcel(ActionEvent event) {
        // Create a FileChooser to select the save location for the Excel file

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        // Show the save dialog and get the file chosen by the user
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        // Check if a file was chosen
        if (file != null) {
            try {
                // Extract the list of transactions from the TableView
                ObservableList<Transaction> transactions = transactionTable.getItems();

                // Generate the Excel file using the list of transactions
                Exel.generateExcel(transactions, file);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Excel file generated successfully!");



            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Error generating Excel file: ");

            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void goToCategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieCreditTemplates/getCategorieCredit.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void handleClicksFF(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/showCompte.fxml"));
            Parent root = loader.load();

            // Create a new stage for the FrontAgence GUI
            Stage stage = new Stage();
            stage.setTitle("Liste des comptes");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleClicksFF1(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/showVirement.fxml"));
            Parent root = loader.load();

            // Create a new stage for the FrontAgence GUI
            Stage stage = new Stage();
            stage.setTitle("Liste des comptes");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    private void fillCheques(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/back.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the button
            Stage stage = (Stage) pnItems.getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }
    private void handleModify(Transaction transaction) {
        try {
            // Charger le fichier FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifTransaction.fxml"));

            // Créer la scène de modification
            Parent root = loader.load();

            // Obtenir le contrôleur de modification et passer la transaction à modifier
            modifTransaction controller = loader.getController();
            controller.initData(transaction);

            // Créer un nouveau stage pour la scène de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la transaction");

            // Afficher la scène de modification
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void handleDelete(Transaction transaction) {
        try {
            // Récupérer l'ID de la transaction à supprimer
            long transactionId = transaction.getId();

            // Appeler la méthode delete de TransactionService avec l'ID de la transaction
            transactionService.delete(transactionId);

            // Recharger les données dans la table après suppression
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }


    private void loadData() {
        try {
            List<Transaction> transactions = transactionService.getAll();
            transactionTable.getItems().setAll(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    public void openAjouterTransactionWindow(ActionEvent event) {
        try {
            // Load the FXML file for the Ajouter Transaction window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTransaction.fxml"));
            Parent root = loader.load();

            // Get the current event's source
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the current scene and stage from the menu item
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();

            // Update the stage with the new scene for Ajouter Transaction
            stage.setTitle("Ajouter Transaction");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openAfficherCartesWindow(ActionEvent event) {
        openWindow(event, "/getCarte.fxml", "Afficher Transactions");
    }


    private void openWindow(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the event source (Button) and retrieve the stage
            Button button = (Button) event.getSource();
            Scene scene = button.getScene();
            Stage stage = (Stage) scene.getWindow();

            // Set the new scene and title on the stage
            stage.setTitle(title);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            // Provide a user-friendly error message or log the error
            System.err.println("Error opening window: " + e.getMessage());
            // Optionally, you can display an alert to the user
            // showAlert(Alert.AlertType.ERROR, "Erreur", "Failed to open window!");
        }
    }

}

