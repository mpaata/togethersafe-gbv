package com.togethersafe.desktop.controller.survivor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ServicesController {

    @FXML private ComboBox<String> serviceCategoryCombo;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        serviceCategoryCombo.getItems().addAll("Counselling", "Police", "Medical", "Shelter", "Legal Aid");
    }

    @FXML
    private void handleDirections() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Directions");
        alert.setHeaderText("Opening map...");
        alert.setContentText("In a real app, this would open Google Maps with turn-by-turn directions.");
        alert.showAndWait();
    }

    @FXML
    private void handleLocation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Location");
        alert.setHeaderText("Using your location");
        alert.setContentText("In a real app, this would use GPS to find nearby services.");
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