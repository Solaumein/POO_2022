package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnexionController {

    @FXML
    public Button connectButton;

    public void connectButtonAction(ActionEvent event){
        Stage mainStage = (Stage) connectButton.getScene().getWindow();
        URL urlofFXML= null;
        try {
            urlofFXML = new File("src/main/java/org/example/GUI/MainScreen.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(urlofFXML);
            mainStage.setTitle("Clavardage Entre Pote");
            mainStage.setScene(new Scene(root));
            mainStage.show();

        } catch (MalformedURLException e) {
            System.out.println("fichier manquant MainScreen.fxml");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
