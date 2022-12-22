package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

public class ConnexionController {

    @FXML
    public Button connectButton;

    public void connectButtonAction(ActionEvent event){
        Stage stage = (Stage) connectButton.getScene().getWindow();
        stage.close();
    }

}
