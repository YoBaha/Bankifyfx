package controllers;

import javafx.event.ActionEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Carte;
import services.CarteService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import models.CompteClient;
import services.CompteClientService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


public class addCarte {

    private final CarteService carteService = new CarteService();
    @FXML
    private VBox pnItems ;
    @FXML
    private TextField num_cTF;

    @FXML
    private TextField dateExpTF;

    @FXML
    private TextField typeTF;
    public static final String ACCOUNT_SID="AC2b2ae1fb180df37728b965b352c3e5c1";
    public static final String AUTH_TOKEN="88057e39007cd1a59615f78b1799f0b7";

    @FXML
    private TextField statutTF;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

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
    private ComboBox<Integer> compteClientComboBox;

    // Add an instance of CompteClientService to retrieve CompteClient objects
    private final CompteClientService compteClientService = new CompteClientService();

    public void initialize() {
        // Load available CompteClient objects from the CompteClientService
        List<CompteClient> compteClients = compteClientService.getAllCompteClients();



        // Iterate through each CompteClient and add the desired attribute (ID) to the ComboBox
        for (CompteClient compteClient : compteClients) {
            // Retrieve the ID of the CompteClient
            int clientId = compteClient.getId();

            // Add the ID to the ComboBox
            compteClientComboBox.getItems().add(clientId);
        }
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
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }}

    private void showAlert(String s) {
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
    void loadMacarte(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCarte.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }






    @FXML
    void addCarte(ActionEvent event) throws SQLException {
        if (statutTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "statut is empty");
            return;
        }
        // Check for empty fields
        if (num_cTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Card number is empty");
            return;
        }
        if (dateExpTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Expiration date is empty");
            return;
        }
        if (typeTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Type is empty");
            return;
        }
        if (compteClientComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Please select a CompteClient");
            return;
        }

        try {
            // Parse card number
            String cardNumber = num_cTF.getText();

            // Parse expiration date
            java.util.Date dateExp = java.sql.Date.valueOf(dateExpTF.getText());

            // Validate card type
            String type = typeTF.getText();
            if (!List.of("Visa", "Mastercard").contains(type)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid card type. Allowed values: Visa, Mastercard");
                return;
            }

            String type1 = statutTF.getText();
            if (!List.of("bloquee", "active","inactive").contains(type1)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid card statut. Allowed values: bloquee, active,inactive");
                return;
            }


            // Retrieve the selected CompteClient ID from the ComboBox
            int selectedClientId = compteClientComboBox.getValue();

            // Retrieve the corresponding CompteClient object
            CompteClient selectedCompteClient = compteClientService.getById(selectedClientId);

            // Create a new Carte object with the provided information
            Carte newCarte = new Carte(cardNumber, dateExp, type, type1, selectedCompteClient);

            // Add the new Carte using the CarteService
            carteService.add(newCarte);
            Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
            String messageBody = "Vous avez une nouvelle carte avec le numéro: " + newCarte.getNum_c();
            Message message=Message.creator(
                    new PhoneNumber("+21629004726"),
                    new PhoneNumber("+13347814881"),
                    messageBody
            ).create();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Carte added successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format for input");
        } catch (IllegalArgumentException e) {
            System.out.println("Format de date invalide");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de date invalide !");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openAjouterTransactionWindow(ActionEvent event) {
        openWindow(event, "/addCarte.fxml", "Ajouter Transaction");
    }

    /**
     * Opens the Afficher Transactions window.
     *
     * @param event The event that triggered this method.
     */
    public void openAfficherTransactionsWindow(ActionEvent event) {
        openWindow(event, "/getCarte.fxml", "Afficher Transactions");
    }

    /**
     * General method to open a new window based on the provided FXML file and title.
     *
     * @param event    The event that triggered this method.
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param title    The title of the new window.
     */
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