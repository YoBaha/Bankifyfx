package controllers.User;

import controllers.creditController.GetCreditFront;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.ServiceCredit;
import services.User.ServiceUser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class ProfileController {


    public ImageView image_view;
    public Button signoutBtn;
    @FXML
    private Button btnModifier;

    @FXML
    private ChoiceBox<String> choiceGenre;

    private ModifierController.OnUserModifiedListener onUserModifiedListener;

    @FXML
    private DatePicker txtDateNaissance;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField picture_input;

    private ServiceUser serviceUser;
    // Declare serviceUser

    public ProfileController() {
        // Initialize serviceUser
        serviceUser = new ServiceUser();
    }

    public void setOnUserModifiedListener(ModifierController.OnUserModifiedListener listener) {
        this.onUserModifiedListener = listener;
    }


    private User user; // User to be modified

    private void loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                if (image.isError()) {
                    System.out.println("Error loading image: " + imagePath);
                } else {
                    image_view.setImage(image);
                    System.out.println("Image set");
                }
            } else {
                System.out.println("Image file does not exist: " + imagePath);
            }
        } else {
            System.out.println("No image path provided");
        }
    }




    public void setUser(User user) {
        this.user = user;
        txtNom.setText(user.getNom());
        txtPrenom.setText(user.getPrenom());
        txtEmail.setText(user.getEmail());
        // Set genre
        choiceGenre.setValue(user.getGenre());
        // Set dateNaissance
        if (user.getDateNaissance() != null) {
            txtDateNaissance.setValue(LocalDate.parse(user.getDateNaissance().toString()));
        } else {
            // Handle the case when dateNaissance is null
            txtDateNaissance.setValue(null);
        }
        picture_input.setText(user.getPicture());
        System.out.println("File path: " + picture_input.getText());
        URL imageUrl = getClass().getResource("/User/images/Profilepictures/" + picture_input.getText());
        System.out.println("Image URL: " + imageUrl);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            image_view.setImage(image);
            image_view.setFitWidth(200);
            image_view.setFitHeight(200);
            System.out.println("Image set");
        } else {
            System.out.println("Image not found");
        }
        picture_input.setVisible(false);
    }

    @FXML
    void upload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                picture_input.setText(selectedFile.getPath());
                System.out.println("Selected file path: " + selectedFile.getPath());

                // Call loadImage to load the selected image
                loadImage(selectedFile.getPath());
            } else {
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            System.out.println("No file selected");
        }
    }




    public interface OnUserModifiedListener {
        void onUserModified(User modifiedUser);
    }


    @FXML
    void modifier(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String genre = choiceGenre.getValue();
        LocalDate dateNaissance = txtDateNaissance.getValue();
        String picturePath = picture_input.getText();

        Path path = Paths.get(picturePath);
        String fileName = path.getFileName().toString();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || genre.isEmpty() || dateNaissance == null) {
            showAlert("Error", "Veuillez remplir tous les champs.");
        } else if (!isValidEmail(email)) {
            showAlert("Error", "Veuillez entrer une adresse email valide.");
        } else {
            // Convert LocalDate to java.util.Date
            Date utilDate = java.sql.Date.valueOf(dateNaissance);

            // Update user object with new values
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setGenre(genre);
            user.setDateNaissance(utilDate);
            user.setPicture(fileName);

            // Update the user using the service
            serviceUser.Modifier(user);
            Path destinationPath = Paths.get("src/main/resources/images/Profilepictures", fileName);
            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Call the listener to notify about user modification
            if (onUserModifiedListener != null) {
                onUserModifiedListener.onUserModified(user);
            }

            // Show success message
            showAlert("Success", "Utilisateur modifié avec succès.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        // Simple email validation using a regular expression
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
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

}