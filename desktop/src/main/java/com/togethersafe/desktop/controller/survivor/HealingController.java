package com.togethersafe.desktop.controller.survivor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HealingController {

    @FXML private Button backButton;

    @FXML private void handleGrounding() { showInfo("Grounding Exercises", "5-minute breathing exercise starting..."); }
    @FXML private void handleJournaling() { showInfo("Journaling", "Opening guided journal prompts..."); }
    @FXML private void handleCourses() { showInfo("Course Library", "Browsing trauma recovery courses..."); }
    @FXML private void handleSounds() { showInfo("Calming Sounds", "Playing nature sounds..."); }
    @FXML private void handleChat() { showInfo("Talk to Someone", "Connecting to a trained counsellor..."); }
    @FXML private void handleSleep() { showInfo("Sleep Support", "Loading sleep techniques..."); }

    private void showInfo(String title, String message) {
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