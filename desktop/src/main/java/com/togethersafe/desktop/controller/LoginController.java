package com.togethersafe.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.togethersafe.desktop.api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        try {
            String body = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
            JsonNode response = ApiClient.post("/auth/login", body);

            if (response.has("data") && !response.get("data").isNull()) {
                // Login successful — switch to Survivor Home
                loadSurvivorHome();
            } else {
                errorLabel.setText("Invalid credentials. Try again.");
            }
        } catch (Exception e) {
            errorLabel.setText("Cannot connect to server. Is the backend running?");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnonymousReport() {
        // Just load the Survivor Home screen (anonymous mode)
        loadSurvivorHome();
    }

    private void loadSurvivorHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SurvivorHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load home screen.");
        }
    }
}