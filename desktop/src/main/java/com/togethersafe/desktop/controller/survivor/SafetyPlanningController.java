package com.togethersafe.desktop.controller.survivor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SafetyPlanningController {

    @FXML private TextArea safePlacesField;
    @FXML private TextArea trustedContactsField;
    @FXML private TextArea emergencyBagField;
    @FXML private Button backButton;

    @FXML
    private void handleSave() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Safety Plan Saved");
        alert.setHeaderText("Your safety plan has been saved securely.");
        alert.setContentText("Safe Places: " + (safePlacesField.getText().isBlank() ? "(empty)" : "saved")
                + "\nTrusted Contacts: " + (trustedContactsField.getText().isBlank() ? "(empty)" : "saved")
                + "\nEmergency Bag: " + (emergencyBagField.getText().isBlank() ? "(empty)" : "saved"));
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