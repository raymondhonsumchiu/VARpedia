package main.java.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.VARpedia;
import main.java.skins.progressindicator.RingProgressIndicator;
import main.java.tasks.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static main.java.VARpedia.*;

public class VARpediaController implements Initializable {
    //num of chunks
    int numChunks = 0;

    // General window fields
    private double xOffset = 0;
    private double yOffset = 0;
    private String css;

    // Creations tab fields
    private MediaPlayer playerCreation;
    private Duration duration;

    // Search tab fields
    private static Stage stage;
    public static double voicePitch;
    public static double voiceSpeed;
    private String query;

    // Combine tab fields
    private List<ImageView> gridImageViews;
    private List<ToggleButton> gridToggles;
    private ObservableList<String> chunksList = FXCollections.observableArrayList();
    private ObservableList<String> actualChunksList = FXCollections.observableArrayList();
    private ArrayList<String> selectedImgs;

    // Quiz tab fields
    private MediaPlayer playerQuiz;
    private Duration durationQuiz;

    @FXML
    private TabPane tabMain;

    @FXML
    private Tab tabSearch;

    @FXML
    private VBox vCreationsEmpty;

    @FXML
    private BorderPane paneCreations;

    @FXML
    private Label lblNumberCreations;

    @FXML
    private ListView<String> listCreations;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnSearchFlickr;

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
    private Button btnAddChunk;

    @FXML
    private Button btnRemoveChunk;

    @FXML
    private ListView<String> listSelectedChunks;

    @FXML
    private Button btnClearChunks;

    @FXML
    private VBox vImages;

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
    private ComboBox cboVoice;

    @FXML
    private ComboBox cboMusic;

    @FXML
    private ToggleButton btnDarkTheme;

    @FXML
    private ToggleButton btnLightTheme;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSearchFlickr;

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
    private VBox vMediaControls;

    @FXML
    private Button btnPlayCreation;

    @FXML
    private Button btnDeleteCreation;

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

    @FXML
    private RingProgressIndicator ringImages;

    @FXML
    private VBox vQuizTitle;

    @FXML
    private VBox vQuizError;

    @FXML
    private VBox vQuizPlayer;

    @FXML
    private Pane mvQuizPane;

    @FXML
    private MediaView mvQuiz;

    @FXML
    private Label lblQuizCurrentTime;

    @FXML
    private ProgressBar progressSliderQuiz;

    @FXML
    private Slider sliderProgressQuiz;

    @FXML
    private Label lblQuizTotalTime;

    @FXML
    private ImageView imgVolumeQuiz;

    @FXML
    private ProgressBar progressVolQuiz;

    @FXML
    private Slider sliderVolQuiz;

    @FXML
    private VBox vQuizCorrect;

    @FXML
    private Label lblQuizCorrect;

    @FXML
    private Button btnQuizNext;

    @FXML
    private Button btnQuizRetry;

    @FXML
    private Button btnQuizFinish;

    @FXML
    private HBox hQuizDifficulty;

    @FXML
    private HBox hQuizToolbar;

    @FXML
    private ToggleButton toggleQuizEasy;

    @FXML
    private ToggleButton toggleQuizMedium;

    @FXML
    private ToggleButton toggleQuizHard;

    @FXML
    private Button btnQuizBegin;

    @FXML
    private Button btnQuizReset;

    @FXML
    private HBox hQuizAnswer;

    @FXML
    private Label lblQuizAnswer;

    @FXML
    private TextField txtQuizAnswer;

    @FXML
    private Button btnQuizSubmit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // --------------------------------------------- Clean up -----------------------------------------------
        deleteDirectory(CHUNKS);
        deleteDirectory(TEMP);

