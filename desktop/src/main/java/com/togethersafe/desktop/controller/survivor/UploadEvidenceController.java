package com.togethersafe.desktop.controller.survivor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UploadEvidenceController implements Initializable {

    @FXML private ComboBox<String> caseCombo;
    @FXML private ComboBox<String> fileTypeCombo;
    @FXML private Label fileNameLabel;
    @FXML private RadioButton radioPrivate;
    @FXML private RadioButton radioShared;
    @FXML private ListView<String> uploadsList;
    @FXML private Button backButton;

    private File selectedFile;
    private final ObservableList<String> uploads = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileTypeCombo.getItems().addAll("Image", "Document", "Audio", "Video");
        uploadsList.setItems(uploads);
        uploads.add("📄 medical-report.pdf — Uploaded 2 days ago");
        uploads.add("📷 photo-bruise.jpg — Uploaded 5 days ago");
        uploads.add("🎤 witness-statement.mp3 — Uploaded 1 week ago");
    }

    @FXML
    private void handleChooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Evidence File");
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            selectedFile = file;
            fileNameLabel.setText(file.getName() + " (" + (file.length() / 1024) + " KB)");
        }
    }

    @FXML
    private void handleUpload() {
        if (selectedFile == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText("Please choose a file first.");
            alert.showAndWait();
            return;
        }

        String privacy = radioPrivate.isSelected() ? "Private" : "Shared with caseworker";
        uploads.add("📄 " + selectedFile.getName() + " — " + privacy + " — Just now");
        fileNameLabel.setText("No file selected");
        selectedFile = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upload Successful");
        alert.setHeaderText("Evidence uploaded successfully.");
        alert.setContentText("Privacy: " + privacy);
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