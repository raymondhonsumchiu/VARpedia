package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.java.VARpedia;

import java.io.IOException;

public class WelcomeController {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void btnBeginClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/varpedia.fxml"));

        root.setOnMousePressed(event1 -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });

        root.setOnMouseDragged(event12 -> {
            VARpedia.primaryStage.setX(event12.getScreenX() - xOffset);
            VARpedia.primaryStage.setY(event12.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root, 1440, 810);
        if (VARpedia.isDark) {
            scene.getStylesheets().add(getClass().getResource("../../resources/css/dark.css").toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource("../../resources/css/light.css").toExternalForm());
        }
        VARpedia.primaryStage.setScene(scene);
        VARpedia.primaryStage.show();
    }

}
