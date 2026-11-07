package com.togethersafe.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Set the window title
        primaryStage.setTitle("TogetherSafe");

        // 2. Load the logo (as the window icon)
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/logo.jpeg"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Logo could not be loaded: " + e.getMessage());
        }

        // 3. Create a simple placeholder screen with the app name
        Label welcomeLabel = new Label("TogetherSafe Desktop");
        welcomeLabel.setFont(Font.font("Segoe UI", 32));
        welcomeLabel.setTextFill(Color.web("#4F46E5")); // Primary color from DESIGN_SYSTEM.md

        Label tagline = new Label("Survivor Support & GBV Case Management");
        tagline.setFont(Font.font("Segoe UI", 16));
        tagline.setTextFill(Color.web("#475569"));

        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(20, welcomeLabel, tagline);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        StackPane root = new StackPane(vbox);
        root.setStyle("-fx-background-color: #F8FAFC;"); // Background color from DESIGN_SYSTEM.md

        // 4. Set the scene (1280x720 minimum as per DESKTOP.md)
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);

        // 5. Show the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}