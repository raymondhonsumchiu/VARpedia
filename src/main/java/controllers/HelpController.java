package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the help menu pop-up
 * the only action to handle is the closing of the window since it is just
 * a smaller instruction manual for reading
 */
public class HelpController {

    @FXML
    void btnHelpOkClicked(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

}
