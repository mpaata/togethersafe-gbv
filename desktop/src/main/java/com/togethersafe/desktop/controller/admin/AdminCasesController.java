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

public class AdminCasesController implements Initializable {

    @FXML private TableView<AdminCaseRow> casesTable;
    @FXML private TableColumn<AdminCaseRow, String> colRef;
    @FXML private TableColumn<AdminCaseRow, String> colVictim;
    @FXML private TableColumn<AdminCaseRow, String> colType;
    @FXML private TableColumn<AdminCaseRow, String> colStatus;
    @FXML private TableColumn<AdminCaseRow, String> colPriority;
    @FXML private TableColumn<AdminCaseRow, String> colAssigned;
    @FXML private TextField searchField;

    private final ObservableList<AdminCaseRow> cases = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colRef.setCellValueFactory(new PropertyValueFactory<>("ref"));
        colVictim.setCellValueFactory(new PropertyValueFactory<>("victim"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colAssigned.setCellValueFactory(new PropertyValueFactory<>("assigned"));
        casesTable.setItems(cases);
        loadCases();

        // Search filter
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldV, newV) -> filterCases(newV));
        }
    }

    private void loadCases() {
        new Thread(() -> {
            try {
                JsonNode response = ApiClient.get("/admin/cases");
                if (response.has("data") && response.get("data").isArray()) {
                    javafx.application.Platform.runLater(() -> {
                        for (JsonNode node : response.get("data")) {
                            cases.add(new AdminCaseRow(
                                node.has("caseReference") ? node.get("caseReference").asText() : "CASE-???",
                                "Victim #" + (node.has("reporterId") ? node.get("reporterId").asText() : "?"),
                                node.has("caseType") ? node.get("caseType").asText() : "Unknown",
                                node.has("status") ? node.get("status").asText() : "OPEN",
                                node.has("priority") ? node.get("priority").asText() : "MEDIUM",
                                "Unassigned"
                            ));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void filterCases(String query) {
        if (query == null || query.isBlank()) return;
        ObservableList<AdminCaseRow> filtered = FXCollections.observableArrayList();
        for (AdminCaseRow row : cases) {
            if (row.getRef().toLowerCase().contains(query.toLowerCase())
                    || row.getType().toLowerCase().contains(query.toLowerCase())
                    || row.getStatus().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(row);
            }
        }
        casesTable.setItems(filtered);
    }

    @FXML
    private void handleNewCase() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Case");
        alert.setHeaderText("Create a New Case");
        alert.setContentText("Case creation form will be available soon.");
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) casesTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class AdminCaseRow {
        private final String ref, victim, type, status, priority, assigned;
        public AdminCaseRow(String ref, String victim, String type, String status, String priority, String assigned) {
            this.ref = ref; this.victim = victim; this.type = type;
            this.status = status; this.priority = priority; this.assigned = assigned;
        }
        public String getRef() { return ref; }
        public String getVictim() { return victim; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public String getPriority() { return priority; }
        public String getAssigned() { return assigned; }
    }
}