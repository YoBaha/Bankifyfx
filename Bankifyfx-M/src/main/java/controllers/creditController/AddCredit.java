package controllers.creditController;
import controllers.User.ProfileController;
import controllers.categorieCreditController.GetCategorieCredit;
import controllers.categorieCreditController.ListeCredits;
import controllers.categorieCreditController.ModifCategorieCredit;
import javafx.scene.Node;
import models.User;
import models.CategorieCredit;
import models.CompteClient;
import models.Credit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import services.ServiceCategorieCredit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.ServiceCredit;

import java.util.List;

import static controllers.User.LoginController.user;

public class AddCredit {
    private final ServiceCredit service = new ServiceCredit();
    private final ServiceCategorieCredit serviceCategorie = new ServiceCategorieCredit();
    private Credit credit = new Credit();
    @FXML
    private TextField montantTF;

    @FXML
    private RadioButton option1RadioButton;

    @FXML
    private RadioButton option2RadioButton;

    @FXML
    private RadioButton option3RadioButton;

    @FXML
    private ToggleGroup toggleGroup;
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
    private ComboBox<String> categorieComboBox;

    public void initialize() {
        try {
            toggleGroup = new ToggleGroup();
            option1RadioButton.setToggleGroup(toggleGroup);
            option2RadioButton.setToggleGroup(toggleGroup);
            option3RadioButton.setToggleGroup(toggleGroup);
            toggleGroup.selectedToggleProperty().getValue();
            option1RadioButton.setSelected(true);
            credit.setInteret(10);
            credit.setDureeTotale(36);
            List<CategorieCredit> categories = serviceCategorie.getAll();
            ArrayList<String> noms = new ArrayList<>();
            for (CategorieCredit categorie : categories)
                noms.add(categorie.getNom());
            ObservableList<String> list = FXCollections.observableArrayList(noms);
            categorieComboBox.setItems(list);
            if (!categories.isEmpty())
                categorieComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void addCredit(ActionEvent event) throws SQLException{
        credit.setDateC(new Date());
        credit.setAccepted(false);
        credit.setPayed(false);
        CompteClient compte = new CompteClient(1,"Ghazouani","Samer","aaaaaaaaaaaa","aaaaaaaaaaaaaaaaa","+21628352443", 10000.0F,"femme");
        credit.setCompte(compte);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == option1RadioButton) {
                credit.setInteret(10);
                credit.setDureeTotale(36);
            } else if (selectedRadioButton == option2RadioButton) {
                credit.setInteret(15);
                credit.setDureeTotale(48);
            } else if (selectedRadioButton == option3RadioButton) {
                credit.setInteret(20);
                credit.setDureeTotale(60);
            }
        });
        if (categorieComboBox.getSelectionModel().getSelectedItem() != null)
            credit.setCategorie(serviceCategorie.getByNom(categorieComboBox.getSelectionModel().getSelectedItem()));

        if (montantTF.getText().isEmpty()) {
            System.out.println(service.clientExiste(credit.getCompte().getId()));
            System.out.println("Pas de montant saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant");
            alert.showAndWait();
        } else if (!montantTF.getText().matches("[0-9.]+")) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être en chiffre");
            alert.showAndWait();
        } else if (Double.parseDouble(montantTF.getText())<credit.getCategorie().getMinMontant() || Double.parseDouble(montantTF.getText())>credit.getCategorie().getMaxMontant()) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être entre "+credit.getCategorie().getMinMontant()+" et "+credit.getCategorie().getMaxMontant()+" pour la categorie "+credit.getCategorie().getNom());
            alert.showAndWait();
        } else if (!service.clientExiste(credit.getCompte().getId())==false) {
            System.out.println("Client existe");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez déjà un crédit en cours");
            alert.showAndWait();
        } else {
            credit.setMontantTotale(Double.parseDouble(montantTF.getText()));
            service.add(credit);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
}
