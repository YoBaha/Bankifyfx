package controllers.Cheques;


import models.Cheques.Cheque;
import models.Cheques.Reclamation;
import models.User;
import Cheques.Cheques;
import Cheques.Reclamations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


import java.util.List;

import static controllers.Cheques.cheques.list;

public class Frontcheques {


 public static int idC=1;

    @FXML
    private ComboBox<User> DestinationC1;

    @FXML
    private ComboBox<User> DestinationC;
    @FXML
    private Tab cheque;
    @FXML
    private TextField MontantC;
    @FXML
    private TextField MontantC1;

    @FXML
    private TextField ID1;

    @FXML
    private Tab updateview;
    @FXML
    private Tab ajouterview;
    @FXML
    private Button pdf;

    @FXML
    private TableView<Cheque> chequeTableView;

    @FXML
    private TableColumn<Cheque, Integer> id;


    @FXML
    private TableColumn<Cheque, Integer> compteID;
    @FXML
    private TableColumn<Cheque, Integer> isFav;

    @FXML
    private TableColumn<Cheque, String> destinationC;

    @FXML
    private TableColumn<Cheque, Double> Montant;

    @FXML
    private TableColumn<Cheque, Date> DateE;

    @FXML
    private TableColumn<Cheque, Void> Reclamation;

    @FXML
    private TableColumn<Cheque, Void> Actions;

    @FXML
    private Button List;

    @FXML
    private ComboBox<String> Categorie;

    @FXML
    private Tab updateViewRec;

    @FXML
    private ComboBox<String> Categorie1;




    @FXML
    private Label montantValidationLabel;



    @FXML
    private Label comboBox2ValidationLabel;

    @FXML
    private Label montantValidationLabel1;
    @FXML
    private Label comboBox2ValidationLabel1;


    @FXML
    private ImageView notificationBell;



    @FXML
    private Label comboBox1ValidationLabel3;

    @FXML
    private Button Refresh;

    @FXML
    private Button hoverButton;

    private Timeline animationTimeline;




    Cheques c = new Cheques();

    Reclamations R=new Reclamations();

    Reclamation R1=new Reclamation();

    String filePath = "C:\\reclamationList.json";


    public Frontcheques() throws IOException {
        list = new ArrayList<>();
        loadListFromFile();
    }

    // Save list to file


