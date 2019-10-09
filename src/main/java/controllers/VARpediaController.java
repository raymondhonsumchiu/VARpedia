package main.java.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
    private MediaPlayer mp;
    private Duration duration;
    private List<ImageView> gridImages;
    private List<ToggleButton> gridToggles;
    private ObservableList<String> chunksList = FXCollections.observableArrayList();
    private ObservableList<String> actualChunksList = FXCollections.observableArrayList();
    private ArrayList<String> selectedImgs;

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
    private ImageView imgPlayPause;

    @FXML
    private ImageView imgVolume;

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
    private MediaView mvPlayCreation;

    @FXML
    private Pane mvPane;

    @FXML
    private HBox mediaToolbar;

    @FXML
    private Button btnPlayCreation;

    @FXML
    private Button btnPlayPause;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnReverse;

    @FXML
    private Label lblCurrentTime;

    @FXML
    private Label lblTotalTime;

    @FXML
    private Slider sliderProgress;

    @FXML
    private ProgressBar progressSlider;

    @FXML
    private Slider sliderVol;

    @FXML
    private ProgressBar progressVol;

    @FXML
    private RingProgressIndicator ringSearch;

    @FXML
    private RingProgressIndicator ringCombine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedImgs = new ArrayList<String>();

        // Clean up
        deleteDirectory(CHUNKS);
        deleteDirectory(TEMP);

        // Initialise "Creations" tab
        mediaToolbar.setVisible(false);
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
        tabMain.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (mp != null) { mp.pause();} // Pause media player on tab change
            if (tabMain.getSelectionModel().getSelectedIndex() == 0) {
                imgPlayPause.setImage(new Image(new File(ICONS.toString() + "/play-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            }
        });
        tabMain.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {
                if (mp != null) {
                    if (mp.getStatus() == MediaPlayer.Status.PAUSED) {
                        mp.play();
                    } else {
                        mp.pause();
                    }
                }
            }
        });
        btnPlayPause.addEventFilter(KeyEvent.ANY, Event::consume);
        btnForward.addEventFilter(KeyEvent.ANY, Event::consume);
        btnReverse.addEventFilter(KeyEvent.ANY, Event::consume);

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
        Collections.addAll(gridToggles,toggleGrid1,toggleGrid2,toggleGrid3,toggleGrid4,toggleGrid5,toggleGrid6,toggleGrid7,toggleGrid8,toggleGrid9,toggleGrid10,toggleGrid11,toggleGrid12);

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
        if (mp != null) { mp.stop();}

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
        //add error checks here:


        //-------------------
        //TEMPIMGS.mkdir();
        //obtain all selected images
        ArrayList<String> selectedImgs = new ArrayList<String>();
        int index = 0;
        for(ToggleButton imgButton: gridToggles){
            if (imgButton.isSelected()){
                String img = this.selectedImgs.get(index);
                selectedImgs.add(img);
            }
            index++;
        }

        //check if all imgs correct
        for(String path: selectedImgs){
            System.out.println(path);
        }

        //------------testing purposes, will be in bg task-------
