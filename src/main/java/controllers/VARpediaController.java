package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.java.VARpedia;
import main.java.skins.progressindicator.RingProgressIndicator;
import main.java.tasks.WikitTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static main.java.VARpedia.*;

public class VARpediaController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TabPane tabMain;

    @FXML
    private Tab tabSearch;

    @FXML
    private Label lblNumberCreations;

    @FXML
    private ListView<String> listCreations;

    @FXML
    private Button btnSearch;

    @FXML
    private ListView<String> listChunksSearch;

    @FXML
    private Button btnSearchPreviewChunk;

    @FXML
    private Button btnCreateChunk;

    @FXML
    private Button btnCombine;

    @FXML
    private ListView<String> listAllChunks;

    @FXML
    private Button btnPreviewChunkCombine;

    @FXML
    private Button btnDeleteChunk;

    @FXML
    private ListView<String> listSelectedChunks;

    @FXML
    private Button btnClearChunks;

    @FXML
    private Button btnPreviewCreation;

    @FXML
    private Button btnCreateCreation;

    @FXML
    private ToggleButton btnDarkTheme;

    @FXML
    private ToggleButton btnLightTheme;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtCreationName;

    @FXML
    private TextField txtChunkName;

    @FXML
    private TextArea txaPreviewChunk1;

    @FXML
    private TextArea txaPreviewChunk2;

    @FXML
    private TextArea txaResults;

    @FXML
    private RingProgressIndicator ringSearch;

    @FXML
    private RingProgressIndicator ringCombine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Clean up
        deleteDirectory(CHUNKS);
        deleteDirectory(TEMP);

        // Initialise "Creations" tab
        if (CREATIONS.exists()) {
            // Find and list all creations using bash
            ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "ls *.mp4");
            b.directory(CREATIONS);
            Process p = null;
            try {
                p = b.start();
                InputStream out = p.getInputStream();
                BufferedReader stdout = new BufferedReader(new InputStreamReader(out));

                List<String> list = new ArrayList<>();
                String line;
                while ((line = stdout.readLine()) != null) {
                    list.add(line.substring(0, line.length() - 4));
                }

                lblNumberCreations.setText("" + list.size());
                Collections.sort(list);
                listCreations.setItems(FXCollections.observableArrayList(list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Initialise "Search" tab
        txaResults.setEditable(false);
        ringSearch.setVisible(false);

        // Initialise "Combine" tab
        ringCombine.setVisible(false);

        // Initialise "Quiz" tab

        // Initialise "Options" tab
        if (VARpedia.isDark) {
            btnLightTheme.setSelected(false);
            btnDarkTheme.setSelected(true);
        } else {
            btnLightTheme.setSelected(true);
            btnDarkTheme.setSelected(false);
        }
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/welcome.fxml"));

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
    void btnClearChunksClicked(ActionEvent event) {

    }

    @FXML
    void btnCloseClicked(ActionEvent event) {
        // Clean up on exit
        bg.shutdownNow();
        deleteDirectory(TEMP);
        deleteDirectory(CHUNKS);
        VARpedia.primaryStage.close();
    }

    @FXML
    void btnCombineClicked(ActionEvent event) {
        tabMain.getSelectionModel().select(2);
    }

    @FXML
    void btnCreateChunkClicked(ActionEvent event) {

    }

    @FXML
    void btnCreateCreationClicked(ActionEvent event) {

    }

    @FXML
    void btnLightThemeClicked(ActionEvent event) {
        if (!btnLightTheme.isSelected()) {
            btnLightTheme.setSelected(true);
            btnDarkTheme.setSelected(false);
        } else {
            VARpedia.isDark = false;
            btnDarkTheme.setSelected(false);
            VARpedia.primaryStage.getScene().getStylesheets().clear();
            VARpedia.primaryStage.getScene().setUserAgentStylesheet(null);
            VARpedia.primaryStage.getScene().getStylesheets().add(getClass().getResource("../../resources/css/light.css").toExternalForm());
        }
    }

    @FXML
    void btnDarkThemeClicked(ActionEvent event) {
        if (!btnDarkTheme.isSelected()) {
            btnDarkTheme.setSelected(true);
            btnLightTheme.setSelected(false);
        } else {
            VARpedia.isDark = true;
            btnLightTheme.setSelected(false);
            VARpedia.primaryStage.getScene().getStylesheets().clear();
            VARpedia.primaryStage.getScene().setUserAgentStylesheet(null);
            VARpedia.primaryStage.getScene().getStylesheets().add(getClass().getResource("../../resources/css/dark.css").toExternalForm());
        }
    }

    @FXML
    void btnDeleteChunkClicked(ActionEvent event) {

    }

    @FXML
    void btnDeleteCreationClicked(ActionEvent event) {

    }

    @FXML
    void btnHelpClicked(ActionEvent event) {

    }

    @FXML
    void btnMinimiseClicked(ActionEvent event) {
        VARpedia.primaryStage.setIconified(true);
    }

    @FXML
    void btnPlayCreationClicked(ActionEvent event) {

    }

    @FXML
    void btnPreviewChunkClicked(ActionEvent event) {

    }

    @FXML
    void btnPreviewCreationClicked(ActionEvent event) {

    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        String query = txtSearch.getText().trim().toLowerCase();
        if (!query.isEmpty()) {
            WikitTask bgWikit = new WikitTask(query);
            bg.submit(bgWikit);
            txaResults.clear();
            txaResults.setEditable(false);
            btnSearch.setDisable(true);
            ringSearch.setVisible(true);

            bgWikit.setOnSucceeded(e -> {
                btnSearch.setDisable(false);
                ringSearch.setVisible(false);
                txaResults.setStyle("-fx-text-fill: font-color;");

                List<String> list = bgWikit.getValue();
                if (list == null || list.get(0).contains("not found :^(")) {
                    txaResults.setText("No results found for \"" + query + "\".");
                    txaResults.setStyle("-fx-text-fill: close-color;");
                    // Reset search field
                    txtSearch.selectAll();
                    txtSearch.requestFocus();
                } else if (list.get(0).contains("Ambiguous results, ") || list.get(0).contains("may also refer to:") || (list.size() > 1 && list.get(1).contains("may also refer to:"))) {
                    txaResults.setText("Ambiguous results found for \"" + query + "\".");
                    txaResults.setStyle("-fx-text-fill: close-color;");
                    // Reset search field
                    txtSearch.selectAll();
                    txtSearch.requestFocus();
                } else {
                    txaResults.setEditable(true);
                    for (String s : list) {
                        txaResults.appendText(s);
                    }
                    txaResults.requestFocus();
                    txaResults.positionCaret(0);
                }
            });
        }
    }

    @FXML
    void listAllChunksClicked(MouseEvent event) {
        String chunk;
        if (event.getClickCount() == 2) {
            chunk = listCreations.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    void listChunksSearchClicked(MouseEvent event) {
        String chunk;
        if (event.getClickCount() == 2) {
            chunk = listCreations.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    void listCreationsClicked(MouseEvent event) {
        String creation;
        if (event.getClickCount() == 2) {
            creation = listCreations.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    void listSelectedChunksClicked(MouseEvent event) {
        String chunk;
        if (event.getClickCount() == 2) {
            chunk = listCreations.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    void tabMainMouseDragged(MouseEvent event) {
        VARpedia.primaryStage.setX(event.getScreenX() - xOffset);
        VARpedia.primaryStage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void tabMainMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void tabMainMouseReleased(MouseEvent event) {
        txtSearch.requestFocus();
    }

    @FXML
    void txtChunkEnter(ActionEvent event) {
        btnCreateChunkClicked(event);
    }

    @FXML
    void txtCreationEnter(ActionEvent event) {
        btnCreateCreationClicked(event);
    }

    @FXML
    void txtSearchEnter(ActionEvent event) {
        btnSearchClicked(event);
    }

}
