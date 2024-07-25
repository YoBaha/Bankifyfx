package controllers;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.Carte;
import services.CarteService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public class getCarte {

    private final CarteService carteService = new CarteService();

    @FXML
    private TableView<Carte> carteTable;

    @FXML
    private TextField tfrecherche;

    @FXML
    private ComboBox<String> cbtri;

    @FXML
    private VBox pnItems ;
    @FXML
    private Button btnPackages;

    @FXML
    private TableColumn<Carte, Long> idColumn;

    @FXML
    private TableColumn<Carte, String> numColumn;

    @FXML
    private TableColumn<Carte, java.sql.Date> dateExpColumn;

    @FXML
    private TableColumn<Carte, String> typeColumn;

    @FXML
    private TableColumn<Carte, String> statutColumn;

    @FXML
    private TableColumn<Carte, Void> modifyColumn;

    @FXML
    private TableColumn<Carte, Void> deleteColumn;

    @FXML
    private PieChart pieChart;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;



    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;




    public void recherche_avance(){
        Set<Carte> cartesSet = new HashSet<>(carteService.getAllCartes());

        // Refresh the view with the set of Cartes
        refresh(cartesSet);
        ObservableList<Carte> data= FXCollections.observableArrayList(carteService.getAllCartes());
        FilteredList<Carte> filteredList=new FilteredList<>(data, c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(c.getNum_c().contains(newValue)){
                    return true;
                }
                else if(c.getType_c().contains(newValue)){
                    return true;
                }
                else if(c.getStatut_c().contains(newValue)){
                    return true;
                }
                else{
                    return false;
                }
            });

            refresh(new HashSet<>(filteredList));
        });
    }
    private void refresh(Set<Carte> cartes) {
        // Convert the Set of Cartes to an ObservableList
        ObservableList<Carte> observableCartes = FXCollections.observableArrayList(cartes);

        // Update the TableView with the new data
        carteTable.setItems(observableCartes);

        // If you're using a ComboBox for sorting criteria and want to refresh its selection, you can do that here
        // Example: if you want to clear the ComboBox selection

    }

    @FXML
    public void tri(ActionEvent actionEvent) {
        // Get the selected criterion from the ComboBox
        String selectedCriterion = cbtri.getValue();

        // Get the sorted data based on the selected criterion
        TreeSet<Carte> sortedCartes = carteService.sortByCritere(selectedCriterion);

        // Refresh the UI with the sorted data
        refresh(sortedCartes);
    }
    @FXML
    public void initialize() {
        recherche_avance();

        cbtri.setItems(FXCollections.observableArrayList("Number", "Date"));

        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num_c"));
        dateExpColumn.setCellValueFactory(new PropertyValueFactory<>("date_exp"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_c"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut_c"));

        // Configurer la colonne "Modifier"
        modifyColumn.setCellFactory(new Callback<TableColumn<Carte, Void>, TableCell<Carte, Void>>() {
            @Override
            public TableCell<Carte, Void> call(final TableColumn<Carte, Void> param) {
                return new TableCell<Carte, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            // Récupérer la carte de la ligne associée
                            Carte carte = getTableView().getItems().get(getIndex());
                            handleModify(carte);
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

        // Configurer la colonne "Supprimer"
        deleteColumn.setCellFactory(column -> new TableCell<Carte, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Carte carte = getTableView().getItems().get(getIndex());
                    handleDelete(carte);
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

    private void handleModify(Carte carte) {
        try {
            // Charger le fichier FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifCarte.fxml"));

            // Créer la scène de modification
            Parent root = loader.load();

            // Obtenir le contrôleur de modification et passer la carte à modifier
            modifCarte controller = loader.getController();
            controller.initData(carte);

            // Créer un nouveau stage pour la scène de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la carte");

            // Afficher la scène de modification
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void handleDelete(Carte carte) {
        try {
            // Récupérer l'ID de la carte à supprimer
            long carteId = carte.getId();

            // Appeler la méthode `delete` de `CarteService` avec l'ID de la carte
            carteService.delete(carteId);

            // Recharger les données dans la table après suppression
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void loadData() {
        try {
            List<Carte> cartes = carteService.getAll();
            carteTable.getItems().setAll(cartes);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    public void openAjouterTransactionWindow(ActionEvent event) {
        openWindow(event, "/addCarte.fxml", "Ajouter Transaction");
    }

    /**
     * Opens the Afficher Transactions window.
     *
     * @param event The event that triggered this method.
     */
    public void openAfficherCarteWindow(ActionEvent event) {
        openWindow(event, "/getCarte.fxml", "Afficher Transactions");
    }

    public void openAfficherTransactionsWindow(ActionEvent event) {
        openWindow(event, "/getTransactionback.fxml", "Afficher Transactions");
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


    public void showCardTypeStatistics() {
        // Create a stage for the pie chart
        Stage stage = new Stage();
        stage.setTitle("Card Type Statistics");

        // Create an ObservableList for the PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            // Get the card type statistics from the CarteService
            Map<String, Integer> cardTypeStatistics = carteService.getCardTypeStatistics();

            // Populate the pie chart data from the statistics
            for (Map.Entry<String, Integer> entry : cardTypeStatistics.entrySet()) {
                String cardType = entry.getKey();
                int count = entry.getValue();
                // Add the data to the ObservableList
                pieChartData.add(new PieChart.Data(cardType, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }

        // Create the PieChart with the data
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Card Type Distribution");

        // Add the PieChart to a layout pane
        StackPane root = new StackPane();
        root.getChildren().add(pieChart);

        // Create a scene with the root pane and set it on the stage
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);

        // Show the stage with the pie chart
        stage.show();
    }

    public void handleGoToStatistics(ActionEvent event) {
        // Define the FXML file path and the title of the new window
        String fxmlPath = "/getstat.fxml";
        String windowTitle = "les statistiques";

        try {
            // Load the FXML file for the new page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new stage for the new window
            Stage newStage = new Stage();

            // Set the title for the new window
            newStage.setTitle(windowTitle);

            // Create a new scene with the loaded FXML content
            Scene scene = new Scene(root);

            // Set the scene to the new stage
            newStage.setScene(scene);

            // Show the new stage (window)
            newStage.show();
        } catch (IOException e) {
            // Handle any exceptions that may occur during the loading of the FXML file
            e.printStackTrace();
            // Optionally, you can display an alert to the user
            // showAlert(Alert.AlertType.ERROR, "Erreur", "Failed to open window!");
        }
    }

}
