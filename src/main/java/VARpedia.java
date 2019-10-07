package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VARpedia extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    public static Stage primaryStage;
    public static boolean isDark = false;
    public static final File CREATIONS = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Creations");
    public static final File TEMP = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "temp");
    public static final File CHUNKS = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "chunks");
    public static final ExecutorService bg = Executors.newFixedThreadPool(3);
    public static ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    public static ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    @Override
    public void start(Stage primaryStage) throws Exception{
        VARpedia.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/welcome.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root, 1440, 810);
        scene.getStylesheets().add(getClass().getResource("../resources/css/light.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Clean up on exit
        primaryStage.setOnCloseRequest(e -> {
            bg.shutdownNow();
            deleteDirectory(TEMP);
            deleteDirectory(CHUNKS);
        });
    }

    // Deletes a file or directory and all its contents recursively
    public static boolean deleteDirectory(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        return dir.delete();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
