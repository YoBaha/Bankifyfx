package controllers;

import controllers.User.ProfileController;
import controllers.creditController.GetCreditFront;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import models.User;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import models.Transaction;

import services.ServiceCredit;
import services.TransactionService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Carte;
import services.CarteService;

import java.util.List;

import static controllers.User.LoginController.user;


public class addTransaction {

    private final TransactionService transactionService = new TransactionService();
    private final CarteService carteService = new CarteService();

    @FXML
    private TextField montantTF;

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
    private Button btnPackages;

    @FXML
    private TextField dateTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField statutTF;
    @FXML
    private Pane pnItems;

    @FXML
    private ComboBox<String> carteComboBox;

    public void initialize() {
        // Load available cartes from the CarteService
        List<Carte> cartes = carteService.getAllCartes();

        // Define a counter to track the number of cards added
        int cardsAdded = 0;

        // Iterate through the cartes list
        for (Carte carte : cartes) {
            // Add the card number (num_c) of filtered Cartes to ComboBox
            String cardNumber = carte.getNum_c();
            carteComboBox.getItems().add(cardNumber);

            // Increment the counter each time a card number is added
            cardsAdded++;

            // Stop the loop after adding the first three cards
            if (cardsAdded == 3) {
                break;
            }
        }
    }


    public void goToProfileButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/profile.fxml"));
            Parent root = loader.load();
            // Get reference to the ProfileController
            ProfileController profileController = loader.getController();

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


    @FXML
    void addTransaction(ActionEvent event) throws SQLException {
        if (montantTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Montant is empty");
            return;
        }
        if (dateTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Date is empty");
            return;
        }
        if (carteComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Please select a Carte");
            return;
        }

        try {
            // Parse montant
            double montant = Double.parseDouble(montantTF.getText());

            // Parse date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateTF.getText());

            // Validate type
            String type = typeTF.getText();
            if (!List.of("paiement", "achat", "retrait").contains(type)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid type. Allowed values: paiement, achat, retrait");
                return;
            }

            // Validate statut
            String statut = statutTF.getText();
            if (!List.of("approuvee", "refusee", "en attente").contains(statut)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid statut. Allowed values: approuvee, refusee, en attente");
                return;
            }

            // Retrieve the selected card number
            String selectedCardNumber = carteComboBox.getValue();

            // Retrieve the corresponding Carte object based on the selected card number
            Carte selectedCarte = null;
            for (Carte carte : carteService.getAllCartes()) {
                if (carte.getNum_c().equals(selectedCardNumber)) {
                    selectedCarte = carte;
                    break;
                }
            }

            // Check if the selected Carte was found
            if (selectedCarte == null) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid Carte selection.");
                return;
            }

            // Create a new transaction with the selected Carte
            Transaction transaction = new Transaction(montant, date, type, statut, selectedCarte);

            // Add the transaction
            transactionService.add(transaction);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format for montant");
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format for date");
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



    /**
     * Method to display alerts to the user.
     *
     * @param alertType The type of alert (e.g., INFORMATION, WARNING, ERROR).
     * @param title The title of the alert dialog.
     * @param content The content or message of the alert.
     */
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional, remove if you want to set a header
        alert.setContentText(content);
        alert.showAndWait();
    }
}
