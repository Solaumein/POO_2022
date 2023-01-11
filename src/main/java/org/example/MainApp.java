package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.User.User;

public class MainApp extends Application {
    @Override
    public void start(Stage firstStage) throws Exception {
        //InitApp();
        GUIController.openNewWindow(firstStage,"src/main/java/org/example/GUI/Login.fxml","Connexion");

       // ListContact.addNewContactSubscriber((user) -> MainScreenController.afficher);
    }
    User user=null;

    private void InitApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
