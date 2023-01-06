package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.ListContact;
import org.example.NetworkManagerUDP;
import org.example.State;
import org.example.ThreadComUDP;

import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import java.io.File;
import java.io.IOException;

public class MainScreenController {



    @FXML
    public Button changePseudoButton;
    @FXML
    public Button deconnectButton;

    @FXML
    public TextField textFieldNewPseudo;
    @FXML
    public VBox listWindow;
    @FXML
    public Button SendButton;
    @FXML
    public TextArea textToSend;
    @FXML
    public VBox messageZone;


    public void deconnectButtonAction(ActionEvent event) {
        notifyDeconection();//send a notify of deconnection

        Stage mainStage = (Stage) deconnectButton.getScene().getWindow();
        String pathMainScreenFXML="src/main/java/org/example/GUI/Login.fxml";
        Stage decoStage= GUIController.openNewWindow(mainStage,pathMainScreenFXML,"Connexion");
        //todo mettre le textfield de la co a son ancien pseudo
    }
    boolean pseudoLibre=true;
    Consumer<String> invalidPseudoCallback= s -> pseudoLibre=false;
    public void changePseudoButtonAction(ActionEvent event) {
        if(!textFieldNewPseudo.isVisible())textFieldNewPseudo.setVisible(true);
        else{
            String newPseudo=textFieldNewPseudo.getText();
            notifyChangePseudo();//send a notify of deconnection
            //demarrage de l'attente de réponse
            int temps=0;
            while(temps<10 && pseudoLibre){
                temps++;
                try {//toute les 10ms ont test
                    Thread.sleep(10);//todo attendre réponse du premier avec future ou promesse
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(pseudoLibre){
                ListContact.selfUser.setPseudo(newPseudo);
            }else{
                alertInvalid(ListContact.selfUser.getPseudo());
            }
        }
    }

    private void alertInvalid(String pseudo) {
        //todo
    }

    public void SendButtonAction(ActionEvent event) throws IOException {
        String message = textToSend.getText();
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);
        textToSend.clear();

        FXMLLoader loader = new FXMLLoader((new File("src/main/java/org/example/GUI/ContactFrame.fxml").toURI().toURL()));
        System.out.println(loader.getLocation());
        Node n = loader.load();
        listWindow.getChildren().add(n);

        Node node = (Node)loader.getNamespace().get("contactFrame");

// Look up the label element inside the node
        Label label = (Label)node.lookup("#elementId");

// Modify the label's text property
        label.setText("New label text");
    }

    private void notifyDeconection() {
        NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.DECONNECTION);
    }
    public void displayReceivedMessage(String message){
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.LEFT);
        messageZone.getChildren().add(messageBubble);
    }
        private void notifyChangePseudo() {
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.CHANGEPSEUDO);
        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);
        thread1.start();
    }
}
