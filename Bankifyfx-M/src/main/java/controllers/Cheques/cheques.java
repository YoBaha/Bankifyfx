package controllers.Cheques;


import models.Cheques.Cheque;
import models.Cheques.Reclamation;
import models.User;
import Cheques.Cheques;
import Cheques.Reclamations;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.google.gson.*;

public class cheques  {

    public static List<Reclamation> list;

    @FXML
    private ComboBox<String> CompteID;
    @FXML
    private ComboBox<String> CompteID1;
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
    private ComboBox<String> StatutR;

    @FXML
    private ComboBox<String> Categorie;

    @FXML
    private Tab updateViewRec;

    @FXML
    private ComboBox<String> Categorie1;

    @FXML
    private ComboBox<String> StatutR1;


    @FXML
    private Label montantValidationLabel;

    @FXML
    private Label comboBox1ValidationLabel;

    @FXML
    private Label comboBox2ValidationLabel;

    @FXML
    private Label montantValidationLabel1;
    @FXML
    private Label comboBox2ValidationLabel1;
    @FXML
    private Label comboBox1ValidationLabel1;

    @FXML
    private Label comboBox2ValidationLabel3;

    @FXML
    private Label comboBox1ValidationLabel3;

    @FXML
    private Button Refresh;

    String filePath = "C:\\reclamationList.json";




    Cheques c = new Cheques();

    Reclamations R=new Reclamations();

    Reclamation R1=new Reclamation();


    public cheques() throws IOException {
        this.list = new ArrayList<>();
        loadListFromFile();
    }

    // Save list to file


