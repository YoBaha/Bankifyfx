package controllers;

import controllers.User.ProfileController;
import controllers.creditController.GetCreditFront;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.User;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import models.Transaction;
import services.ServiceCredit;
import services.TransactionService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import utils.PDF;

import static controllers.User.LoginController.user;

public class getTransaction {

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Long> idColumn;

    @FXML
    private TableColumn<Transaction, Double> montantColumn;
    private User currentUser; // Store the current user information

    // Setter method to set the current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Getter method to get the current user
    public User getCurrentUser() {
        return currentUser;
    }

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
    private Pane pnItems;


    public void handleClicksFF(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/showVirementF.fxml"));
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

    public void goToProfileButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/profile.fxml"));
            Parent root = loader.load();
            // Get reference to the ProfileController
            ProfileController profileController = loader.getController();

            // Pass the current user to the ProfileController
            profileController.setUser(user);

            // Get the current event's source
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the current scene and stage from the menu item
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();


            stage.setTitle("Profile");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
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


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    @FXML
    void signout(ActionEvent event) {
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
    public void handleClicksFF1(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/showCompteF.fxml"));
            Parent root = loader.load();
            MenuItem menuItem = (MenuItem) actionEvent.getSource();
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(root));

            // Close the current window
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void fillCheques(ActionEvent event) {
        // Check if the content is already loaded

            // Load Cheques.fxml into panel_scroll VBox when the button is clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Front/FrontCheques.fxml"));
                Node chequesNode = loader.load();
                pnItems.getChildren().setAll(chequesNode);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the FXML file cannot be loaded
            }

    }

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_t"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_t"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut_t"));

        // Configurer la colonne "Modify"
        modifyColumn.setCellFactory(new Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>>() {
            @Override
            public TableCell<Transaction, Void> call(final TableColumn<Transaction, Void> param) {
                return new TableCell<Transaction, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setStyle("-fx-background-color: #87CEFA; -fx-text-fill: white; -fx-border-color: #FFC0CB; /* Matching border color */         -fx-border-radius: 5; /* Slightly rounded border corners */         -fx-background-radius: 5; /* Slightly rounded background corners */         -fx-font-weight: bold; /* Bold text */         -fx-padding: 8px 16px;");
                        modifyButton.setOnAction(event -> {
                            // Récupérer la transaction de la ligne associée
                            Transaction transaction = getTableView().getItems().get(getIndex());
                            handleModify(transaction);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            }
        });
        // Configurer la colonne "Delete"
        deleteColumn.setCellFactory(column -> new TableCell<Transaction, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #FF6666; -fx-text-fill: white; -fx-border-color: #FFC0CB; /* Matching border color */         -fx-border-radius: 5; /* Slightly rounded border corners */         -fx-background-radius: 5; /* Slightly rounded background corners */         -fx-font-weight: bold; /* Bold text */         -fx-padding: 8px 16px;");
                deleteButton.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    handleDelete(transaction);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Charger les données dans la table
        loadData();
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
    @FXML
    private void demanderCredit(ActionEvent event) throws IOException {
        try {
            if (!new ServiceCredit().clientExiste(1)==false) {
                System.out.println("Client existe");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez déjà un crédit en cours");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/addCredit.fxml"));
                    Parent root = loader.load();
                    MenuItem menuItem = (MenuItem) event.getSource();
                    Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
                    Stage stage = (Stage) scene.getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void consulterCredit(ActionEvent event) throws IOException {
        try {
            if (new ServiceCredit().clientExiste(1)==false) {
                System.out.println("Client n'existe pas");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Vous n'avez pas un crédit");
                alert.showAndWait();
            } else if (new ServiceCredit().getByClient(1).isAccepted()==false) {
                System.out.println("Demande en cours");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Votre demande est en attente");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditFront.fxml"));
                    Parent root = loader.load();
                    MenuItem menuItem = (MenuItem) event.getSource();
                    GetCreditFront controller = loader.getController();
                    controller.initData(1);
                    Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
                    Stage stage = (Stage) scene.getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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


    public void openAfficherTransactionsWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getTransaction.fxml"));
            Parent root = loader.load();

            // Get the source of the event
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the scene from the source, and then get the stage from the scene
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();

            // Set the new scene on the existing stage
            stage.setTitle("Afficher Transactions");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception (e.g., log it)
        }
    }

    public void generatePDF(ActionEvent actionEvent) {
        // Create a new TableView with only the desired columns (montant, date, type, and statut)
        TableView<Transaction> filteredTransactionTable = new TableView<>();

        // Add only the columns you want to include in the PDF
        for (TableColumn<Transaction, ?> column : transactionTable.getColumns()) {
            String columnName = column.getText().toLowerCase();
            if (columnName.equals("montant") || columnName.equals("date") || columnName.equals("type") || columnName.equals("statut")) {
                // Add the matching column to the filtered table
                filteredTransactionTable.getColumns().add(column);
            }
        }

        // Copy all the items from the original table to the filtered table
        filteredTransactionTable.getItems().addAll(transactionTable.getItems());

        // Create a PDF generator object and generate the PDF using the filtered table
        PDF pdfGenerator = new PDF();
        pdfGenerator.generatePDF(actionEvent, filteredTransactionTable, "Table des transactions");  // Provide the title
    }


}