//        TEMPIMGS.mkdir();
//        for (String img: selectedImgs) {
//            ProcessBuilder b1 = new ProcessBuilder("/bin/bash", "-c", "cp " + TEMP.toString() + img + " " + TEMPIMGS.toString() + img);
//            Process p1 = null;
//            try {
//                p1 = b1.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                p1.waitFor();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //----------------------------------------------
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

    private String currentlyPlaying;
    private boolean mute;
    private double volume;
    @FXML
    void btnPlayCreationClicked(ActionEvent event) {
        if (listCreations.getSelectionModel().getSelectedItem() != null) {
            currentlyPlaying = listCreations.getSelectionModel().getSelectedItem();
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            mute = false;
            volume = 100;

            Media video = new Media(CREATIONS.toURI().toString() + currentlyPlaying + ".mp4");
            mp = new MediaPlayer(video);
            mvPlayCreation.setMediaPlayer(mp);
            mp.setAutoPlay(true);
            mvPlayCreation.fitWidthProperty().bind(mvPane.widthProperty());
            mvPlayCreation.fitHeightProperty().bind(mvPane.heightProperty());
            mvPlayCreation.setPreserveRatio(false);

            mediaToolbar.setVisible(true);
            progressSlider.progressProperty().bind(sliderProgress.valueProperty().divide(100.0));
            progressVol.progressProperty().bind(sliderVol.valueProperty().divide(100.0));

            mp.currentTimeProperty().addListener(ov -> updateValues());

            sliderVol.valueProperty().addListener(ov -> {
                if (sliderVol.isValueChanging()) {
                    mp.setVolume(sliderVol.getValue() / 100.0);
                }
            });

            mp.setOnReady(() -> {
                duration = mp.getMedia().getDuration();
                updateValues();
            });

            mp.setOnPlaying(() -> {
                imgPlayPause.setImage(new Image(new File(ICONS.toString() + "/pause-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            });

            mp.setOnPaused(() -> {
                imgPlayPause.setImage(new Image(new File(ICONS.toString() + "/play-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            });

            mp.setOnEndOfMedia(() -> {
                mp.stop();
                mp.pause();
            });
        }
    }

    private void updateValues() {
        Platform.runLater(() -> {
            Duration currentTime = mp.getCurrentTime();
            lblCurrentTime.setText(formatTime(currentTime, duration, false));
            lblTotalTime.setText(formatTime(currentTime, duration, true));
            sliderProgress.setDisable(duration.isUnknown());
            progressSlider.setDisable(duration.isUnknown());
            if (!sliderProgress.isDisabled() && duration.greaterThan(Duration.ZERO) && !sliderProgress.isValueChanging()) {
                sliderProgress.setValue(currentTime.toMillis() / duration.toMillis() * 100.0);
            }
            if (!sliderVol.isValueChanging()) {
                sliderVol.setValue((int) Math.round(mp.getVolume() * 100));
            }
        });
    }

    private static String formatTime(Duration elapsed, Duration duration, boolean totalTime) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                if (totalTime) {
                    return String.format("%d:%02d:%02d", durationHours, durationMinutes, durationSeconds);
                } else {
                    return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
                }
            } else {
                if (totalTime) {
                    return String.format("%02d:%02d", durationMinutes, durationSeconds);
                } else {
                    return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
                }
            }
        } else {
            if (!totalTime) {
                if (elapsedHours > 0) {
                    return String.format("%d:%02d:%02d", elapsedHours,
                            elapsedMinutes, elapsedSeconds);
                } else {
                    return String.format("%02d:%02d", elapsedMinutes,
                            elapsedSeconds);
                }
            } else {
                return "";
            }
        }
    }

    @FXML
    void sliderCreationDragged(MouseEvent event) {
        sliderProgress.valueProperty().addListener(ov -> {
            if (sliderProgress.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                mp.seek(duration.multiply(sliderProgress.getValue() / 100.0));
            }
        });
    }

    @FXML
    void btnPlayPauseClicked(ActionEvent event) {
        if (mp != null) {
            MediaPlayer.Status status = mp.getStatus();

            if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                // don't do anything in these states
                return;
            }

            // rewind the movie if we're sitting at the end
            if (mp.getCurrentTime().equals(duration)) {
                listCreations.getSelectionModel().select(currentlyPlaying);
                btnPlayCreation.fire();
            } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
                mp.play();
            } else {
                mp.pause();
            }
        }
    }

    @FXML
    void btnReverseClicked(ActionEvent event) {
        if (mp != null) {
            double time = mp.getCurrentTime().toMillis() - duration.toMillis() / 10.0;
            if (time > 0) {
                mp.seek(mp.getCurrentTime().subtract(duration.divide(10.0)));
            } else {
                mp.seek(mp.getStartTime());
            }
        }
    }

    @FXML
    void btnForwardClicked(ActionEvent event) {
        if (mp != null) {
            double time = mp.getCurrentTime().toMillis() + duration.toMillis() / 10.0;
            if (time < duration.toMillis()) {
                mp.seek(mp.getCurrentTime().add(duration.divide(10.0)));
            } else {
                mp.seek(duration);
            }
        }
    }

    @FXML
    void btnMuteClicked(ActionEvent event) {
        if (!mute) {
            volume = sliderVol.getValue();
            sliderVol.setValue(0);
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/mute-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            mp.setVolume(0);
            mute = true;
        } else {
            sliderVol.setValue(volume);
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            mp.setVolume(volume / 100.0);
            mute = false;
        }
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

                // Once all images are downloaded, we place each into the image grid
                int imgCount = 1;
                for (ImageView imgView: gridImages){
                    //set each image
                    File file = new File(TEMP.toString() + "/" + query + "-" + imgCount + ".jpg");
                    Image image = new Image(file.toURI().toString());
                    imgView.setImage(image);

                    //add image path to arraylist so it can be extracted later for creation
                    selectedImgs.add( "/" + query + "-" + imgCount + ".jpg");
                    imgCount++;
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
        if (event.getClickCount() == 2) {
            btnPlayCreation.fire();
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
