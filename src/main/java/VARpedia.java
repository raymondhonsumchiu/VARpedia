package main.java;/*
 * Copyright (c) 2019, Kyle Zhou and Raymond Chiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public static final File TEMPIMGS = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "tempImages");
    public static final File CHUNKS = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "chunks");
    public static final File ICONS = new File(System.getProperty("user.dir") + "/src/main/resources/images");
    public static final ExecutorService bg = Executors.newFixedThreadPool(3);
    public static ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    public static ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    @Override
    public void start(Stage primaryStage) throws Exception{
        VARpedia.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/varpedia.fxml"));
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
