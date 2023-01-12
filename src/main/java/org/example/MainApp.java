package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.Message.Message;
import org.example.User.User;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainApp extends Application {
    @Override
    public void start(Stage firstStage) throws Exception {
        //InitApp();
        GUIController.openNewWindow(firstStage,"src/main/resources/Login.fxml","Connexion");

    }
    User user=null;

    private void InitApp() {}

    public static void main(String[] args) {
        launch(args);
    }
}