    // Load list from file
    public void loadListFromFile() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Reclamation.class, new typeadapter())
                .create();

        String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
        Type listType = new TypeToken<ArrayList<Reclamation>>(){}.getType();
        list = gson.fromJson(jsonString, listType);
    }
    public void saveListToFile() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Reclamation.class, new typeadapter())
                .create();

        String json = gson.toJson(list);
        Files.write(Paths.get(filePath), json.getBytes());
    }


    public List<Reclamation> getList() {
        return list;
    }

    public void setList(List<Reclamation> list) {
        this.list = list;
    }




    @FXML
    public void   updatefill(int idCheque) {
populateComboBox();
        try {
            Cheque ch = c.update1(idCheque);
            ID1.setText(String.valueOf(ch.getId()));

            MontantC1.setText(String.valueOf(ch.getMontantC()));
            ObservableList<String> items = CompteID1.getItems();
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).equals("..")) {
                    int currentItem = Integer.parseInt(items.get(i));
                    if (currentItem == ch.getCompteID()) {
                        CompteID1.getSelectionModel().select(i);
                        break;
                    }
                }
            }
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
            String StatutR = ch.getStatutR();
            ObservableList<String> items1 = StatutR1.getItems();

            for (int i = 0; i < items1.size(); i++) {
                String currentItem =items1.get(i);
                if (Objects.equals(currentItem, StatutR)) {
                    StatutR1.getSelectionModel().select(i);
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
            int compteID = Integer.parseInt(CompteID1.getValue());

            User selectedUserData = (User) DestinationC1.getValue();
            int destinationC = selectedUserData.getId();

            Cheque updatedCheque = new Cheque();
            updatedCheque.setId(Integer.parseInt(ID1.getText()));
            updatedCheque.setMontantC(montantC);
            updatedCheque.setCompteID(compteID);
            updatedCheque.setDestinationCID(destinationC);

            c.update(updatedCheque);
            montantValidationLabel1.setText("");
            comboBox2ValidationLabel1.setText("");
            comboBox1ValidationLabel1.setText("");

            boolean isComboBox22Valid = !DestinationC1.getValue().getNom().equals("..");

            if (!isComboBox22Valid) {
                comboBox1ValidationLabel.setText("");
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

            boolean isComboBox11Valid = !CompteID1.getValue().equals("..");
            try {
                if (!isMonatantValid) {
                    montantValidationLabel1.setText("Montant invalide");


                    boolean isComboBox2Valid = !DestinationC1.getValue().getNom().equals("..");

                    if (!isComboBox11Valid && !isComboBox2Valid) {
                        comboBox2ValidationLabel1.setText("");
                        comboBox1ValidationLabel1.setText("");


                        comboBox1ValidationLabel1.setText("Please select a valid value for compte.");
                        comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");

                    } else if (!isComboBox2Valid) {
                        comboBox2ValidationLabel1.setText("");
                        comboBox1ValidationLabel1.setText("");

                        comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");
                    } else if (!isComboBox11Valid) {
                        comboBox2ValidationLabel1.setText("");
                        comboBox1ValidationLabel1.setText("");
                        comboBox1ValidationLabel1.setText("Please select a valid value for compte.");


                    }


                } else {
                    montantValidationLabel1.setText("");
                    boolean isComboBox22Valid = !DestinationC1.getValue().getNom().equals("..");
                    System.out.println(isComboBox22Valid);

                    if (!isComboBox11Valid && !isComboBox22Valid) {

                        comboBox1ValidationLabel1.setText("Please select a valid value for compte.");
                        comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");

                    } else if (!isComboBox11Valid) {
                        comboBox2ValidationLabel1.setText("");
                        comboBox1ValidationLabel1.setText("Please select a valid value for compte.");

                    }


                }
            } catch (NullPointerException e) {
                comboBox1ValidationLabel1.setText("");
                comboBox2ValidationLabel1.setText("Please select a valid value for Destination.");
                if (!isComboBox11Valid) {
                    comboBox1ValidationLabel1.setText("Please select a valid value for compte.");

                }
            }


        }


    }
    public void saveListToFile(List<Reclamation> list, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load list from file with full path
    public List<Reclamation> loadListFromFile(String filePath) {
        List<Reclamation> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            list = (List<Reclamation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }


    @FXML
    public void updateRec() throws SQLException, IOException {






        // try {

        String Categorie = Categorie1.getValue();
        String StatutR = StatutR1.getValue();




        R1.setCategorie(Categorie);
        R1.setStatutR(StatutR);

        if ("Traitée".equals(StatutR)) {
            boolean idExists = list.stream().anyMatch(reclamation -> reclamation.getId() == R1.getId());

            if (!idExists) {
                list.add(R1);
            }





        }
        if ("En Cours".equals(StatutR)) {

            List<Reclamation> reclamationsToRemove = new ArrayList<>();
            for (Reclamation recl : list) {
                if (R1.getId()==(recl.getId())) {
                    reclamationsToRemove.add(recl);
                }
            }
            list.removeAll(reclamationsToRemove);

        }

        List<Reclamation> reclamationsToRemove = new ArrayList<>();


        list.removeAll(reclamationsToRemove);

        for (Reclamation reclamation : list) {
            System.out.println(reclamation.toString());  // Assuming Reclamation has a proper toString method
        }

        saveListToFile();





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
                 ResultSet resultSet1 = statement1.executeQuery("SELECT id,nom, prenom ,email,datenaissance,genre FROM user")) {
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
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT id FROM compte_client")) {
                List<String> items5 = new ArrayList<>();

                List<String> items1 = new ArrayList<>();
                items1.add("..");

                while (resultSet.next()) {
                    String itemName = resultSet.getString("id");
                    items1.add(itemName);
                    items5.add(itemName);


                }

                // Add items from the list to comboBox1
                ObservableList<String> itemList = FXCollections.observableArrayList(items1);
                ObservableList<String> itemList1 = FXCollections.observableArrayList(items5);

                CompteID.setItems(itemList);
                CompteID1.setItems(itemList);


                /*for (String item : items5) {
                    DestinationC.getItems().add(item);
                }*/
            }


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
        if ( StatutR.getItems().isEmpty()) {
            ObservableList<String> items1 = StatutR.getItems();
            items1.add("En Cours");
            items1.add("Traitée");
        }
        if ( Categorie1.getItems().isEmpty()) {
            ObservableList<String> items111 = Categorie1.getItems();
            items111.add("Payment Discrepancy");
            items111.add("Non-Receipt");
            items111.add("Delayed Processing");
            items111.add("Forgery");
        }
        if ( StatutR1.getItems().isEmpty()) {
            ObservableList<String> items11 = StatutR1.getItems();
            items11.add("En Cours");
            items11.add("Traitée");
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

            int compteID;

            compteID = Integer.parseInt(CompteID.getValue());
            User selectedUser = DestinationC.getValue();

            montantValidationLabel.setText("");
            comboBox2ValidationLabel.setText("");
            comboBox1ValidationLabel.setText("");
            boolean isComboBox22Valid = !DestinationC.getValue().getNom().equals("..");

            if (!isComboBox22Valid) {
                comboBox1ValidationLabel.setText("");
                comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
            }


            a.setMontantC(MonatantC);
            a.setDestinationCID(selectedUser.getId());
            a.setCompteID(compteID);


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

            boolean isComboBox11Valid = !CompteID.getValue().equals("..");
            try {
                if (!isMonatantValid) {
                    montantValidationLabel.setText("Montant invalide");


                boolean isComboBox2Valid = !DestinationC.getValue().getNom().equals("..");

                if (!isComboBox11Valid && !isComboBox2Valid) {
                    comboBox2ValidationLabel.setText("");
                    comboBox1ValidationLabel.setText("");


                    comboBox1ValidationLabel.setText("Please select a valid value for compte.");
                    comboBox2ValidationLabel.setText("Please select a valid value for Destination.");

                } else if (!isComboBox2Valid) {
                    comboBox2ValidationLabel.setText("");
                    comboBox1ValidationLabel.setText("");

                    comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
                } else if (!isComboBox11Valid) {
                    comboBox2ValidationLabel.setText("");
                    comboBox1ValidationLabel.setText("");
                    comboBox1ValidationLabel.setText("Please select a valid value for compte.");


                }


            }
                else
                {            montantValidationLabel.setText("");
                    boolean isComboBox22Valid = !DestinationC.getValue().getNom().equals("..");
                    System.out.println(isComboBox22Valid);

                    if (!isComboBox11Valid && !isComboBox22Valid) {

                        comboBox1ValidationLabel.setText("Please select a valid value for compte.");
                        comboBox2ValidationLabel.setText("Please select a valid value for Destination.");

                    } else if (!isComboBox11Valid) {
                        comboBox2ValidationLabel.setText("");
                        comboBox1ValidationLabel.setText("Please select a valid value for compte.");

                    }


                }
        }catch (NullPointerException e) {
                comboBox1ValidationLabel.setText("");
                comboBox2ValidationLabel.setText("Please select a valid value for Destination.");
                if (!isComboBox11Valid) {
                    comboBox1ValidationLabel.setText("Please select a valid value for compte.");

                }
            }

        }
    }

    @FXML
void addfill()
{  CompteID.getSelectionModel().select("..");
    DestinationC.getSelectionModel().select(new User(0,"..","..","..",null,".."));

}
    @FXML
    void addRec() throws SQLException {
        String categorie = null;
        String statutR = null;
        try {



            categorie = Categorie.getValue();
            statutR = StatutR.getValue();


            R1.setCategorie(categorie);
            R1.setStatutR(statutR);


            R.add(R1);
            ajouterview.setDisable(true);
            cheque.getTabPane().getSelectionModel().select(cheque);
            comboBox1ValidationLabel3.setText("");
            comboBox2ValidationLabel3.setText("");


        } catch (NullPointerException | SQLIntegrityConstraintViolationException e  ) {
            boolean isComboBox11Valid = categorie != null;
            boolean isComboBox22Valid = statutR != null;
            if(!isComboBox22Valid && ! isComboBox11Valid)
            {
                comboBox1ValidationLabel3.setText("Please select a valid value for Categorie.");
                comboBox2ValidationLabel3.setText("Please select a valid value for Statut.");

            }
            else if (!isComboBox22Valid )
            {comboBox1ValidationLabel3.setText("");
                comboBox2ValidationLabel3.setText("Please select a valid value for Statut.");}
            else if(! isComboBox11Valid)
            {
                comboBox2ValidationLabel3.setText("");
                comboBox1ValidationLabel3.setText("Please select a valid value for Categorie.");
                }


        }
    }
    @FXML
    public void action()throws SQLException
    {  Refresh.setOnAction(event -> {
                try {
                    afficher();
                } catch (SQLException e) {
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

            }


    );}


    @FXML
    public void afficher() throws SQLException {
        // Assuming c.getAll() returns a list of Cheque objects
        ObservableList<Cheque> chequeList = FXCollections.observableArrayList(c.getAll());
        chequeTableView.setItems(chequeList);

        chequeTableView.setStyle("-fx-background-color: #ffffff;");
        chequeTableView.setRowFactory(tv -> {
            TableRow<Cheque> row = new TableRow<>();
            row.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: #000000;");
            return row;
        });
        chequeTableView.setPadding(new Insets(10)); // Apply padding to the table cells for better spacing
        chequeTableView.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");





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


            {
                updateButton.setOnAction(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());
                    updatefill(cheque.getId());
                    updateview.setDisable(false);
                    updateview.getTabPane().getSelectionModel().select(updateview);

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
                    StatutR.getItems().clear();


                    System.out.println("Update clicked for Cheque with ID: " + cheque.getId());
                });
                delete.setOnAction(event -> {
                    Cheque cheque = getTableView().getItems().get(getIndex());
                    try {
                        c.delete(cheque.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("deleted cheque with id : " + cheque.getId());
                });
                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                ADD.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                delete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");
                updateButton.getStyleClass().add("custom-button");
                ADD.getStyleClass().add("custom-button");
                delete.getStyleClass().add("custom-button");

                buttonsContainer.getChildren().addAll(updateButton, ADD,delete);

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
                        Cheque cheque = getTableView().getItems().get(getIndex());

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
                                  R1.setChequeId(cheque.getId());  // Assuming Reclamation has a proper toString method

                                    updatefillRec(R1.getId());
                                    updateViewRec.setDisable(false);
                                    updateViewRec.getTabPane().getSelectionModel().select(updateViewRec);

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





}
