package org.example.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GUIController {

    public MainScreenController openAndGetController(Stage currentStage, String title){

        MainScreenController controller;
        Parent root;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreen.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentStage.setTitle(title);
        currentStage.setScene(new Scene(root));
        currentStage.show();

        return controller;
    }
    public static void openNewWindow(Stage currentStage, String pathToFXML, String title) {
        URL urlOfFXML;
        try {
            urlOfFXML = new File(pathToFXML).toURI().toURL();

        } catch (MalformedURLException e) {
            System.out.println("fichier manquant:" + pathToFXML);
            throw new RuntimeException(e);
        }
        Parent root;
        try{
            root = FXMLLoader.load(urlOfFXML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentStage.setTitle(title);
        currentStage.setScene(new Scene(root));
        currentStage.show();
        //return currentStage;
    }

    public static Alert getPopup(Alert.AlertType type,String title,String message,String header){
        Alert alert=new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(header);
        return alert;
    }

/*
    public static Popup getPopupInvalidPseudo(String pseudo){  //todo faire un VRAI popup
        Popup popup=new Popup();
        Label label=new Label("Invalid pseudo : "+pseudo+" is already taken");
        label.setStyle(" -fx-background-color: white; -fx-color-label-visible: red;");
        popup.getContent().add(label);
        label.setMinWidth(80);
        label.setMinHeight(50);
        return popup;
    }
*/
}
