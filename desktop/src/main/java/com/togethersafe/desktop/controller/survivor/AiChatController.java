package com.togethersafe.desktop.controller.survivor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.awt.Desktop;

public class AiChatController implements Initializable {

    @FXML private ScrollPane chatScroll;
    @FXML private VBox chatContainer;
    @FXML private TextField messageField;
    @FXML private ComboBox<String> languageCombo;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String AI_URL = "http://localhost:8000/ai/chat";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate language dropdown in code (not FXML)
        languageCombo.getItems().addAll("en", "lg", "sw");
        languageCombo.setValue("en");

        addMessage("Hello, I'm Amani. How can I help you today? Your safety is my priority.", false, false);
    }

    @FXML
    private void handleSend() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        addMessage(text, true, false);
        messageField.clear();

        String language = languageCombo.getValue() != null ? languageCombo.getValue() : "en";

        new Thread(() -> {
            try {
                String body = String.format("{\"sessionId\": \"java-app\", \"message\": \"%s\", \"language\": \"%s\"}", text.replace("\"", "\\\""), language);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(AI_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.has("data") ? root.get("data") : root;

                String reply = data.has("reply") ? data.get("reply").asText() : "I'm here to help. Could you please rephrase?";
                boolean crisis = data.has("crisisDetected") && data.get("crisisDetected").asBoolean();

                Platform.runLater(() -> addMessage(reply, false, crisis));
            } catch (Exception e) {
                Platform.runLater(() -> addMessage("Connection error. Is the AI service running on port 8000?", false, true));
            }
        }).start();
    }

    private void addMessage(String text, boolean isUser, boolean isCrisis) {
        HBox container = new HBox();
        container.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(600);
        label.setPadding(new javafx.geometry.Insets(12));

        if (isUser) {
            label.setStyle("-fx-background-color: #4F46E5; -fx-text-fill: white; -fx-background-radius: 12 12 0 12;");
        } else if (isCrisis) {
            label.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-border-color: #DC2626; -fx-border-radius: 12 12 12 0; -fx-background-radius: 12;");
        } else {
            label.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #0F172A; -fx-border-color: #E2E8F0; -fx-background-radius: 12 12 12 0; -fx-border-radius: 12;");
        }

        container.getChildren().add(label);
        chatContainer.getChildren().add(container);
        chatScroll.setVvalue(1.0);
    }

    @FXML
    private void handleSos() {
        try {
            Desktop.getDesktop().browse(new URI("tel:999"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SurvivorHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) messageField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}