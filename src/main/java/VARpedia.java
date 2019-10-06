package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VARpedia extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    public static Stage primaryStage;
    public static boolean isDark = false;

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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
