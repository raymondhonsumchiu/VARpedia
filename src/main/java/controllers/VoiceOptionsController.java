package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static main.java.controllers.VARpediaController.voicePitch;
import static main.java.controllers.VARpediaController.voiceSpeed;

public class VoiceOptionsController implements Initializable {

    @FXML
    private Slider sliderPitch;

    @FXML
    private Slider sliderSpeed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sliderPitch.setValue(voicePitch * 50);
        sliderSpeed.setValue(voiceSpeed * 50);
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnResetClicked(ActionEvent event) {
        sliderPitch.setValue(50);
        sliderSpeed.setValue(50);
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        voicePitch = sliderPitch.getValue() / 50;
        voiceSpeed = sliderSpeed.getValue() / 50;

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}