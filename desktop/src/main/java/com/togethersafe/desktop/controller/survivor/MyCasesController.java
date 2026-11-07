package com.togethersafe.desktop.controller.survivor;

import com.togethersafe.desktop.api.ApiClient;
import com.togethersafe.desktop.model.CaseItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
            System.err.println("Could not fetch cases: " + e.getMessage());
            e.printStackTrace();
        }
    }
}