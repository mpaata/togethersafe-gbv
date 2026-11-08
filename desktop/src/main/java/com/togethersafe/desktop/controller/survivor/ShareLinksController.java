package com.togethersafe.desktop.controller.survivor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ShareLinksController implements Initializable {

    @FXML private ComboBox<String> evidenceCombo;
    @FXML private TextField recipientField;
    @FXML private ComboBox<String> expiryCombo;
    @FXML private ListView<String> activeLinksList;
    @FXML private Button backButton;

    private final ObservableList<String> links = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        evidenceCombo.getItems().addAll("medical-report.pdf", "photo-bruise.jpg", "witness-statement.mp3");
        expiryCombo.getItems().addAll("1 hour", "24 hours", "7 days", "30 days");
        expiryCombo.setValue("24 hours");
        activeLinksList.setItems(links);
        links.add("🔗 https://safe.link/abc123 → shared with doctor@hospital.ug — expires in 20 hours");
        links.add("🔗 https://safe.link/xyz789 → shared with caseworker@ngo.org — expires in 5 days");
    }

    @FXML
    private void handleGenerate() {
        String evidence = evidenceCombo.getValue();
        String recipient = recipientField.getText();
        String expiry = expiryCombo.getValue();

        if (evidence == null || recipient.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Please select evidence and enter recipient email.");
            alert.showAndWait();
            return;
        }

        String uniqueId = Integer.toHexString((int) (Math.random() * 0xFFFFFF));
        String link = "https://safe.link/" + uniqueId;
        links.add("🔗 " + link + " → shared with " + recipient + " — expires in " + expiry);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Link Generated");
        alert.setHeaderText("Share link created successfully.");
        alert.setContentText(link);
        alert.showAndWait();

        recipientField.clear();
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