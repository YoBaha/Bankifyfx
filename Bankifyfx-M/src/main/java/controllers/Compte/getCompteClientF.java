package controllers.Compte;

import controllers.Cheques.Frontcheques;
import models.User;
import controllers.User.ProfileController;
import controllers.creditController.GetCreditFront;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.CompteClient;
import services.CompteClientService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import services.ServiceCredit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static controllers.User.LoginController.user;

public class getCompteClientF {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private TableView<CompteClient> compteClientTable;



    @FXML
    private TableColumn<CompteClient, String> nomColumn;

    @FXML
    private TableColumn<CompteClient, String> prenomColumn;

    @FXML
    private TableColumn<CompteClient, String> ribColumn;

    @FXML
    private TableColumn<CompteClient, String> mailColumn;

    @FXML
    private TableColumn<CompteClient, String> telColumn;

    @FXML
    private TableColumn<CompteClient, Float> soldeColumn;
    @FXML
    private TableColumn<CompteClient, String> sexeColumn;

    @FXML
    private TableColumn<CompteClient, Void> modifyColumn;

    @FXML
    private TableColumn<CompteClient, Void> deleteColumn;

    @FXML
    private TableColumn<CompteClient, Void> ChequeColumn;

    @FXML
    private VBox panel_scroll;

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
    public void initialize() {
        // Configure table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        // Configure "Modifier" column
        modifyColumn.setCellFactory(new Callback<TableColumn<CompteClient, Void>, TableCell<CompteClient, Void>>() {
            @Override
            public TableCell<CompteClient, Void> call(final TableColumn<CompteClient, Void> param) {
                return new TableCell<CompteClient, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            CompteClient compteClient = getTableView().getItems().get(getIndex());
                            handleModify(compteClient);
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

        // Configure "Supprimer" column
        deleteColumn.setCellFactory(column -> new TableCell<CompteClient, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    handleDelete(compteClient);
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
        ChequeColumn.setCellFactory(column -> new TableCell<CompteClient, Void>() {
            private final Button ChequeButton = new Button("Cheques");

            {
                ChequeButton.setOnAction(event -> {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    Frontcheques.idC=compteClient.getId();


                        // Load Cheques.fxml into panel_scroll VBox when the button is clicked
                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Front/FrontCheques.fxml"));
                            Node chequesNode = loader.load();
                            panel_scroll.getChildren().setAll(chequesNode);
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle the exception if the FXML file cannot be loaded
                        }






                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(ChequeButton);
                }
            }
        });

        // Load data into the table
        loadData();
    }

    private void handleModify(CompteClient compteClient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/modiCompte.fxml"));
            Parent root = loader.load();
            modiCompte controller = loader.getController();
            controller.initData(compteClient);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le compte client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here...
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

    private void handleDelete(CompteClient compteClient) {
        try {
            int compteClientId = compteClient.getId();
            compteClientService.delete(compteClientId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void loadData() {
        try {
            List<CompteClient> compteClients = compteClientService.getAll();
            compteClientTable.getItems().setAll(compteClients);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    public void ajouterCompte(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/ajoutCompte.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajout Compte");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void NewCompte(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/ajouCompteF.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter un compte");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    @FXML
    private void fillCheques(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Front.fxml"));
            Parent root = loader.load();
            MenuItem menuItem = (MenuItem) event.getSource();
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }
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
}