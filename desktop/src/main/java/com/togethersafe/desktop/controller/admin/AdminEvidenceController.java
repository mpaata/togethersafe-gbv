package com.togethersafe.desktop.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.togethersafe.desktop.api.ApiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEvidenceController implements Initializable {

    @FXML private TableView<AdminEvidenceRow> evidenceTable;
    @FXML private TableColumn<AdminEvidenceRow, String> colFile;
    @FXML private TableColumn<AdminEvidenceRow, String> colCase;
    @FXML private TableColumn<AdminEvidenceRow, String> colType;
    @FXML private TableColumn<AdminEvidenceRow, String> colUploadedBy;
    @FXML private TableColumn<AdminEvidenceRow, String> colChecksum;

    private final ObservableList<AdminEvidenceRow> evidence = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colFile.setCellValueFactory(new PropertyValueFactory<>("file"));
        colCase.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUploadedBy.setCellValueFactory(new PropertyValueFactory<>("uploadedBy"));
        colChecksum.setCellValueFactory(new PropertyValueFactory<>("checksum"));
        evidenceTable.setItems(evidence);
        // Placeholder rows (no backend endpoint yet)
        evidence.add(new AdminEvidenceRow("medical-report.pdf", "CASE-0001", "PDF", "survivor1", "a3f5...e9b2"));
        evidence.add(new AdminEvidenceRow("photo-bruise.jpg", "CASE-0002", "Image", "survivor2", "b4d7...f2c1"));
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) evidenceTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class AdminEvidenceRow {
        private final String file, caseId, type, uploadedBy, checksum;
        public AdminEvidenceRow(String file, String caseId, String type, String uploadedBy, String checksum) {
            this.file = file; this.caseId = caseId; this.type = type;
            this.uploadedBy = uploadedBy; this.checksum = checksum;
        }
        public String getFile() { return file; }
        public String getCaseId() { return caseId; }
        public String getType() { return type; }
        public String getUploadedBy() { return uploadedBy; }
        public String getChecksum() { return checksum; }
    }
}