        // --------------------------------------- Listener for tab changes -------------------------------------
        tabMain.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (playerCreation != null) { playerCreation.stop();} // Stop media player on tab change
            if (playerQuiz != null) {playerQuiz.stop();} // Stop quiz player on tab change
            if (btnSearchPreviewChunk.getText() == "Stop") { btnSearchPreviewChunk.fire(); } // Stop chunk preview on tab change
            if (btnPreviewChunkCombine.getText() == "Stop") { btnPreviewChunkCombine.fire(); } // Stop chunk preview on tab change
            int tab = tabMain.getSelectionModel().getSelectedIndex();
            if (tab == 0) {
                // Refresh list of creations, but also resets media player.
                initialiseCreationsTab();
            } else if (tab == 2) {
                //initialiseCombineTab();
            } else if (tab == 3) {
                // Reset quiz tab on change (prevents cheating, among other things)
                initialiseQuizTab();
            }
        });

        // ----------------------------- Initialise "Creations" tab --------------------------------
        initialiseCreationsTab();

        // Pressing space anywhere on this tab will pause the media if it is playing.
        tabMain.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE && tabMain.getSelectionModel().getSelectedIndex() == 0) {
                if (playerCreation != null) {
                    if (playerCreation.getStatus() == MediaPlayer.Status.PAUSED) {
                        playerCreation.play();
                    } else {
                        playerCreation.pause();
                    }
                }
            } else if (event.getCode() == KeyCode.DELETE && tabMain.getSelectionModel().getSelectedIndex() == 0) {
                if (listCreations.getSelectionModel().getSelectedItem() != null) {
                    btnDeleteCreation.fire();
                }
            }
        });
        btnPlayPause.addEventFilter(KeyEvent.ANY, Event::consume);
        btnForward.addEventFilter(KeyEvent.ANY, Event::consume);
        btnReverse.addEventFilter(KeyEvent.ANY, Event::consume);

        // ------------------------------ Initialise "Search" tab ----------------------------------------
        txaResults.setEditable(false);
        ringSearch.setVisible(false);

        cboVoice.getItems().addAll("US Male", "New Zealand Male");
        cboVoice.getSelectionModel().selectFirst();

        voicePitch = 1;
        voiceSpeed = 1;
        query = "";

        // set listview of chunks to observe the arraylist of chunks
        listChunksSearch.setItems(chunksList);
        //add listener to listview to allow for previewing of chunk text
        listChunksSearch.getSelectionModel().selectedItemProperty().addListener(e ->{
            String chunktxt = getChunkText(listChunksSearch.getSelectionModel().getSelectedItem());
            txaPreviewChunk1.setStyle("-fx-text-fill: font-color");
            txaPreviewChunk1.setText(chunktxt);
        });

        // ------------------------------------ Initialise "Combine" tab ----------------------------------
        initialiseCombineTab();

        // Add listener to list view to allow previewing chunk text
        listAllChunks.getSelectionModel().selectedItemProperty().addListener(e ->{
            String chunktxt = getChunkText(listAllChunks.getSelectionModel().getSelectedItem());
            txaPreviewChunk2.setStyle("-fx-text-fill: font-color");
            txaPreviewChunk2.setText(chunktxt);
        });

        // ------------------------------------ Initialise "Quiz" tab ------------------------------------
        initialiseQuizTab();

        // --------------------------------- Initialise "Options" tab --------------------------------------
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

    // ------------------------------------------- GENERAL ------------------------------------------------

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        if (playerCreation != null) { playerCreation.stop();}

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
    void btnMinimiseClicked(ActionEvent event) {
        VARpedia.primaryStage.setIconified(true);
    }

    @FXML
    void btnHelpClicked(ActionEvent event) {

    }

    @FXML
    void btnCloseClicked(ActionEvent event) {
        // Clean up on exit
        bg.shutdownNow();
        deleteDirectory(TEMP);
        deleteDirectory(CHUNKS);
        VARpedia.primaryStage.close();
        if (stage != null) { stage.close(); }
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
        if (tabMain.getSelectionModel().getSelectedIndex() == 1) {
            txtSearch.requestFocus();
        }
    }

    // ----------------------------------------------------------------------------------------------------

    // ---------------------------------- CREATIONS TAB METHODS -------------------------------------------

    private String currentlyPlaying;
    private boolean mute;
    private double volume;

    private void initialiseCreationsTab() {
        // Hide and display the appropriate panels
        vMediaControls.setVisible(false);
        if (isNonEmptyDirectory(CREATIONS)) {
            vCreationsEmpty.setVisible(false);
            paneCreations.setVisible(true);

            // Find and list all creations using bash
            ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "ls");
            b.directory(CREATIONS);
            Process p;
            try {
                p = b.start();
                InputStream out = p.getInputStream();
                BufferedReader stdout = new BufferedReader(new InputStreamReader(out));

                List<String> list = new ArrayList<>();
                String line;
                while ((line = stdout.readLine()) != null) {
                    //list.add(line.substring(0, line.length() - 4));
                    list.add(line);
                }

                lblNumberCreations.setText("" + list.size());
                Collections.sort(list);
                listCreations.setItems(FXCollections.observableArrayList(list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            vCreationsEmpty.setVisible(true);
            paneCreations.setVisible(false);
        }
    }

    private void updateValues() {
        Platform.runLater(() -> {
            Duration currentTime = playerCreation.getCurrentTime();
            lblCurrentTime.setText(formatTime(currentTime, duration, false));
            lblTotalTime.setText(formatTime(currentTime, duration, true));
            sliderProgress.setDisable(duration.isUnknown());
            progressSlider.setDisable(duration.isUnknown());
            if (!sliderProgress.isDisabled() && duration.greaterThan(Duration.ZERO) && !sliderProgress.isValueChanging()) {
                sliderProgress.setValue(currentTime.toMillis() / duration.toMillis() * 100.0);
            }
            if (!sliderVol.isValueChanging()) {
                sliderVol.setValue((int) Math.round(playerCreation.getVolume() * 100));
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
    void btnPlayCreationClicked(ActionEvent event) {
        if (listCreations.getSelectionModel().getSelectedItem() != null) {
            currentlyPlaying = listCreations.getSelectionModel().getSelectedItem();
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            mute = false;
            volume = 100;

            Media video = new Media(CREATIONS.toURI().toString() + currentlyPlaying + "/" + currentlyPlaying + ".mp4");
            playerCreation = new MediaPlayer(video);
            mvPlayCreation.setMediaPlayer(playerCreation);
            playerCreation.setAutoPlay(true);
            mvPlayCreation.fitWidthProperty().bind(mvPane.widthProperty());
            mvPlayCreation.fitHeightProperty().bind(mvPane.heightProperty());
            mvPlayCreation.setPreserveRatio(false);

            vMediaControls.setVisible(true);
            progressSlider.progressProperty().bind(sliderProgress.valueProperty().divide(100.0));
            progressVol.progressProperty().bind(sliderVol.valueProperty().divide(100.0));

            playerCreation.currentTimeProperty().addListener(ov -> updateValues());

            sliderVol.valueProperty().addListener(ov -> {
                if (sliderVol.isValueChanging()) {
                    playerCreation.setVolume(sliderVol.getValue() / 100.0);
                }
            });

            playerCreation.setOnReady(() -> {
                duration = playerCreation.getMedia().getDuration();
                updateValues();
            });

            playerCreation.setOnPlaying(() -> {
                imgPlayPause.setImage(new Image(new File(ICONS.toString() + "/pause-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            });

            playerCreation.setOnPaused(() -> {
                imgPlayPause.setImage(new Image(new File(ICONS.toString() + "/play-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            });

            playerCreation.setOnEndOfMedia(() -> {
                playerCreation.stop();
                playerCreation.pause();
            });
        }
    }

    @FXML
    void sliderCreationDragged(MouseEvent event) {
        sliderProgress.valueProperty().addListener(ov -> {
            if (sliderProgress.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                playerCreation.seek(duration.multiply(sliderProgress.getValue() / 100.0));
            }
        });
    }

    @FXML
    void btnPlayPauseClicked(ActionEvent event) {
        if (playerCreation != null) {
            MediaPlayer.Status status = playerCreation.getStatus();

            if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                // don't do anything in these states
                return;
            }

            // rewind the movie if we're sitting at the end
            if (playerCreation.getCurrentTime().equals(duration)) {
                listCreations.getSelectionModel().select(currentlyPlaying);
                btnPlayCreation.fire();
            } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
                playerCreation.play();
            } else {
                playerCreation.pause();
            }
        }
    }

    @FXML
    void btnReverseClicked(ActionEvent event) {
        if (playerCreation != null) {
            double time = playerCreation.getCurrentTime().toMillis() - duration.toMillis() / 10.0;
            if (time > 0) {
                playerCreation.seek(playerCreation.getCurrentTime().subtract(duration.divide(10.0)));
            } else {
                playerCreation.seek(playerCreation.getStartTime());
            }
        }
    }

    @FXML
    void btnForwardClicked(ActionEvent event) {
        if (playerCreation != null) {
            double time = playerCreation.getCurrentTime().toMillis() + duration.toMillis() / 10.0;
            if (time < duration.toMillis()) {
                playerCreation.seek(playerCreation.getCurrentTime().add(duration.divide(10.0)));
            } else {
                playerCreation.seek(duration);
            }
        }
    }

    @FXML
    void btnMuteClicked(ActionEvent event) {
        if (!mute) {
            volume = sliderVol.getValue();
            sliderVol.setValue(0);
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/mute-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            playerCreation.setVolume(0);
            mute = true;
        } else {
            sliderVol.setValue(volume);
            imgVolume.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            playerCreation.setVolume(volume / 100.0);
            mute = false;
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
                deleteDirectory(new File(CREATIONS.toString() + "/" + vid));
                //new File(CREATIONS.toString() + System.getProperty("file.separator") + vid + ".mp4").delete();
            }
        }
    }

    @FXML
    void listCreationsClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            btnPlayCreation.fire();
        }
    }

    // -----------------------------------------------------------------------------------------------------

    // ------------------------------------- SEARCH TAB METHODS ------------------------------------------

    private boolean error;

    // This method returns the text content of a chunk as a string
    private String getChunkText(String chunkName) {
        ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "cat " + chunkName + "/" + chunkName + ".txt");
        b.directory(CHUNKS);
        String chunktxt = "";
        try {
            Process p = b.start();
            InputStream stdout = p.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

            //use obtained string to add to textArea
            String line;
            while ((line = stdoutBuffered.readLine()) != null ) {
                chunktxt += line + "\n";
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return chunktxt;
    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        // Clean up for new search
        deleteDirectory(TEMP);
        deleteDirectory(CHUNKS);
        error = false;

        query = txtSearch.getText().trim().toLowerCase();
        if (!query.isEmpty()) {

            // Multithreading - search Wikit for query
            WikitTask bgWikit = new WikitTask(query);
            bg.submit(bgWikit);

            // Lock controls, reveal loading ring
            txaResults.clear();
            txaResults.setEditable(false);
            btnSearch.setDisable(true);
            btnSearchFlickr.setDisable(true);
            ringSearch.setVisible(true);

            fillGridImages(query);

            bgWikit.setOnSucceeded(e -> {
                // Unlock controls, hide loading ring
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
                    error = true;
                } else if (list.get(0).contains("Ambiguous results, ") || list.get(0).contains("may also refer to:") || (list.size() > 1 && list.get(1).contains("may also refer to:"))) {
                    txaResults.setText("Ambiguous results found for \"" + query + "\".");
                    txaResults.setStyle("-fx-text-fill: close-color;");
                    // Reset search field
                    txtSearch.selectAll();
                    txtSearch.requestFocus();
                    error = true;
                } else {
                    txaResults.setEditable(true);
                    for (String s : list) {
                        txaResults.appendText(s);
                    }
                    txaResults.requestFocus();
                    txaResults.positionCaret(0);
                    error = false;
                }
            });
        }
    }

    @FXML
    void btnVoiceOptionClicked(ActionEvent event) throws IOException {
        if (btnSearchPreviewChunk.getText() == "Stop") { btnSearchPreviewChunk.fire(); }
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/voiceoptions.fxml"));

        if (stage != null) {stage.close();}
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);

        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root, 500, 240);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void btnCombineClicked(ActionEvent event) {
        tabMain.getSelectionModel().select(2);
    }

    @FXML
    void listChunksSearchClicked(MouseEvent event) {
        txaResults.deselect();
        if (event.getClickCount() == 2) {
            btnSearchPreviewChunk.fire();
        }
    }

    @FXML
    void txtSearchEnter(ActionEvent event) {
        btnSearch.fire();
    }

    @FXML
    void txtChunkEnter(ActionEvent event) {
        btnCreateChunk.fire();
    }

    @FXML
    void txaResultsDragged(MouseEvent event) {
        listChunksSearch.getSelectionModel().clearSelection();
        txaPreviewChunk1.setStyle("-fx-text-fill: font-color");
        txaPreviewChunk1.setText("");
    }

    // ----------------------------------------------------------------------------------------------------

    // ---------------------------------------- COMBINE TAB METHODS ----------------------------------------

    private void initialiseCombineTab() {
        selectedImgs = new ArrayList<String>();
        gridImageViews = new ArrayList<>();
        gridToggles = new ArrayList<>();
        Collections.addAll(gridImageViews, imgGrid1, imgGrid2, imgGrid3, imgGrid4, imgGrid5, imgGrid6, imgGrid7, imgGrid8, imgGrid9, imgGrid10, imgGrid11, imgGrid12);
        Collections.addAll(gridToggles, toggleGrid1, toggleGrid2, toggleGrid3, toggleGrid4, toggleGrid5, toggleGrid6, toggleGrid7, toggleGrid8, toggleGrid9, toggleGrid10, toggleGrid11, toggleGrid12);

        // Bind list views to lists
        listAllChunks.setItems(chunksList);
        listSelectedChunks.setItems(actualChunksList);

        // Hide images and progress rings
        ringCombine.setVisible(false);
        ringImages.setVisible(false);
        vImages.setVisible(false);

        cboMusic.getItems().addAll("No music", "Happy Piano", "Funny Piano", "Groovy Music");
        cboMusic.getSelectionModel().selectFirst();
    }

    private void fillGridImages(String query) {
        if (!query.isEmpty()) {

            // Multithreading - search Flickr API for images
            FlickrTask bgFlickr = new FlickrTask(query);
            bg.submit(bgFlickr);

            // Lock controls, reveal loading ring
            btnCreateCreation.setDisable(true);
            btnPreviewCreation.setDisable(true);
            vImages.setVisible(false);
            ringImages.setVisible(true);

            // Reset selection
            for (ToggleButton t : gridToggles) {
                t.setSelected(false);
            }

            bgFlickr.setOnSucceeded(e -> {
                // Unlock controls, hide loading ring
                btnCreateCreation.setDisable(false);
                btnPreviewCreation.setDisable(false);
                btnSearchFlickr.setDisable(false);
                vImages.setVisible(true);
                ringImages.setVisible(false);

                // Once all images are downloaded, we place each into the image grid
                int imgCount = 1;
                for (ImageView imgView : gridImageViews) {
                    //set each image
                    File file = new File(TEMPIMGS.toString() + "/" + query + "-" + imgCount + ".jpg");
                    Image image = new Image(file.toURI().toString());
                    double n = (image.getWidth() < image.getHeight()) ? image.getWidth() : image.getHeight();
                    double x = (image.getWidth() - n) / 2;
                    double y = (image.getHeight() - n) / 2;
                    Rectangle2D rect = new Rectangle2D(x, y, n, n);

                    imgView.setViewport(rect);
                    imgView.setSmooth(true);
                    imgView.setImage(image);

                    // Add image path to arraylist so it can be extracted later for creation
                    selectedImgs.add("/" + query + "-" + imgCount + ".jpg");
                    imgCount++;
                }
                if (error) {
                    deleteDirectory(TEMPIMGS);
                    vImages.setVisible(false);
                }
                txtSearchFlickr.clear();
            });
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
    void btnRemoveChunkClicked(ActionEvent event) {
        //if an item is selected, it will be removed
        if(listSelectedChunks.getSelectionModel().getSelectedItem() != null){
            int index = listSelectedChunks.getSelectionModel().getSelectedIndex();
            listSelectedChunks.getItems().remove(index);
        }
    }

    @FXML
    void btnClearChunksClicked(ActionEvent event) {
        //clear all selected items
        listSelectedChunks.getItems().clear();
    }

    @FXML
    void btnDeleteChunkClicked(ActionEvent event) {
        if(listAllChunks.getSelectionModel().getSelectedItem() != null){
            int index = listAllChunks.getSelectionModel().getSelectedIndex();
            listAllChunks.getItems().remove(index);
        }
    }

    @FXML
    void btnSearchFlickrClicked(ActionEvent event) {
        fillGridImages(txtSearchFlickr.getText().trim().toLowerCase());
    }

    @FXML
    void listAllChunksClicked(MouseEvent event) {
        listSelectedChunks.getSelectionModel().clearSelection();
        if (event.getClickCount() == 2) {
            btnAddChunk.fire();
        }
    }

    @FXML
    void listSelectedChunksClicked(MouseEvent event) {
        listAllChunks.getSelectionModel().clearSelection();
        if (event.getClickCount() == 2) {
            btnRemoveChunk.fire();
        }
    }

    @FXML
    void txtCreationEnter(ActionEvent event) {
        btnCreateCreation.fire();
    }

    @FXML
    void txtSearchFlickrEnter(ActionEvent event) {
        btnSearchFlickr.fire();
    }

    // -----------------------------------------------------------------------------------------------------

    // ----------------------------------------- QUIZ TAB METHODS ---------------------------------------------
    private int numQuestions;
    private int currentQuestion;
    private int numCorrect;
    private boolean quizMute;
    private double quizVolume;

    private void initialiseQuizTab() {
        if (CREATIONS.exists() && CREATIONS.isDirectory()) {
            numQuestions = CREATIONS.listFiles().length;
        }
        currentQuestion = 0;
        numCorrect = 0;
        hQuizToolbar.setVisible(true);
        hQuizDifficulty.setDisable(false);
        vQuizTitle.setVisible(true);
        vQuizError.setVisible(false);
        vQuizPlayer.setVisible(false);
        vQuizCorrect.setVisible(false);
        hQuizAnswer.setVisible(false);
        toggleQuizEasy.setSelected(true);
        toggleQuizMedium.setSelected(false);
        toggleQuizHard.setSelected(false);
        if (!isNonEmptyDirectory(CREATIONS)) {
            hQuizToolbar.setVisible(false);
            vQuizTitle.setVisible(false);
            vQuizError.setVisible(true);
        }
    }

    @FXML
    void toggleQuizEasyClicked(ActionEvent event) {
        if (toggleQuizEasy.isSelected()) {
            toggleQuizMedium.setSelected(false);
            toggleQuizHard.setSelected(false);
        } else {
            toggleQuizEasy.setSelected(true);
        }
    }

    @FXML
    void toggleQuizMediumClicked(ActionEvent event) {
        if (toggleQuizMedium.isSelected()) {
            toggleQuizEasy.setSelected(false);
            toggleQuizHard.setSelected(false);
        } else {
            toggleQuizMedium.setSelected(true);
        }
    }

    @FXML
    void toggleQuizHardClicked(ActionEvent event) {
        if (toggleQuizHard.isSelected()) {
            toggleQuizMedium.setSelected(false);
            toggleQuizEasy.setSelected(false);
        } else {
            toggleQuizHard.setSelected(true);
        }
    }

    @FXML
    void btnQuizBeginClicked(ActionEvent event) {
        // Hide/reveal appropriate components
        hQuizDifficulty.setDisable(true);
        vQuizTitle.setVisible(false);
        vQuizPlayer.setVisible(true);
        hQuizAnswer.setVisible(true);

        imgVolumeQuiz.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
        quizMute = false;
        quizVolume = 100;

        //TODO this section: pick quiz files depending on difficulty

        // -------------------------------------------------------------------------------------------------------
        int difficulty = 0; // 0 = easy, 1 = med, 2 = hard
        if (toggleQuizMedium.isSelected()) {
            difficulty = 1;
        } else if (toggleQuizHard.isSelected()) {
            difficulty = 2;
        }

        // Randomise list of files
        File[] fi = CREATIONS.listFiles(); // change this to the proper directory
        List<File> files = new ArrayList<>();
        for (File f : fi) {
            files.add(f);
        }
        Collections.shuffle(files);

        Media video = new Media(files.get(currentQuestion).toURI().toString());
        currentQuestion++;

        // -------------------------------------------------------------------------------------------------------

        // Play selected file
        playerQuiz = new MediaPlayer(video);
        mvQuiz.setMediaPlayer(playerQuiz);
        playerQuiz.setAutoPlay(true);
        mvQuiz.fitWidthProperty().bind(mvQuizPane.widthProperty());
        mvQuiz.fitHeightProperty().bind(mvQuizPane.heightProperty());
        mvQuiz.setPreserveRatio(false);

        progressSliderQuiz.progressProperty().bind(sliderProgressQuiz.valueProperty().divide(100.0));
        progressVolQuiz.progressProperty().bind(sliderVolQuiz.valueProperty().divide(100.0));

        playerQuiz.currentTimeProperty().addListener(ov -> updateValuesQuiz());

        sliderVolQuiz.valueProperty().addListener(ov -> {
            if (sliderVolQuiz.isValueChanging()) {
                playerQuiz.setVolume(sliderVolQuiz.getValue() / 100.0);
            }
        });

        playerQuiz.setOnReady(() -> {
            durationQuiz = playerQuiz.getMedia().getDuration();
            updateValuesQuiz();
        });

        playerQuiz.setOnEndOfMedia(() -> {
            playerQuiz.stop();

            // Display "out of time" panel
            vQuizPlayer.setVisible(false);
            hQuizAnswer.setVisible(false);
            vQuizCorrect.setVisible(true);
            btnQuizRetry.setVisible(true);
            lblQuizCorrect.setText("Out of time!");
        });
    }

    private void updateValuesQuiz() {
        Platform.runLater(() -> {
            Duration currentTime = playerQuiz.getCurrentTime();
            lblQuizCurrentTime.setText(formatTime(currentTime, durationQuiz, false));
            lblQuizTotalTime.setText(formatTime(currentTime, durationQuiz, true));
            sliderProgressQuiz.setDisable(durationQuiz.isUnknown());
            progressSliderQuiz.setDisable(durationQuiz.isUnknown());
            if (!sliderProgressQuiz.isDisabled() && durationQuiz.greaterThan(Duration.ZERO) && !sliderProgressQuiz.isValueChanging()) {
                sliderProgressQuiz.setValue(currentTime.toMillis() / durationQuiz.toMillis() * 100.0);
            }
            if (!sliderVolQuiz.isValueChanging()) {
                sliderVolQuiz.setValue((int) Math.round(playerQuiz.getVolume() * 100));
            }
        });
    }

    @FXML
    void btnQuizResetClicked(ActionEvent event) {
        initialiseQuizTab();
        if (playerQuiz != null) {playerQuiz.stop();}
    }

    @FXML
    void btnQuizFinishClicked(ActionEvent event) {

    }

    @FXML
    void btnQuizNextClicked(ActionEvent event) {

    }

    @FXML
    void btnQuizRetryClicked(ActionEvent event) {

    }

    @FXML
    void btnQuizSubmitClicked(ActionEvent event) {
        //TODO logic (use the variable currentQuestion)

        // if correct
        playerQuiz.stop();
        vQuizPlayer.setVisible(false);
        hQuizAnswer.setVisible(false);
        vQuizCorrect.setVisible(true);
        lblQuizCorrect.setText("Correct!");
        btnQuizRetry.setVisible(false);
    }

    @FXML
    void txtQuizAnswerEnter(ActionEvent event) {
        btnQuizSubmit.fire();
    }

    @FXML
    void btnMuteQuizClicked(ActionEvent event) {
        if (!quizMute) {
            quizVolume = sliderVolQuiz.getValue();
            sliderVolQuiz.setValue(0);
            imgVolumeQuiz.setImage(new Image(new File(ICONS.toString() + "/mute-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            playerQuiz.setVolume(0);
            quizMute = true;
        } else {
            sliderVolQuiz.setValue(quizVolume);
            imgVolumeQuiz.setImage(new Image(new File(ICONS.toString() + "/volume-" + (isDark ? "dark" : "light") + ".png").toURI().toString()));
            playerQuiz.setVolume(quizVolume / 100.0);
            quizMute = false;
        }
    }

    // -----------------------------------------------------------------------------------------------

    // --------------------------------------- OPTIONS TAB METHODS -----------------------------------------

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

    // -----------------------------------------------------------------------------------------------------

    Process p1;
    @FXML
    void btnPreviewChunkClicked(ActionEvent event) throws IOException, InterruptedException {
        CHUNKS.mkdirs();

        if(btnSearchPreviewChunk.getText().equals("Preview")) {
            // Obtain selected text and check num of words
            String previewText = txaResults.getSelectedText().trim();
            int numWords = previewText.length() - previewText.replaceAll("\\s", "").length();

            if (listChunksSearch.getSelectionModel().getSelectedItem() != null) {
                // If a chunk from the list is selected, preview its audio
                ProcessBuilder b1 = new ProcessBuilder("/bin/bash", "-c", "ffplay -nodisp -autoexit " + listChunksSearch.getSelectionModel().getSelectedItem());
                b1.directory(new File(CHUNKS.toString() + "/" + listChunksSearch.getSelectionModel().getSelectedItem()));
                p1 = b1.start();
                // bg thread keeps an eye on the process while it is alive
                PreviewChunkTask previewTask = new PreviewChunkTask(p1);
                bg.submit(previewTask);
                // Once finished, the text is set back
                previewTask.setOnSucceeded(e ->{
                    btnSearchPreviewChunk.setText("Preview");
                });

                // Set text to allowing stopping of audio
                btnSearchPreviewChunk.setText("Stop");
            } else if (previewText.length() == 0) {
                txaPreviewChunk1.setText("Nothing to preview.");
                txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
            } else if (numWords > 40) {
                txaPreviewChunk1.setText("Please select no more than 40 words per chunk.");
                txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
            } else {
                // Handle previewing of chunk
                txaPreviewChunk1.setStyle("-fx-text-fill: font-color");
                txaPreviewChunk1.setText(previewText);

                // Create preview audio file, this will allow stopping
                File festChunk = new File(CHUNKS.toString() + System.getProperty("file.separator") + "festivalChunk");
                CHUNKS.mkdirs();
                festChunk.createNewFile();
                BufferedWriter w = new BufferedWriter(new FileWriter(CHUNKS.toString() + System.getProperty("file.separator") + "festivalChunk"));

                String voice = "voice_kal_diphone";
                if (cboVoice.getValue() == "New Zealand Male") { voice = "voice_akl_nz_jdt_diphone"; }
                double stretch = 2.0 - voiceSpeed;
                if (stretch < 0.1) { stretch = 0.1; }
                int pitch = 60 + (int) (Math.round(voicePitch * 100) / 2);

                // Set voice
                w.write("(" + voice + ")\n");

                // Set pitch
                if (voicePitch > 1.1 || voicePitch < 0.9) {
                    w.write("(set! duffint_params '((start " + pitch + ") (end " + pitch + ")))\n");
                    w.write("(Parameter.set 'Int_Method 'DuffInt)\n");
                    w.write("(Parameter.set 'Int_Target_Method Int_Targets_Default)\n");
                }

                // Set speed
                w.write("(Parameter.set 'Duration_Stretch " + stretch + ")\n");

                // Save audio to file
                w.write("(set! utt1 (Utterance Text \"" + previewText + "\"))\n");
                w.write("(utt.synth utt1)\n");
                w.write("(utt.save.wave utt1 \"previewChunk\" 'riff)");
                w.close();
                ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "festival -b festivalChunk");
                b.directory(CHUNKS);
                Process p = b.start();
                p.waitFor();

                // Second process for actually playing the audio
                ProcessBuilder b1 = new ProcessBuilder("/bin/bash", "-c", "ffplay -nodisp -autoexit previewChunk");
                b1.directory(CHUNKS);
                p1 = b1.start();

                // bg thread keeps an eye on the process while it is alive
                PreviewChunkTask previewTask = new PreviewChunkTask(p1);
                bg.submit(previewTask);
                // Once finished, the text is set back
                previewTask.setOnSucceeded(e ->{
                    btnSearchPreviewChunk.setText("Preview");
                });

                // Set text to allowing stopping of audio
                btnSearchPreviewChunk.setText("Stop");

            }
        } else {
            // Handle stopping of preview
            p1.destroy();
            btnSearchPreviewChunk.setText("Preview");
        }

    }

    @FXML
    void btnPreviewChunkCombineClicked(ActionEvent event) throws IOException, InterruptedException {
        CHUNKS.mkdirs();

        if(btnPreviewChunkCombine.getText().equals("Preview")) {
            if (listAllChunks.getSelectionModel().getSelectedItem() != null) {
                // If a chunk from the list is selected, preview its audio
                ProcessBuilder b1 = new ProcessBuilder("/bin/bash", "-c", "ffplay -nodisp -autoexit " + listAllChunks.getSelectionModel().getSelectedItem());
                b1.directory(new File(CHUNKS.toString() + "/" + listAllChunks.getSelectionModel().getSelectedItem()));
                p1 = b1.start();
                // Set text to allowing stopping of audio
                btnPreviewChunkCombine.setText("Stop");
            }
        } else {
            // Handle stopping of preview
            p1.destroy();
            btnPreviewChunkCombine.setText("Preview");
        }

    }

    @FXML
    void btnCreateChunkClicked(ActionEvent event) throws IOException, InterruptedException {
        CHUNKS.mkdirs();

        String selectedText = txaResults.getSelectedText().trim();
        int numWords = selectedText.length() - selectedText.replaceAll("\\s", "").length();
        if (selectedText.length() == 0) {
            txaPreviewChunk1.setText("No text selected.");
            txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
        } else if (numWords > 40) {
            txaPreviewChunk1.setText("Please select no more than 40 words per chunk.");
            txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
        } else {

            //obtain chunk name
            String chunkName = txtChunkName.getText();

            if (chunkName.startsWith("-") || !Pattern.matches("^[-_a-zA-Z0-9]*$", chunkName)) {
                txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
                txaPreviewChunk1.setText("Invalid chunk name, please choose another.");
                txtChunkName.selectAll();
                txtChunkName.requestFocus();
            } else if (chunksList.contains(chunkName)) {
                txaPreviewChunk1.setStyle("-fx-text-fill: close-color");
                txaPreviewChunk1.setText("\"" + chunkName + "\" already exists!\n Please choose a unique chunk name.");
                txtChunkName.selectAll();
                txtChunkName.requestFocus();
            } else {
                txaPreviewChunk1.setStyle("-fx-text-fill: font-color");
                txaPreviewChunk1.setText(selectedText);
                txtChunkName.clear();

                //handle default chunk name
                numChunks++;
                if (chunkName.isEmpty()) {
                    chunkName = query + numChunks;
                }

                //create new chunk's directory
                File NEWCHUNK = new File(CHUNKS.toString() + "/" + chunkName);
                NEWCHUNK.mkdirs();

                File festChunk = new File(NEWCHUNK.toString() + System.getProperty("file.separator") + "festivalChunk");
                festChunk.createNewFile();
                BufferedWriter w = new BufferedWriter(new FileWriter(NEWCHUNK.toString() + System.getProperty("file.separator") + "festivalChunk"));

                String voice = "voice_kal_diphone";
                if (cboVoice.getValue() == "New Zealand Male") { voice = "voice_akl_nz_jdt_diphone"; }
                double stretch = 2.0 - voiceSpeed;
                if (stretch < 0.1) { stretch = 0.1; }
                int pitch = 60 + (int) (Math.round(voicePitch * 100) / 2);

                // Set voice
                w.write("(" + voice + ")\n");

                // Set pitch
                if (voicePitch > 1.1 || voicePitch < 0.9) {
                    w.write("(set! duffint_params '((start " + pitch + ") (end " + pitch + ")))\n");
                    w.write("(Parameter.set 'Int_Method 'DuffInt)\n");
                    w.write("(Parameter.set 'Int_Target_Method Int_Targets_Default)\n");
                }

                // Set speed
                w.write("(Parameter.set 'Duration_Stretch " + stretch + ")\n");

                // Save audio to file
                w.write("(set! utt1 (Utterance Text \"" + selectedText + "\"))\n");
                w.write("(utt.synth utt1)\n");
                w.write("(utt.save.wave utt1 \"" + chunkName + "\" 'riff)");
                w.close();
                ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "festival -b festivalChunk");
                b.directory(NEWCHUNK);
                Process p = b.start();
                p.waitFor();

                ProcessBuilder b2 = new ProcessBuilder("/bin/bash", "-c", "echo \"" + txaResults.getSelectedText().trim() + "\" > " + chunkName + ".txt");
                b2.directory(NEWCHUNK);
                Process p2 = b2.start();
                p2.waitFor();


                chunksList.add(chunkName);
                Collections.sort(chunksList);

            }
        }

        //PLace holder for chunk creation
        //this list will house the names of all the chunks that will be displayed by the chunk listviews
        //ideally these following lines will be kept, just swapping the name of the chunk

    }

    @FXML
    void btnCreateCreationClicked(ActionEvent event) {
        //add error checks here:


        //-------------------
        //TEMPIMGS.mkdirs();
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

        String creationName = txtCreationName.getText();
        if(creationName.isEmpty()) {
            //PLACEHOLDER
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No name!");
            alert.showAndWait();

        }else if (actualChunksList.isEmpty()){
            //PLACEHOLDER
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No chunks!");
            alert.showAndWait();
        }else if (selectedImgs.isEmpty()) {
            //PLACEHOLDER
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No images!");
            alert.showAndWait();

        }else{
            String musicChoice = (String)cboMusic.getValue();
            CombineTask bgCreate = new CombineTask(creationName, query, actualChunksList, selectedImgs, musicChoice);
            bg.submit(bgCreate);

            bgCreate.setOnSucceeded(e ->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\"" + creationName + "\" created successfully!");
                alert.setTitle("New Creation");
                alert.setHeaderText("Success");
                alert.showAndWait();
            });
        }

    }

    @FXML
    void btnPreviewCreationClicked(ActionEvent event) {
        ArrayList<String> selectedImgs = new ArrayList<String>();
        int index = 0;
        for(ToggleButton imgButton: gridToggles){
            if (imgButton.isSelected()){
                String img = this.selectedImgs.get(index);
                selectedImgs.add(img);
            }
            index++;
        }

        if(actualChunksList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No chunks!");
            alert.showAndWait();
        }else if (selectedImgs.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No images!");
            alert.showAndWait();
        }else {

            String musicChoice = (String)cboMusic.getValue();
            PrevCombineTask bgCreate = new PrevCombineTask(query, actualChunksList, selectedImgs, musicChoice);
            bg.submit(bgCreate);

            bgCreate.setOnSucceeded(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\"preview\" created successfully!");
                alert.setTitle("New Creation");
                alert.setHeaderText("Success");
                alert.showAndWait();
            });
        }

    }

}
