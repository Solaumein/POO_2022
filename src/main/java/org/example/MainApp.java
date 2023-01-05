package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage firstStage) throws Exception {

        URL urlofFXML=new File("src/main/java/org/example/GUI/Login.fxml").toURI().toURL();

        System.out.println(urlofFXML);
        Parent root = FXMLLoader.load(urlofFXML);
        firstStage.setTitle("Connexion");
        firstStage.setScene(new Scene(root));
        firstStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
