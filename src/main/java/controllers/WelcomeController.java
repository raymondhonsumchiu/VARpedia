package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import main.java.VARpedia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static main.java.VARpedia.*;

public class WelcomeController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private HBox hRoot;

    @FXML private Button btnMinimise;
    @FXML private Button btnHelp;
    @FXML private Button btnClose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Allow pressing space to begin
        hRoot.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                btnBeginClick(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnMinimise.addEventFilter(KeyEvent.ANY, Event::consume);
        btnHelp.addEventFilter(KeyEvent.ANY, Event::consume);
        btnClose.addEventFilter(KeyEvent.ANY, Event::consume);
    }

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

    @FXML
    void btnMinimiseClicked(ActionEvent event) {
        VARpedia.primaryStage.setIconified(true);
    }

    @FXML
    void btnHelpClicked(ActionEvent event) {

    }

    @FXML
    void btnCloseClicked(ActionEvent event) {
        // Clean up on exit
        bgExecutor.shutdownNow();
        deleteDirectory(TEMP);
        deleteDirectory(CHUNKS);
        VARpedia.primaryStage.close();
    }

}
