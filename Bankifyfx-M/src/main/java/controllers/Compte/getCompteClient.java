package controllers.Compte;
import models.CompteClient;
import services.CompteClientService;
import utils.PDFGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import javafx.collections.transformation.FilteredList;
import java.util.*;

import static java.time.zone.ZoneRulesProvider.refresh;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.Statistics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getCompteClient {

    private final Statistics statistics = new Statistics();
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
    private Label maleCountLabel;

    @FXML
    private Label femaleCountLabel;

    @FXML
    private Label otherCountLabel;
    @FXML
    private TableColumn<CompteClient, ImageView> qrCodeColumn;
    @FXML
    private  TextField tfrecherche;

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("sexe"));

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

        qrCodeColumn.setCellFactory(column -> new TableCell<CompteClient, ImageView>() {
            private final ImageView imageView = new ImageView();
            {
                // Set up rendering of the QR code image
                imageView.setFitHeight(115);
                imageView.setFitWidth(115);
            }

            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    Image qrCodeImage = generateQRCodeImage(compteClient.getId());
                    imageView.setImage(qrCodeImage);
                    setGraphic(imageView);
                }
            }
        });

        loadData();
    }


    private void refresh(Set<CompteClient> compte) {
        // Convert the Set of Cartes to an ObservableList
        ObservableList<CompteClient> observableCartes = FXCollections.observableArrayList(compte);

        // Update the TableView with the new data
        compteClientTable.setItems(observableCartes);

        // If you're using a ComboBox for sorting criteria and want to refresh its selection, you can do that here
        // Example: if you want to clear the ComboBox selection

    }
    public void recherche_avance(){


        Set<CompteClient> CompteClientSet = new HashSet<>(CompteClientService.getAllCompte());

        // Refresh the view with the set of Cartes
        refresh(CompteClientSet);
        ObservableList<CompteClient> data= FXCollections.observableArrayList(CompteClientService.getAllCompte());
        FilteredList<CompteClient> filteredList=new FilteredList<>(data, c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(c.getNom().contains(newValue)){
                    return true;
                }
                else if(c.getPrenom().contains(newValue)){
                    return true;
                }
                else if(c.getRib().contains(newValue)){
                    return true;
                }

                else{
                    return false;
                }
            });

            refresh(new HashSet<>(filteredList));
        });
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
        }
    }

    private void handleDelete(CompteClient compteClient) {
        try {
            int compteClientId = compteClient.getId();
            compteClientService.delete(compteClientId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            List<CompteClient> compteClients = compteClientService.getAll();
            compteClientTable.getItems().setAll(compteClients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showSexeStatistics() {
        // Create a stage for the pie chart
        Stage stage = new Stage();
        stage.setTitle("Sexe Statistics");

        // Create an ObservableList for the PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            // Get the sexe statistics from the CompteClientService
            Map<String, Integer> sexeStatistics = compteClientService.getSexeStatistics();

            // Populate the pie chart data from the statistics
            for (Map.Entry<String, Integer> entry : sexeStatistics.entrySet()) {
                String sexe = entry.getKey();
                int count = entry.getValue();
                // Create a pie chart data object
                PieChart.Data data = new PieChart.Data(sexe, count);
                // Add the data to the ObservableList
                pieChartData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }

        // Create the PieChart with the data
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Sexe Distribution");

        // Customize tooltips to display count when hovering over segments
        for (PieChart.Data data : pieChart.getData()) {
            String countText = String.valueOf((int) data.getPieValue());
            Tooltip tooltip = new Tooltip(countText);
            Tooltip.install(data.getNode(), tooltip);
        }

        // Add the PieChart to a layout pane
        StackPane root = new StackPane();
        root.getChildren().add(pieChart);

        // Create a scene with the root pane and set it on the stage
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);

        // Show the stage with the pie chart
        stage.show();
    }


    public void ajouterCompte(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/ajoutCompte.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajout Compte");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void NewCompte(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte/ajouCompteF.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un compte");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generatePDF(ActionEvent actionEvent) {
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generatePDF(actionEvent, compteClientTable, "CompteClient Table");
    }
    private Image generateQRCodeImage(int qrCodeData) {
        int qrCodeSize = 100; // Set the size of the QR code image (reduced size)

        // Title to be displayed when scanning the QR code
        String title = "L'id de ce compte est : ";

        // Combine title and qrCodeData
        String qrCodeText = title + qrCodeData;

        // Create QR code writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        // Convert BitMatrix to BufferedImage
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        WritableImage writableImage = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writableImage.getPixelWriter().setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return writableImage;
    }

}
