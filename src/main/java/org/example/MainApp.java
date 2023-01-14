package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.User.User;

public class MainApp extends Application {
    @Override
    public void start(Stage firstStage) throws Exception {
        InitApp();

        GUIController.openNewWindow(firstStage,"src/main/resources/Login.fxml","Connexion");

      /*  ServerSocket serverSocket=new ServerSocket(42069);
        Socket s= serverSocket.accept();
        BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
        while (true){
            System.out.println("on attend");
            System.out.println(in.readLine());
        }*/

       // ListContact.addNewContactSubscriber((user) -> MainScreenController.afficher);
    }
    User user=null;

    private void InitApp() {}

    public static void main(String[] args) {
        launch(args);
    }
}
