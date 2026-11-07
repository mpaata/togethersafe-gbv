package com.togethersafe.desktop.controller.survivor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class SurvivorHomeController implements Initializable {

    @FXML
    private Button sosButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup complete
    }

    @FXML
    private void handleSos() {
        // Show a confirmation alert (SOS is critical, we want to make sure)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SOS - Emergency");
        alert.setHeaderText("Immediate Help Needed");
        alert.setContentText("This will open your device's dialer to call emergency services (999). Proceed?");

        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    Desktop.getDesktop().browse(new URI("tel:999"));
                } catch (Exception e) {
                    System.err.println("Could not open dialer: " + e.getMessage());
                }
            }
        });
    }
}