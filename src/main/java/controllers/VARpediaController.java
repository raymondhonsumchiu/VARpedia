package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.stage.StageStyle;
import main.java.VARpedia;
import main.java.skins.progressindicator.RingProgressIndicator;
import main.java.tasks.FlickrTask;
import main.java.tasks.WikitTask;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static main.java.VARpedia.*;

public class VARpediaController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private String css;
    private List<ImageView> gridImages;
    private List<ToggleButton> gridToggles;
    private ObservableList<String> chunksList = FXCollections.observableArrayList();
    private ObservableList<String> actualChunksList = FXCollections.observableArrayList();

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
    private ToggleButton toggleGrid1;

    @FXML
    private ImageView imgGrid1;

    @FXML
    private ToggleButton toggleGrid2;

    @FXML
    private ImageView imgGrid2;

    @FXML
    private ToggleButton toggleGrid3;

    @FXML
    private ImageView imgGrid3;

    @FXML
    private ToggleButton toggleGrid4;

    @FXML
    private ImageView imgGrid4;

    @FXML
    private ToggleButton toggleGrid5;

    @FXML
    private ImageView imgGrid5;

    @FXML
    private ToggleButton toggleGrid6;

    @FXML
    private ImageView imgGrid6;

    @FXML
    private ToggleButton toggleGrid7;

    @FXML
    private ImageView imgGrid7;

    @FXML
    private ToggleButton toggleGrid8;

    @FXML
    private ImageView imgGrid8;

    @FXML
    private ToggleButton toggleGrid9;

    @FXML
    private ImageView imgGrid9;

    @FXML
    private ToggleButton toggleGrid10;

    @FXML
    private ImageView imgGrid10;

    @FXML
    private ToggleButton toggleGrid11;

    @FXML
    private ImageView imgGrid11;

    @FXML
    private ToggleButton toggleGrid12;

    @FXML
    private ImageView imgGrid12;

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
    private MediaView medPlayCreation;

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

        // Initialise "Search" tab------
        txaResults.setEditable(false);
        ringSearch.setVisible(false);

        //set listview of chunks to observe the arraylist of chunks
        listChunksSearch.setItems(chunksList);

        // Initialise "Combine" tab------
        ringCombine.setVisible(false);
        gridImages = new ArrayList<>();
        gridToggles = new ArrayList<>();
        Collections.addAll(gridImages,imgGrid1,imgGrid2,imgGrid3,imgGrid4,imgGrid5,imgGrid6,imgGrid7,imgGrid8,imgGrid9,imgGrid10,imgGrid11,imgGrid12);
        Collections.addAll(gridToggles,toggleGrid1,toggleGrid2,toggleGrid3,toggleGrid4,toggleGrid5,toggleGrid5,toggleGrid6,toggleGrid7,toggleGrid8,toggleGrid8,toggleGrid9,toggleGrid10,toggleGrid11,toggleGrid12);

        //set listview of all available chunks to same arraylist of chunks as prior chunk listview
        listAllChunks.setItems(chunksList);
        listSelectedChunks.setItems(actualChunksList);

        // Initialise "Quiz" tab-------

        // Initialise "Options" tab
        if (VARpedia.isDark) {
            btnLightTheme.setSelected(false);
            btnDarkTheme.setSelected(true);
            css = getClass().getResource("../../resources/css/dark.css").toExternalForm();
        } else {
            btnLightTheme.setSelected(true);
            btnDarkTheme.setSelected(false);
            css = getClass().getResource("../../resources/css/light.css").toExternalForm();
        }
    }

    @FXML
    void btnAddChunkClicked(ActionEvent event) {
        String chunkToAdd = listAllChunks.getSelectionModel().getSelectedItem();
        if(chunkToAdd != null){
            listSelectedChunks.getItems().add(chunkToAdd);
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
        scene.getStylesheets().add(css);
        VARpedia.primaryStage.setScene(scene);
        VARpedia.primaryStage.show();
    }

    @FXML
    void btnClearChunksClicked(ActionEvent event) {
        //clear all selected items
        listSelectedChunks.getItems().clear();
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

        //PLace holder for chunk creation
        //this list will house the names of all the chunks that will be displayed by the chunk listviews
        //ideally these following lines will be kept, just swapping the name of the chunk
        chunksList.add("Hello!");
        Collections.sort(chunksList);
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
            css = getClass().getResource("../../resources/css/light.css").toExternalForm();
            VARpedia.primaryStage.getScene().getStylesheets().add(css);
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
            css = getClass().getResource("../../resources/css/dark.css").toExternalForm();
            VARpedia.primaryStage.getScene().getStylesheets().add(css);
        }
    }

    @FXML
    void btnDeleteChunkClicked(ActionEvent event) {
        if(listAllChunks.getSelectionModel().getSelectedItem() != null){
            int index = listAllChunks.getSelectionModel().getSelectedIndex();
            listAllChunks.getItems().remove(index);
        }
    }

    @FXML
    void btnDeleteCreationClicked(ActionEvent event) {
        if (listCreations.getSelectionModel().getSelectedItem() != null) {
            // Confirm if user wants to delete Creation
            String vid = listCreations.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete \"" + vid + "\"?", btnYes, btnNo);
            alert.setTitle("Delete Creation");
            alert.getDialogPane().getStylesheets().add(css);
            alert.setHeaderText("Delete Creation");
            alert.setGraphic(null);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setResizable(false);
            if (alert.showAndWait().get() == btnYes) {
                new File(CREATIONS.toString() + System.getProperty("file.separator") + vid + ".mp4").delete();
            }
        }
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
    void btnRemoveChunkClicked(ActionEvent event) {
        //if an item is selected, it will be removed
        if(listSelectedChunks.getSelectionModel().getSelectedItem() != null){
            int index = listSelectedChunks.getSelectionModel().getSelectedIndex();
            listSelectedChunks.getItems().remove(index);
        }
    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        // Clean up for new search
        deleteDirectory(TEMP);
        deleteDirectory(CHUNKS);

        String query = txtSearch.getText().trim().toLowerCase();
        if (!query.isEmpty()) {
            WikitTask bgWikit = new WikitTask(query);
            bg.submit(bgWikit);
            txaResults.clear();
            txaResults.setEditable(false);
            btnSearch.setDisable(true);
            ringSearch.setVisible(true);

            FlickrTask bgFlickr = new FlickrTask(query);
            bg.submit(bgFlickr);
            btnCreateCreation.setDisable(true);
            ringCombine.setVisible(true);

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

            bgFlickr.setOnSucceeded(e -> {
                btnCreateCreation.setDisable(false);
                ringCombine.setVisible(false);

                //once all images are downloaded, we place each into the image grid
                int imgCount = 1;
                for (ImageView imgView: gridImages){
                    File file = new File(TEMP.toString() + "/" + query + "-" + imgCount + ".jpg");
                    Image image = new Image(file.toURI().toString());
                    imgView.setImage(image);
                    imgCount++;
                }

                //imgGrid1 = new ImageView(getClass().getResource(TEMP.toString() + "/" + query + "-1.jpg").toExternalForm());
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
        btnCreateChunk.fire();
    }

    @FXML
    void txtCreationEnter(ActionEvent event) {
        btnCreateCreation.fire();
    }

    @FXML
    void txtSearchEnter(ActionEvent event) {
        btnSearch.fire();
    }

}
