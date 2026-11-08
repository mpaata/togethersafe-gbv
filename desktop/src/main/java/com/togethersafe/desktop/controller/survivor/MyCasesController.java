package com.togethersafe.desktop.controller.survivor;

import com.togethersafe.desktop.api.ApiClient;
import com.togethersafe.desktop.model.CaseItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyCasesController implements Initializable {

    @FXML private TableView<CaseItem> casesTable;
    @FXML private TableColumn<CaseItem, String> colCaseRef;
    @FXML private TableColumn<CaseItem, String> colType;
    @FXML private TableColumn<CaseItem, String> colStatus;
    @FXML private TableColumn<CaseItem, String> colPriority;
    @FXML private TableColumn<CaseItem, String> colCreated;

    private final ObservableList<CaseItem> cases = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCaseRef.setCellValueFactory(new PropertyValueFactory<>("caseReference"));
        colType.setCellValueFactory(new PropertyValueFactory<>("caseType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        casesTable.setItems(cases);
        loadCases();
    }

    private void loadCases() {
        try {
            List<CaseItem> fetched = ApiClient.fetchCases();
            cases.setAll(fetched);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewCase() {
        // Show a simple dialog to create a case
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Case");
        dialog.setHeaderText("Report a New Case");

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        TextField typeField = new TextField();
        typeField.setPromptText("Case Type (e.g., PHYSICAL_ASSAULT)");
        
        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        VBox vbox = new VBox(10, new Label("Type:"), typeField, new Label("Description:"), descField, new Label("Location:"), locationField);
        dialog.getDialogPane().setContent(vbox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == createButtonType) {
                try {
                    String body = String.format(
                        "{\"caseType\": \"%s\", \"description\": \"%s\", \"locationSummary\": \"%s\"}",
                        typeField.getText(), descField.getText(), locationField.getText()
                    );
                    ApiClient.post("/cases", body);
                    loadCases(); // Refresh the table
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void goBack(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SurvivorHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) casesTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}