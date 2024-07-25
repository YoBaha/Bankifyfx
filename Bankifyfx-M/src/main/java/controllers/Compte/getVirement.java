package controllers.Compte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import javafx.util.Callback;
import models.Virement;
import services.VirementService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import utils.PDFGenerator;
import utils.ExcellGenerator;

public class getVirement {

    private final VirementService virementService = new VirementService();

    @FXML
    private TableView<Virement> virementTable;

    @FXML
    private TableColumn<Virement, Integer> idColumn;

    @FXML
    private TableColumn<Virement, String> compteSourceColumn;

    @FXML
    private TableColumn<Virement, String> compteDestinationColumn;

    @FXML
    private TableColumn<Virement, Float> montantColumn;

    @FXML
    private TableColumn<Virement, String> dateColumn;

    @FXML
    private TableColumn<Virement, String> heureColumn;

    @FXML
    private TableColumn<Virement, Void> modifyColumn;

    @FXML
    private TableColumn<Virement, Void> deleteColumn;

    @FXML
    public void initialize() {
        // Configure table columns
        compteSourceColumn.setCellValueFactory(new PropertyValueFactory<>("compte_source"));
        compteDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("compte_destination"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));

        // Configure "Modifier" column
        /*modifyColumn.setCellFactory(new Callback<TableColumn<Virement, Void>, TableCell<Virement, Void>>() {
            @Override
            public TableCell<Virement, Void> call(final TableColumn<Virement, Void> param) {
                return new TableCell<Virement, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            Virement virement = getTableView().getItems().get(getIndex());
                            handleModify(virement);
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
*/
        // Configure "Supprimer" column
  /*      deleteColumn.setCellFactory(column -> new TableCell<Virement, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Virement virement = getTableView().getItems().get(getIndex());
                    handleDelete(virement);
                });
            }*/

          /*  @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
*/
        // Load data into the table
        loadData();
    }

    private void handleModify(Virement virement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modiVirement.fxml"));
            Parent root = loader.load();
            //modiVirement controller = loader.getController();
            // controller.initData(virement);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le virement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void handleDelete(Virement virement) {
        try {
            int virementId = virement.getId();
            virementService.delete(virementId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void loadData() {
        try {
            List<Virement> virements = virementService.getAll();
            virementTable.getItems().setAll(virements);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    public void NewVirement(ActionEvent actionEvent) {
        try {
            // Load the FrontAgence GUI FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/ajoutVirement.fxml"));
            Parent root = loader.load();

            // Create a new stage for the FrontAgence GUI
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Virement");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void NewwVirementF(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutVirementF.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter Virement");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void Excell(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                ExcellGenerator.generateExcel(virementTable.getItems(), file);
                showAlert("Excel file generated successfully!");
                Notifications.create().title("Notification").text("Excel file generated successfully!").showInformation();
            }
        } catch (IOException e) {
            showAlert("Error generating Excel file: " + e.getMessage());
        }
    }
}