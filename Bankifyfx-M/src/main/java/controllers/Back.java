package controllers;

import models.Credit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.ServiceCredit;

import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Back  {

    @FXML
    private VBox pnItems ;
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
    private final ServiceCredit service = new ServiceCredit();

    @FXML
    private TableView<Credit> creditsTable;

    @FXML
    private TableColumn<Credit, Integer> idColumn;

    @FXML
    private TableColumn<Credit, Double> montantTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> dureeTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> interetColumn;

    @FXML
    private TableColumn<Credit, java.sql.Date> dateCColumn;

    @FXML
    private TableColumn<Credit, String> categorieColumn;

    @FXML
    private TableColumn<Credit, String> statusColumn;

    @FXML
    private TextField tfrecherche;


    @FXML
    public void initialize() throws SQLException {
        recherche();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotale"));
        dureeTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("dureeTotale"));
        interetColumn.setCellValueFactory(new PropertyValueFactory<>("interet"));
        dateCColumn.setCellValueFactory(new PropertyValueFactory<>("dateC"));
        categorieColumn.setCellValueFactory(cellData -> {
            Credit credit = cellData.getValue();
            if (credit != null && credit.getCategorie() != null) {
                return new SimpleStringProperty(credit.getCategorie().getNom());
            } else {
                return new SimpleStringProperty("");
            }
        });
        statusColumn.setCellValueFactory(cellData -> {
            Credit credit = cellData.getValue();
            if (credit != null && !credit.isAccepted()) {
                return new SimpleStringProperty("En attente");
            } else if (credit != null && credit.isPayed()) {
                return new SimpleStringProperty("Payé");
            } else {
                return new SimpleStringProperty("En cours");
            }
        });
        loadData();
    }

    private void loadData() {
        try {
            List<Credit> credits = service.getCreditsAccepted();
            creditsTable.getItems().setAll(credits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recherche() throws SQLException {
        Set<Credit> credits = new HashSet<>(service.getAll());
        refresh(credits);
        ObservableList<Credit> data= FXCollections.observableArrayList(service.getAll());
        FilteredList<Credit> filteredList=new FilteredList<>(data, c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(String.valueOf(c.getId()).contains(newValue)){
                    return true;
                } else if(String.valueOf(c.getMontantTotale()).contains(newValue)){
                    return true;
                } else if(String.valueOf(c.getDureeTotale()).contains(newValue)){
                    return true;
                } else if(String.valueOf(c.getInteret()).contains(newValue)){
                    return true;
                } else if(String.valueOf(c.getDateC()).contains(newValue)){
                    return true;
                } if(c.getCategorie().getNom().contains(newValue)){
                    return true;
                } else {
                    return false;
                }
            });
            refresh(new HashSet<>(filteredList));
        });
    }
    private void refresh(Set<Credit> credits) {
        ObservableList<Credit> observableCredits = FXCollections.observableArrayList(credits);
        creditsTable.setItems(observableCredits);
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
    private void fillCheques(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/back.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

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
    private void goToBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToAttente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditAttente.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAccepted(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditAccepted.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToPayed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditPayed.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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

}