    // Load list from file
    public void saveListToFile() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Reclamation.class, new typeadapter())
                .create();

        String json = gson.toJson(list);
        Files.write(Paths.get(filePath), json.getBytes());
    }
    public void loadListFromFile() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Reclamation.class, new typeadapter())
                .create();

        String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
        Type listType = new TypeToken<ArrayList<Reclamation>>(){}.getType();
        list = gson.fromJson(jsonString, listType);
    }






    @FXML
    public void   updatefill(int idCheque) {
populateComboBox();
        try {
            Cheque ch = c.update1(idCheque);
            ID1.setText(String.valueOf(ch.getId()));

            MontantC1.setText(String.valueOf(ch.getMontantC()));

            int destinationC = ch.getDestinationCID();
            ObservableList<User> items1 = DestinationC1.getItems();

            for (int i = 0; i < items1.size(); i++) {
                if (!items1.get(i).toString().equals("..")) {
                    int currentItem = items1.get(i).getId();
                    if (currentItem == destinationC) {
                        DestinationC1.getSelectionModel().select(i);
                        break;
                    }
                }
            }





        } catch (SQLException e) {
            e.printStackTrace();
        }}
    private void updatefillRec(int id) {
        populateREC();
        try {
            Reclamation ch = R.getReclamationById(id);

            ObservableList<String> items = Categorie1.getItems();
            for (int i = 0; i < items.size(); i++) {
                String currentItem = items.get(i);
                if (Objects.equals(currentItem, ch.getCategorie())) {
                    Categorie1.getSelectionModel().select(i);
                    break;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void update() {
        float montantC = 0;
        try {
            montantC = Float.parseFloat(MontantC1.getText());

            User selectedUserData = (User) DestinationC1.getValue();
            int destinationC = selectedUserData.getId();

            Cheque updatedCheque = new Cheque();
            updatedCheque.setId(Integer.parseInt(ID1.getText()));
            updatedCheque.setMontantC(montantC);
            updatedCheque.setDestinationCID(selectedUserData.getId());

            c.update1(updatedCheque);
            montantValidationLabel1.setText("");
            comboBox2ValidationLabel1.setText("");

            boolean isComboBox22Valid = !DestinationC1.getValue().getNom().equals("..");

            if (!isComboBox22Valid) {
                comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
            }


            System.out.println("Cheque updated successfully!");
            updateview.setDisable(true);
            cheque.getTabPane().getSelectionModel().select(cheque);

        } catch (SQLException ex) {
            System.out.println("Error occurred while updating the cheque: " + ex.getMessage());
            throw new RuntimeException(ex);
        } catch (NullPointerException e) {
            comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");
        } catch (NumberFormatException ex) {
            comboBox2ValidationLabel1.setText("");
            montantValidationLabel1.setText("");


            float defaultMonatantC = 0.0f;
            boolean isMonatantValid = montantC != defaultMonatantC;

            try {
                if (!isMonatantValid) {
                    montantValidationLabel1.setText("Montant invalide");


                    boolean isComboBox2Valid = !DestinationC1.getValue().getNom().equals("..");

               if (!isComboBox2Valid) {
                        comboBox2ValidationLabel1.setText("");

                        comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");
                    }


                } else {
                    montantValidationLabel1.setText("");
                    boolean isComboBox22Valid = !DestinationC1.getValue().getNom().equals("..");
                    System.out.println(isComboBox22Valid);

                    if ( !isComboBox22Valid) {

                        comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");

                    }


                }
            } catch (NullPointerException e) {
                comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");

            }


        }


    }

    @FXML
    public void updateRec() throws SQLException {
        // try {

        String Categorie = Categorie1.getValue();




        R1.setCategorie(Categorie);



        try {
            R.update(R1);
            updateViewRec.setDisable(true);
            cheque.getTabPane().getSelectionModel().select(cheque);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
    // catch (NumberFormatException ex) {
    //  montantValidationLabel.setText("Montant Invalide: Please enter a valid number.");
    // }

    @FXML
    public void populateComboBox() {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankify", "root", "");
            try (Statement statement1 = connection.createStatement();
                 ResultSet resultSet1 = statement1.executeQuery("SELECT id,nom, prenom,email,genre,datenaissance FROM user")) {
                List<User> items3 = new ArrayList<>();
                List<User> items2 = new ArrayList<>();
                User m=new User(0,"..","..","..",null,"..");
                items2.add(m);


                while (resultSet1.next()) {
                    int id = resultSet1.getInt("id");
                    String name = resultSet1.getString("nom") + " " + resultSet1.getString("prenom");

                    items2.add(new User(id,resultSet1.getString("nom"),resultSet1.getString("prenom"),resultSet1.getString("email"),resultSet1.getDate("datenaissance"),resultSet1.getString("genre")));
                    items3.add(new User(id,resultSet1.getString("nom"),resultSet1.getString("prenom"),resultSet1.getString("email"),resultSet1.getDate("datenaissance"),resultSet1.getString("genre")));


                }

                // Add items from the list to comboBox2


                ObservableList<User> observableItems = FXCollections.observableArrayList(items2);
                ObservableList<User> observableItems1 = FXCollections.observableArrayList(items3);


// Assuming comboBox2 is your ComboBox<User>
                DestinationC.setItems(observableItems);
                DestinationC1.setItems(observableItems1);

                //combox11.setModel(model1);

            }

            // Populate comboBox1 with data from the Compte table



            // Close the connection
            connection.close();

            // Populate comboBox2 with data from the user table

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void populateREC() {
        if ( Categorie.getItems().isEmpty()) {
            ObservableList<String> items = Categorie.getItems();
            items.add("Payment Discrepancy");
            items.add("Non-Receipt");
            items.add("Delayed Processing");
            items.add("Forgery");
        }

        if ( Categorie1.getItems().isEmpty()) {
            ObservableList<String> items111 = Categorie1.getItems();
            items111.add("Payment Discrepancy");
            items111.add("Non-Receipt");
            items111.add("Delayed Processing");
            items111.add("Forgery");
        }

    }



    @FXML
    void add() {


        float MonatantC = 0;
        try {
            Cheque a = new Cheque();
            a.setIsFav(0);
            LocalDate currentDate = LocalDate.now();
            Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            a.setDateEmission(date);
            MonatantC = Float.parseFloat(MontantC.getText());


            User selectedUser = DestinationC.getValue();

            montantValidationLabel.setText("");
            comboBox2ValidationLabel.setText("");
            boolean isComboBox22Valid = !DestinationC.getValue().getNom().equals("..");

            if (!isComboBox22Valid) {
                comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
            }



            a.setMontantC(MonatantC);
            a.setDestinationCID(selectedUser.getId());
            a.setCompteID(idC);


            c.add(a);
            cheque.getTabPane().getSelectionModel().select(cheque);


        } catch (SQLException ex) {


        } catch (NullPointerException e) {
            comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
        } catch (NumberFormatException ex) {
            comboBox2ValidationLabel.setText("");
            montantValidationLabel.setText("");



            float defaultMonatantC = 0.0f;
            boolean isMonatantValid = MonatantC != defaultMonatantC;

            try {
                if (!isMonatantValid) {
                    montantValidationLabel.setText("Montant invalide");


                boolean isComboBox2Valid = !DestinationC.getValue().getNom().equals("..");

                if (!isComboBox2Valid) {
                    comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
                }


            }
                else
                {            montantValidationLabel.setText("");

                }
        }catch (NullPointerException e) {
                comboBox2ValidationLabel.setText("Please select a valid value for Destination.");

            }

        }
    }

    @FXML
void addfill()
{
    DestinationC.getSelectionModel().select(new User(0,"..","..","..",null,".."));

}
    @FXML
    void addRec() throws SQLException {
        String categorie = null;
        try {



            categorie = Categorie.getValue();


            R1.setCategorie(categorie);
            R1.setStatutR("En Cours");


            R.add(R1);
            ajouterview.setDisable(true);
            cheque.getTabPane().getSelectionModel().select(cheque);
            comboBox1ValidationLabel3.setText("");


        } catch (NullPointerException | SQLIntegrityConstraintViolationException e  ) {
            boolean isComboBox11Valid = categorie != null;
            if(! isComboBox11Valid)
            {
                comboBox1ValidationLabel3.setText("Please select a valid value for Categorie.");
                }


        }
    }


    @FXML
    public void action()throws SQLException
    {  Refresh.setOnAction(event -> {
        try {
            afficher(c.getAll(idC));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chequeTableView.setRowFactory(tv -> new TableRow<Cheque>() {
                    @Override
                    protected void updateItem(Cheque item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            // Apply style for empty rows

                        } else {
                            // Apply style for non-empty rows
                            setStyle("   -fx-alignment: CENTER; /* Center align column content */" +
                                    "    -fx-font-size: 14px; /* Column font size */" +
                                    "    -fx-text-fill: #333333; /* Column text color */" +
                                    "    -fx-background-color: #ffffff; /* Column background color */" +
                                    "    -fx-border-color: #ccc; /* Column border color */" +
                                    "    -fx-border-width: 1px; /* Column border width */" +
                                    "    -fx-border-radius: 5px; /* Column border radius */;");// Clear any previous styles
                        }
                    }
                });


    });

        List.setOnAction(event -> {
            try {
                afficher(c.getAll(idC,1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            chequeTableView.setRowFactory(tv -> new TableRow<Cheque>() {
                @Override
                protected void updateItem(Cheque item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        // Apply style for empty rows

                    } else {
                        // Apply style for non-empty rows
                        setStyle("   -fx-alignment: CENTER; /* Center align column content */" +
                                "    -fx-font-size: 14px; /* Column font size */" +
                                "    -fx-text-fill: #333333; /* Column text color */" +
                                "    -fx-background-color: #ffffff; /* Column background color */" +
                                "    -fx-border-color: #ccc; /* Column border color */" +
                                "    -fx-border-width: 1px; /* Column border width */" +
                                "    -fx-border-radius: 5px; /* Column border radius */;");// Clear any previous styles
                    }
                }
            });


        });

        pdf.setOnAction(event -> {
            try {
                generateChequeListPDF(c.getAll(idC));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        });






    }

    @FXML
    public void afficher(List <Cheque> L) throws SQLException, IOException {
        // Assuming c.getAll() returns a list of Cheque objects
        ObservableList<Cheque> chequeList = FXCollections.observableArrayList(L);
        chequeTableView.setItems(chequeList);
        for (Reclamation reclamation : list) {
            System.out.println(reclamation.toString());  // Assuming Reclamation has a proper toString method
        }



        ContextMenu contextMenu = new ContextMenu();

// Create MenuItems and add them to the ContextMenu
        for (Reclamation cheque : list) {
            MenuItem menuItem = new MenuItem("ID: " + cheque.getId() + ", Montant: " + cheque.getCategorie());
            contextMenu.getItems().add(menuItem);
        }

// Set the ContextMenu to the ImageView
        notificationBell.setOnMouseClicked(event -> {
            contextMenu.show(notificationBell, event.getScreenX(), event.getScreenY());
        });






        id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        compteID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCompteID()));
        DateE.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateEmission()));

        destinationC.setCellValueFactory(cellData -> {
            int Destination = cellData.getValue().getDestinationCID(); // Assuming getMontantC() returns an Integer
            return new SimpleStringProperty(String.valueOf(Destination));
        });

        Montant.setCellValueFactory(cellData -> {
            float montant = cellData.getValue().getMontantC(); // Assuming getMontantC() returns a float
            return new SimpleDoubleProperty(montant).asObject();
        });
        isFav.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIsFav()));

        Actions.setCellFactory(param -> new TableCell<Cheque, Void>() {
            final VBox buttonsContainer = new VBox();
            private final Button updateButton = new Button("Update");

            private final Button ADD = new Button("Add Reclamation");

            private final Button delete = new Button("Delete");

            // Assuming the image file is located in the same directory as your controller class

            private final ImageView Fav = new ImageView();




            private final ContextMenu contextMenu = new ContextMenu();

            private final ImageView notificationBell1=new ImageView();

            final File file1 = new File("C:\\Users\\balho\\Desktop\\bankify\\src\\main\\resources\\Cheques\\Front\\Images\\star.png");
            final File file2 = new File("C:\\Users\\balho\\Desktop\\bankify\\src\\main\\resources\\Cheques\\Front\\Images\\star1.png");
            final Image image1 = new Image(file1.toURI().toString());
            final Image image2 = new Image(file2.toURI().toString());

            final File file = new File("C:\\Users\\balho\\Desktop\\bankify\\src\\main\\resources\\Cheques\\Front\\Images\\bell.png");

            final File file11 = new File("C:\\Users\\balho\\Desktop\\bankify\\src\\main\\resources\\Cheques\\Front\\Images\\notification.png");



            final Image image = new Image(file.toURI().toString());


            final Image image11 = new Image(file11.toURI().toString());








            private void populateMenuButton(Cheque cheque) throws IOException {





                // Clear existing MenuItems
                contextMenu.getItems().clear();

                int s = 0;

                // Check if there's a notification for the current Cheque
                boolean hasNotification = false;
                for (Reclamation Rec : list) {
                    if (Rec.getChequeId() == cheque.getId()) {
                        hasNotification = true;
                        break;
                    }
                }

                // Set the image based on the presence of a notification
                if (hasNotification) {
                    notificationBell1.setImage(image11);
                } else {
                    notificationBell1.setImage(image);
                }

                notificationBell1.setFitWidth(30);  // Set the width
                notificationBell1.setFitHeight(30); // Set the height


                // Populate MenuButton with notifications for the current Cheque
                for (Reclamation Rec : list) {
                    if (Rec.getChequeId() == cheque.getId()) {
                        MenuItem menuItem = new MenuItem("La Reclamation ID: " + Rec.getId() + "a été Traitée" );
                        menuItem.setOnAction(event -> {
                            // Remove the Reclamation item from the list
                            list.remove(Rec);

                            // Refresh the MenuButton
                            try {
                                populateMenuButton(cheque);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        contextMenu.getItems().add(menuItem);
                    }
                }

                // Add "Clear Notifications" menu item
                MenuItem clearMenuItem = new MenuItem("Clear Notifications");
                clearMenuItem.setOnAction(event -> {
                    // Remove all Reclamation items associated with the current Cheque
                    list.removeIf(Rec -> Rec.getChequeId() == cheque.getId());

                    // Refresh the MenuButton
                    try {
                        populateMenuButton(cheque);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                contextMenu.getItems().add(clearMenuItem);
                saveListToFile();
            }






            {

                notificationBell1.setOnMouseClicked(event -> {
                    Cheque currentCheque = getTableView().getItems().get(getIndex());
                    try {
                        populateMenuButton(currentCheque);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    contextMenu.show(notificationBell1, event.getScreenX(), event.getScreenY());
                });








                updateButton.setOnAction(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());

                    updatefill(cheque.getId());
                    updateview.setDisable(false);
                    updateview.getTabPane().getSelectionModel().select(updateview);
                    try {
                        afficher(L);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("Update clicked for Cheque with ID: " + cheque.getId());
                });




                ADD.setOnAction(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());

                    R1.setChequeId(cheque.getId());
                    R1.setCategorie("..");
                    R1.setStatutR("..");
                    ajouterview.setDisable(false);
                    ajouterview.getTabPane().getSelectionModel().select(ajouterview);
                    Categorie.getItems().clear();
                    try {
                        afficher(L);
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("Update clicked for Cheque with ID: " + cheque.getId());

                    getTableView().refresh();
                });

                delete.setOnAction(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());

                    try {
                        c.delete(cheque.getId());

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    getTableView().refresh();
                    System.out.println("deleted cheque with id : " + cheque.getId());





                });

                Fav.setOnMouseClicked(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());

                    if(cheque.getIsFav()==0)
                    {
                        cheque.setIsFav(1);
                        try {
                            c.updateFav(cheque);



                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        getTableView().refresh();
                    } else {cheque.setIsFav(0);
                        try {
                            c.updateFav(cheque);



                        } catch (SQLException e ) {
                            throw new RuntimeException(e);
                        }
                        getTableView().refresh();}
                });




                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                ADD.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                delete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                updateButton.getStyleClass().add("custom-button");
                ADD.getStyleClass().add("custom-button");
                delete.getStyleClass().add("custom-button");
                buttonsContainer.getChildren().addAll(notificationBell1, Fav, updateButton, ADD, delete);



            }



            @Override
            protected void updateItem(Void  item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Cheque cheque = getTableView().getItems().get(getIndex());

                    try {
                        populateMenuButton(cheque);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    // Update the style of the Fav button based on cheque.getIsFav()
                    // Update the style of the Fav button based on cheque.getIsFav()
                    if (cheque.getIsFav() == 0) {
                        Fav.setImage(image1);

                    } else {
                        Fav.setImage(image2);
                    }
                    Fav.setFitWidth(30);  // Set the width
                    Fav.setFitHeight(30);

                    // Add your other buttons to the buttonsContainer

                    // Set the buttonsContainer as the graphic of the cell

                    // Set the buttonsContainer as the graphic of the cell
                    setGraphic(buttonsContainer);

                }
            }
        });

        Reclamation.setCellFactory(column -> {
            return new TableCell<Cheque, Void>() {
                private final Button viewReclamationsButton = new Button("View Reclamations");
                private final TableView<Reclamation> reclamationTableView = new TableView<>();

                TableColumn<Reclamation, Integer> idColumn = new TableColumn<>("ID");
                TableColumn<Reclamation, String> categoryColumn = new TableColumn<>("Category");
                TableColumn<Reclamation, String> statusColumn = new TableColumn<>("Status");

                TableColumn<Reclamation, Void> ActionColumn = new TableColumn<>("Actions");


                {reclamationTableView.setVisible(false);




// Set the minimum height for the TableView

                    reclamationTableView.setStyle("-fx-background-color: #ffffff;");
                    reclamationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    reclamationTableView.setRowFactory(tv -> {
                        TableRow<Reclamation> row = new TableRow<>();
                        row.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: #000000;"); // Set alternating row colors
                        return row;
                    });
                    reclamationTableView.setPadding(new Insets(10));
                    reclamationTableView.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");


                    reclamationTableView.getColumns().addAll(idColumn, categoryColumn, statusColumn,ActionColumn);
                    VBox vbox = new VBox(reclamationTableView, viewReclamationsButton);
                    vbox.setPrefHeight(0);

                    setGraphic(vbox);
                    viewReclamationsButton.setOnAction(event -> {



                        reclamationTableView.setRowFactory(tv -> new TableRow<Reclamation>() {
                            @Override
                            protected void updateItem(Reclamation item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty || item == null) {
                                    // Apply style for empty rows

                                } else {
                                    // Apply style for non-empty rows
                                    setStyle("   -fx-alignment: CENTER; /* Center align column content */" +
                                            "    -fx-font-size: 14px; /* Column font size */" +
                                            "    -fx-text-fill: #333333; /* Column text color */" +
                                            "    -fx-background-color: #ffffff; /* Column background color */" +
                                            "    -fx-border-color: #ccc; /* Column border color */" +
                                            "    -fx-border-width: 1px; /* Column border width */" +
                                            "    -fx-border-radius: 5px; /* Column border radius */;");// Clear any previous styles
                                }
                            }
                        });


                        reclamationTableView.setVisible(true);

                        Cheque selectedCheque = getTableView().getItems().get(getIndex());
                        int chequeID = selectedCheque.getId();
                        List<Reclamation> reclamations;
                        try {
                            reclamations = R.getAll(chequeID);
                            System.out.println("Number of reclamations returned: " + reclamations.size());
                            for (Reclamation recl : reclamations) {
                                System.out.println(recl); // Assuming Reclamation class has overridden toString() method
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        ObservableList<Reclamation> reclamationData = FXCollections.observableArrayList(reclamations);




                        reclamationTableView.setItems(reclamationData);

                        int rowCount = reclamationTableView.getItems().size();
                        if(rowCount==0){
                        reclamationTableView.setPrefHeight(0);
                        vbox.setPrefHeight(0);
                        setGraphic(vbox);}
                        else{ reclamationTableView.setPrefHeight(300);
                            vbox.setPrefHeight(300);
                            setGraphic(vbox);}

                        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
                        categoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategorie()));
                        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatutR()));
                        ActionColumn.setCellFactory(param -> new TableCell<Reclamation, Void>() {
                            final VBox buttonsContainer = new VBox();
                            private final Button updateButton = new Button("Update");
                            private final Button delete = new Button("Delete");





                            {
                                updateButton.setOnAction(event -> {
                                    R1 = getTableView().getItems().get(getIndex());

                                    updatefillRec(R1.getId());
                                    updateViewRec.setDisable(false);
                                    updateViewRec.getTabPane().getSelectionModel().select(updateViewRec);

                                    System.out.println("Update clicked for REc with ID: " + R1.getId());
                                });

                                delete.setOnAction(event -> {
                                    R1 = getTableView().getItems().get(getIndex());

                                    try {
                                        R.delete(R1.getId());
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }


                                    System.out.println("Deleted Rec  with ID: " + R1.getId());
                                });
                                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                                delete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");

                                updateButton.getStyleClass().add("custom-button");
                                delete.getStyleClass().add("custom-button");

                                buttonsContainer.getChildren().addAll(updateButton,delete);

                            }



                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(buttonsContainer); // Set the container as the graphic

                                }
                            }
                        });
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    }
                }
            };
        });



    }

    public static void generateChequeListPDF(List<Cheque> cheques) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Load your TrueType font file
            File fontFile = new File("C:\\RadiantKingdom-mL5eV.ttf");
            PDType0Font font = PDType0Font.load(document, fontFile);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set up table parameters
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
                float cellMargin = 5;
                float rowHeight = 30; // Increased row height
                int numOfColumns = 3;

                // Set up font and font size
                contentStream.setFont(font, 12); // Use your loaded TrueType font

                float cellWidth = tableWidth / numOfColumns;

                // Set purple color
                contentStream.setNonStrokingColor(new Color(128, 0, 128));

                // Draw purple rectangle for background
                // Draw purple rectangle for background
                contentStream.addRect(margin, yStart - 75, tableWidth, 75);  // Increased height to 100
                contentStream.fill();


                // Set font size for title
                contentStream.setFont(font, 24); // Set title font size

                // Calculate text width for center alignment
                float textWidth = font.getStringWidth("Bankify") / 1000f * 24;
                float xCenter = margin + (tableWidth - textWidth) / 2;

                // Set white color for "Bankify"
                contentStream.setNonStrokingColor(Color.BLACK);

                // Draw title "Bankify"
                contentStream.beginText();
                contentStream.newLineAtOffset(xCenter, yStart - 30); // Adjust text position
                contentStream.showText("Bankify");
                contentStream.endText();

                yStart -= 80; // Adjust yStart for title and background

                drawCell(contentStream, margin, yStart, cellWidth, rowHeight, "ID");
                drawCell(contentStream, margin + cellWidth, yStart, cellWidth, rowHeight, "Montant");
                drawCell(contentStream, margin + (2 * cellWidth), yStart, cellWidth, rowHeight, "Date Emmission");

                // Draw data rows
                yStart -= rowHeight;
                for (Cheque cheque : cheques) {
                    drawCell(contentStream, margin, yStart, cellWidth, rowHeight, String.valueOf(cheque.getId()));
                    drawCell(contentStream, margin + cellWidth, yStart, cellWidth, rowHeight, String.valueOf(cheque.getMontantC()));
                    drawCell(contentStream, margin + (2 * cellWidth), yStart, cellWidth, rowHeight, String.valueOf(cheque.getDateEmission()));
                    yStart -= rowHeight;
                }
            }

            String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
            String filePath = downloadsPath + File.separator + "cheque_list.pdf";

            // Save the PDF document
            try {
                // Create the "Downloads" directory if it doesn't exist
                if (!Files.exists(Paths.get(downloadsPath))) {
                    Files.createDirectories(Paths.get(downloadsPath));
                }

                // Save the PDF document to the "Downloads" directory
                document.save(filePath);
                document.close(); // Close the document after saving

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("PDF Saved Successfully");
                alert.setContentText("The PDF file has been saved in the Downloads directory.");
                alert.showAndWait();
            } catch (IOException e) {
                // Show error message if saving failed
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Save PDF");
                alert.setContentText("An error occurred while saving the PDF file.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private static void drawCell(PDPageContentStream contentStream, float x, float y, float cellWidth, float rowHeight, String text) throws IOException {
        // Draw cell border
        contentStream.addRect(x, y - rowHeight, cellWidth, rowHeight);
        contentStream.stroke();

        // Draw text
        contentStream.beginText();
        contentStream.newLineAtOffset(x + 5, y - 15); // Adjust text position
        contentStream.showText(text);
        contentStream.endText();
    }







}
