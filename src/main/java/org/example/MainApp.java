package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.GUI.GUIController;

import java.io.File;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage firstStage) throws Exception {
        //InitApp();
        GUIController.openNewWindow(firstStage,"src/main/java/org/example/GUI/Login.fxml","Connexion");
    }

    private void InitApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
