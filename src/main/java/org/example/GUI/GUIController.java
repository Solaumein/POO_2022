package org.example.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GUIController {
    public static Stage openNewWindow(Stage currentStage, String pathToFXML, String title) {
        URL urlofFXML= null;
        try {
            urlofFXML = new File(pathToFXML).toURI().toURL();

        } catch (MalformedURLException e) {
            System.out.println("fichier manquant:" + pathToFXML);
            throw new RuntimeException(e);
        }
        Parent root=null;
        try{
            root = FXMLLoader.load(urlofFXML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentStage.setTitle(title);
        currentStage.setScene(new Scene(root));
        currentStage.show();
        return currentStage;
    }

    public static Popup getPopupInvalidPseudo(String pseudo){
        Popup popup=new Popup();
        Label label=new Label("Invalid pseudo : "+pseudo+" is already taken");
        label.setStyle(" -fx-background-color: white; -fx-color-label-visible: red;");
        popup.getContent().add(label);
        label.setMinWidth(80);
        label.setMinHeight(50);
        return popup;
    }

}
