package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import services.ServiceCredit;
import controllers.creditController.GetCreditFront;
import models.User;

import javafx.scene.control.*;


import java.io.IOException;
import java.sql.SQLException;

public class FrontController {

    @FXML
    private Button signoutBtn;
    @FXML
    private MenuItem pnItems;

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
    public void goToProfileButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/profile.fxml"));
            Parent root = loader.load();
            // Get reference to the ProfileController
            ProfileController profileController = loader.getController();

            // Pass the current user to the ProfileController
            User currentUser = getCurrentUser(); // Get the current user from FrontController
            profileController.setUser(currentUser);

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
