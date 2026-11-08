package com.togethersafe.desktop.controller.survivor;

import com.togethersafe.desktop.util.TranslationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class SurvivorHomeController implements Initializable {

    @FXML private ComboBox<String> languageCombo;

    // Sidebar Labels
    @FXML private Label navHome, navAbout, navImpact, navSponsor, navResources;
    @FXML private Label navSos, navSafety, navDiscreet;
    @FXML private Label navChat, navLanguage;
    @FXML private Label navMyCases, navUpload, navShare, navLegal;
    @FXML private Label navServices, navHealing, navCommunity, navSettings;

    // Content Labels
    @FXML private Label lblSosTitle, lblSosDesc, lblNeedHelp, lblPressSos;
    @FXML private Label lblEmergencyContacts, lblQuickAccess;
    @FXML private Label lblPolice, lblPoliceDesc, lblMedical, lblMedicalDesc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        languageCombo.getItems().addAll("English", "Luganda", "Kiswahili");
        languageCombo.setValue("English");
        updateLanguage();
    }

    @FXML
    private void handleLanguageChange() {
        TranslationService.setLanguage(languageCombo.getValue());
        updateLanguage();
    }

    private void updateLanguage() {
        navHome.setText(TranslationService.get("Home"));
        navAbout.setText(TranslationService.get("About"));
        navImpact.setText(TranslationService.get("Impact Dashboard"));
        navSponsor.setText(TranslationService.get("Sponsor Recognition"));
        navResources.setText(TranslationService.get("Resources Library"));
        navSos.setText(TranslationService.get("SOS Center"));
        navSafety.setText(TranslationService.get("Safety Planning"));
        navDiscreet.setText(TranslationService.get("Discreet Mode"));
        navChat.setText(TranslationService.get("Chat Interface"));
        navLanguage.setText(TranslationService.get("Language Selection"));
        navMyCases.setText(TranslationService.get("My Cases"));
        navUpload.setText(TranslationService.get("Upload Evidence"));
        navShare.setText(TranslationService.get("Share Links"));
        navLegal.setText(TranslationService.get("Legal Guide"));
        navServices.setText(TranslationService.get("Services"));
        navHealing.setText(TranslationService.get("Healing"));
        navCommunity.setText(TranslationService.get("Community"));
        navSettings.setText(TranslationService.get("Settings"));

        lblSosTitle.setText(TranslationService.get("SOS Center"));
        lblSosDesc.setText(TranslationService.get("In an emergency, you are not alone. Get immediate help, contact trusted people, and access safety resources."));
        lblNeedHelp.setText(TranslationService.get("Need Immediate Help?"));
        lblPressSos.setText(TranslationService.get("Press the SOS button for emergency assistance."));
        lblEmergencyContacts.setText(TranslationService.get("Emergency Contacts"));
        lblQuickAccess.setText(TranslationService.get("Quick access to trusted helplines and support services."));
        lblPolice.setText(TranslationService.get("Police"));
        lblPoliceDesc.setText(TranslationService.get("Report crime, get immediate help"));
        lblMedical.setText(TranslationService.get("Medical"));
        lblMedicalDesc.setText(TranslationService.get("Emergency medical assistance"));
    }

    @FXML private void handleSos() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SOS - Emergency");
        alert.setHeaderText("Immediate Help Needed");
        alert.setContentText("This will open your device's dialer to call emergency services (999). Proceed?");
        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try { Desktop.getDesktop().browse(new URI("tel:999")); }
                catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    @FXML private void handleGuestProfile(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guest Profile");
        alert.setHeaderText("You are browsing as a Guest.");
        alert.setContentText("Since you are anonymous, there is no profile to manage.");
        alert.showAndWait();
    }

    private void navigateTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) navHome.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void goToAbout(MouseEvent e) { navigateTo("About.fxml"); }
    @FXML private void goToImpact(MouseEvent e) { navigateTo("ImpactDashboard.fxml"); }
    @FXML private void goToSponsor(MouseEvent e) { navigateTo("SponsorRecognition.fxml"); }
    @FXML private void goToResources(MouseEvent e) { navigateTo("ResourcesLibrary.fxml"); }
    @FXML private void goToSos(MouseEvent e) { /* Already here */ }
    @FXML private void goToSafety(MouseEvent e) { navigateTo("SafetyPlanning.fxml"); }
    @FXML private void goToDiscreet(MouseEvent e) { navigateTo("DiscreetMode.fxml"); }
    @FXML private void goToChat(MouseEvent e) { navigateTo("AiChat.fxml"); }
    @FXML private void goToLanguage(MouseEvent e) { navigateTo("LanguageSelection.fxml"); }
    @FXML private void goToMyCases(MouseEvent e) { navigateTo("MyCases.fxml"); }
    @FXML private void goToUpload(MouseEvent e) { navigateTo("UploadEvidence.fxml"); }
    @FXML private void goToShare(MouseEvent e) { navigateTo("ShareLinks.fxml"); }
    @FXML private void goToLegal(MouseEvent e) { navigateTo("LegalGuide.fxml"); }
    @FXML private void goToServices(MouseEvent e) { navigateTo("Services.fxml"); }
    @FXML private void goToHealing(MouseEvent e) { navigateTo("Healing.fxml"); }
    @FXML private void goToCommunity(MouseEvent e) { navigateTo("Community.fxml"); }
    @FXML private void goToSettings(MouseEvent e) { navigateTo("SurvivorSettings.fxml"); }
}