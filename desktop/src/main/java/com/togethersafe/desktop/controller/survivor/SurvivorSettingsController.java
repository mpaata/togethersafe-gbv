package com.togethersafe.desktop.controller.survivor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SurvivorSettingsController implements Initializable {

    // Account
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    // Privacy
    @FXML private CheckBox anonymousReportsCheck;
    @FXML private CheckBox hideLocationCheck;
    @FXML private CheckBox shareEvidenceCheck;

    // Notifications
    @FXML private CheckBox emailNotificationsCheck;
    @FXML private CheckBox smsNotificationsCheck;
    @FXML private CheckBox pushNotificationsCheck;

    // Security
    @FXML private PasswordField currentPinField;
    @FXML private PasswordField newPinField;
    @FXML private PasswordField confirmPinField;

    @FXML private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load current user info
        if (com.togethersafe.desktop.config.Session.getCurrentUser() != null) {
            usernameField.setText(com.togethersafe.desktop.config.Session.getCurrentUser().getUsername());
        }
    }

    @FXML
    private void handleSaveAccount() {
        showAlert("Account Saved", "Your account information has been updated.");
    }

    @FXML
    private void handleSavePrivacy() {
        showAlert("Privacy Saved", "Your privacy preferences have been saved.");
    }

    @FXML
    private void handleSaveNotifications() {
        showAlert("Notifications Saved", "Your notification preferences have been saved.");
    }

    @FXML
    private void handleUpdatePin() {
        String current = currentPinField.getText();
        String newPin = newPinField.getText();
        String confirm = confirmPinField.getText();

        if (current.isBlank() || newPin.isBlank() || confirm.isBlank()) {
            showAlert("Missing Fields", "Please fill in all PIN fields.");
            return;
        }

        if (!newPin.equals(confirm)) {
            showAlert("PIN Mismatch", "New PIN and confirmation do not match.");
            return;
        }

        if (newPin.length() < 4 || newPin.length() > 6) {
            showAlert("Invalid PIN", "PIN must be 4 to 6 digits.");
            return;
        }

        currentPinField.clear();
        newPinField.clear();
        confirmPinField.clear();
        showAlert("PIN Updated", "Your PIN has been updated successfully.");
    }

    @FXML
    private void handleDeleteAccount() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText("Are you absolutely sure?");
        confirm.setContentText("This will permanently delete your account and all associated data.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showAlert("Account Deleted", "Your account has been deleted. You will be logged out.");
                // In a real app, call the API to delete, then navigate to login
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SurvivorHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}