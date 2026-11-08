package com.togethersafe.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.togethersafe.desktop.api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label errorLabel;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button signInButton;
    @FXML private Button registerButton;

    private boolean isRegisterMode = false;

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("SURVIVOR", "CASEWORKER", "ADMIN");
        roleCombo.setValue("SURVIVOR");
    }

    @FXML
    private void handleRegisterToggle() {
        isRegisterMode = !isRegisterMode;

        if (isRegisterMode) {
            titleLabel.setText("Create Account");
            subtitleLabel.setText("Register to access TogetherSafe");
            fullNameField.setVisible(true);
            fullNameField.setManaged(true);
            roleCombo.setVisible(true);
            roleCombo.setManaged(true);
            signInButton.setText("Register");
            registerButton.setText("Back to Sign In");
        } else {
            titleLabel.setText("TogetherSafe");
            subtitleLabel.setText("Sign in to continue");
            fullNameField.setVisible(false);
            fullNameField.setManaged(false);
            roleCombo.setVisible(false);
            roleCombo.setManaged(false);
            signInButton.setText("Sign In");
            registerButton.setText("Create Account");
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        try {
            if (isRegisterMode) {
                String fullName = fullNameField.getText().trim();
                String role = roleCombo.getValue();
                if (fullName.isEmpty()) {
                    errorLabel.setText("Please enter your full name.");
                    return;
                }
                JsonNode response = ApiClient.register(username, password, fullName, role);
                if (response.has("data") && !response.get("data").isNull()) {
                    errorLabel.setStyle("-fx-text-fill: #059669;");
                    errorLabel.setText("Account created! You can now sign in.");
                    handleRegisterToggle(); // Switch back to sign-in mode
                } else {
                    errorLabel.setStyle("-fx-text-fill: #DC2626;");
                    errorLabel.setText(response.get("error").asText());
                }
            } else {
                String body = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
                JsonNode response = ApiClient.post("/auth/login", body);

                if (response.has("data") && !response.get("data").isNull()) {
                    if ("admin".equalsIgnoreCase(username)) {
                        loadAdminDashboard();
                    } else {
                        loadSurvivorHome();
                    }
                } else {
                    errorLabel.setText("Invalid credentials. Try again.");
                }
            }
        } catch (Exception e) {
            errorLabel.setText("Cannot connect to server. Is the backend running?");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnonymousReport() {
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
            errorLabel.setText("Failed to load home screen: " + e.getMessage());
        }
    }

    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load admin dashboard: " + e.getMessage());
        }
    }
}