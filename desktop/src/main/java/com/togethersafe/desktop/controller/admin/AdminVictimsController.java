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

public class AdminVictimsController implements Initializable {

    @FXML private TableView<AdminVictimRow> victimsTable;
    @FXML private TableColumn<AdminVictimRow, String> colId;
    @FXML private TableColumn<AdminVictimRow, String> colName;
    @FXML private TableColumn<AdminVictimRow, String> colAge;
    @FXML private TableColumn<AdminVictimRow, String> colLocation;
    @FXML private TableColumn<AdminVictimRow, String> colCases;
    @FXML private TableColumn<AdminVictimRow, String> colLastContact;

    private final ObservableList<AdminVictimRow> victims = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCases.setCellValueFactory(new PropertyValueFactory<>("cases"));
        colLastContact.setCellValueFactory(new PropertyValueFactory<>("lastContact"));
        victimsTable.setItems(victims);
        loadVictims();
    }

    private void loadVictims() {
        new Thread(() -> {
            try {
                JsonNode response = ApiClient.get("/admin/victims");
                if (response.has("data") && response.get("data").isArray()) {
                    javafx.application.Platform.runLater(() -> {
                        for (JsonNode node : response.get("data")) {
                            victims.add(new AdminVictimRow(
                                node.has("id") ? "#" + node.get("id").asText() : "#?",
                                node.has("fullName") ? node.get("fullName").asText() : "Unknown",
                                node.has("age") ? node.get("age").asText() : "N/A",
                                "Kampala",
                                "1",
                                "2026-11-01"
                            ));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleAddVictim() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Victim");
        alert.setHeaderText("Add a New Victim Record");
        alert.setContentText("Victim registration form will be available soon.");
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) victimsTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class AdminVictimRow {
        private final String id, name, age, location, cases, lastContact;
        public AdminVictimRow(String id, String name, String age, String location, String cases, String lastContact) {
            this.id = id; this.name = name; this.age = age;
            this.location = location; this.cases = cases; this.lastContact = lastContact;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getAge() { return age; }
        public String getLocation() { return location; }
        public String getCases() { return cases; }
        public String getLastContact() { return lastContact; }
    }
}