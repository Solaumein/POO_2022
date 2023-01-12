package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.User.ContactEventHandler;
import org.example.User.ListContact;
import org.example.Network.NetworkManagerUDP;
import org.example.Network.State;
import org.example.Network.ThreadComUDP;

import javafx.scene.text.Text;
import org.example.User.User;
import org.example.User.UserAddress;

import java.io.IOException;
import java.util.function.Consumer;

import java.util.ArrayList;

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
        String pathMainScreenFXML="src/main/resources/Login.fxml";
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

    @FXML
    public Button sendButton;


    @FXML
    public HBox contactFrame;

    private void afficherMessageEnvoye(String message) throws IOException {
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/MessageFrameSent.fxml"));
        messageLoader.load();
        Node node;
        node = (Node)messageLoader.getNamespace().get("messageFrameContainer");
        Label messageToDisplay = (Label)node.lookup("#messageContent");
        messageToDisplay.setText(message);
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Envoyé à " + "HEURE");
        messageZone.getChildren().add(node);

    }

    public void afficherMessageRecu() throws IOException {
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/MessageFrame.fxml"));
        messageLoader.load();
        Node node;
        node = (Node)messageLoader.getNamespace().get("messageFrameContainer");
        Label messageToDisplay = (Label)node.lookup("#messageContent");
        messageToDisplay.setText("message  #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e #36393e");
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Reçu à " + "HEURE");
        HBox hbox =(HBox)node.lookup("#messageFrameContainer");
        hbox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        HBox hbox2 = (HBox)node.lookup("#messageFrame");
        HBox.setMargin(hbox2, new Insets(0,300,0,10));
        messageZone.getChildren().add(node);
    }

    public void SendButtonAction(ActionEvent event) throws IOException {
        /*Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);*/

        String message = textToSend.getText();
        afficherMessageEnvoye(message);
        textToSend.clear();



        ArrayList<String> li = new ArrayList<>();
        li.add("Tanguy");
        li.add("Onnig");
        li.add("Stefou");
        li.add("Gabi");
        li.add("Mattew");
        li.add("Romain");
        li.add("Aude");
        for (String pseudo : li) {
            UserAddress addr = null;
            User test = new User(addr, pseudo);
            afficherNouveauUser(test);

        }


    }

    public void sendButtonEnteredAction(){
        sendButton.setStyle("-fx-background-color:  #5E636E");}

    public void sendButtonExitAction(){
        sendButton.setStyle("-fx-background-color:  #282b30");}

    public void changePseudoButtonEnteredAction(){
        changePseudoButton.setStyle("-fx-background-color:  #424549");}

    public void changePseudoButtonExitAction(){
        changePseudoButton.setStyle("-fx-background-color: #1e2124");}

    public void DeconnexionButtonEnteredAction(){
        deconnectButton.setStyle("-fx-background-color:  #424549");}

    public void DeconnexionButtonExitAction(){
        deconnectButton.setStyle("-fx-background-color: #1e2124");}

    public void contactFrameEnteredAction(){
        contactFrame.setStyle("-fx-background-color: #424549;-fx-background-radius: 10px");}

    public void contactFrameExitAction(){
        contactFrame.setStyle("-fx-background-color: #282b30;-fx-background-radius: 10px");}

    /*public void contactFrameClickAction() throws IOException {
        Label labelPseudo = (Label)contactFrame.getChildren().get(1);
        String pseudo = labelPseudo.getText();
        System.out.println(pseudo);

    }*/

    public void contactFrameClickAction(){
        messageZone.getChildren().clear();
    }

    public void afficherNouveauUser(User user){
        //listWindow.getChildren().add(user.getNode());
        Node userNode = user.getNode();
        userNode.setOnMouseClicked(
                event -> {
                    contactFrameClickAction();
                }
        );
        listWindow.getChildren().add(userNode);
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
