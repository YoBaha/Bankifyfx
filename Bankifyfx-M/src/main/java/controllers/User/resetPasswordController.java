package controllers.User;


import javafx.scene.Scene;
import javafx.stage.Stage;
import services.User.ServiceEmail;
import services.User.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class resetPasswordController {
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordField1;
    @FXML
    private Button ConfirmPasswordBtn;

    @FXML
    private TextField emailField;
    private ServiceUser serviceForgetPassword = new ServiceUser();

    public void initialize(){
        passwordField.setVisible(false);
        passwordField1.setVisible(false);
        ConfirmPasswordBtn.setVisible(false);
    }

    public void submitForgetPassword(ActionEvent actionEvent) {
        String email = emailField.getText();

        if (serviceForgetPassword.emailExists(email)) {
            String verificationCode = generateVerificationCode();
            ServiceEmail.sendEmail(emailField.getText(),verificationCode,"Your verifcatrion code is "+verificationCode);
            System.out.println(verificationCode);
            boolean isCodeCorrect = showVerificationPrompt(verificationCode);

            if (isCodeCorrect) {
                passwordField.setVisible(true);
                passwordField1.setVisible(true);
                ConfirmPasswordBtn.setVisible(true);
            } else {
                showAlert("Incorrect Code", "The verification code entered is incorrect.");
            }
        } else {
            showAlert("Email Not Found", "The provided email does not exist.");
        }
    }

    public void goBackToLogin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000 + 100000));
    }

    private boolean showVerificationPrompt(String verificationCode) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verification Code");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the verification code sent to your email:");

        Optional<String> result = dialog.showAndWait();
        return result.isPresent() && result.get().equals(verificationCode);
    }
    private void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void ConfirmPassword(ActionEvent actionEvent) {
        String newPassword = passwordField.getText();
        String confirmPassword = passwordField1.getText();
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The entered passwords do not match.");
            return;
        }
        String email = emailField.getText();
        serviceForgetPassword.changePassword(email, newPassword);
        showAlert1("Password Changed", "Your password has been successfully changed.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
        try {

            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
