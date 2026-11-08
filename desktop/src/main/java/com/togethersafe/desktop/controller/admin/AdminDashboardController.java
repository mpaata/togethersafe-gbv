package com.togethersafe.desktop.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.togethersafe.desktop.api.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML private Label activeCasesLabel;
    @FXML private Label reviewCasesLabel;
    @FXML private Label referralsLabel;
    @FXML private Label alertsLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDashboardData();
    }

    private void loadDashboardData() {
        new Thread(() -> {
            try {
                JsonNode response = ApiClient.get("/admin/dashboard-stats");
                if (response.has("data")) {
                    JsonNode data = response.get("data");
                    Platform.runLater(() -> {
                        activeCasesLabel.setText(String.valueOf(data.get("activeCases").asInt()));
                        reviewCasesLabel.setText(String.valueOf(data.get("pendingReview").asInt()));
                        referralsLabel.setText(String.valueOf(data.get("referrals").asInt()));
                        alertsLabel.setText(String.valueOf(data.get("alerts").asInt()));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void navigateTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) activeCasesLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Summary card navigation
    @FXML private void goToCases(MouseEvent event) { navigateTo("AdminCases.fxml"); }
    @FXML private void goToReview(MouseEvent event) { navigateTo("AdminCases.fxml"); }
    @FXML private void goToReferrals(MouseEvent event) { navigateTo("AdminReferrals.fxml"); }
    @FXML private void goToAlerts(MouseEvent event) { navigateTo("AdminEvidence.fxml"); }

    // Sidebar navigation
    @FXML private void goToVictims(MouseEvent event) { navigateTo("AdminVictims.fxml"); }
    @FXML private void goToEvidence(MouseEvent event) { navigateTo("AdminEvidence.fxml"); }
    @FXML private void goToDirectory(MouseEvent event) { navigateTo("AdminDirectory.fxml"); }
    @FXML private void goToMap(MouseEvent event) { navigateTo("AdminMap.fxml"); }
    @FXML private void goToAnalytics(MouseEvent event) { navigateTo("AdminAnalytics.fxml"); }
    @FXML private void goToReports(MouseEvent event) { navigateTo("AdminReports.fxml"); }
    @FXML private void goToSettings(MouseEvent event) { navigateTo("AdminSettings.fxml"); }

    // Reports link
    @FXML private void goToAllReports(MouseEvent event) { navigateTo("AdminReports.fxml"); }

    // Services link
    @FXML private void goToAllServices(MouseEvent event) { navigateTo("AdminDirectory.fxml"); }
}