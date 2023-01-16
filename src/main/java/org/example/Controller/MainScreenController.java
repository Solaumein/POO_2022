package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.GUI.GUIController;
import org.example.Network.*;
import org.example.User.ContactEventHandler;
import org.example.User.ListContact;

import javafx.scene.text.Text;
import org.example.User.User;
import org.example.User.UserAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

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

    @FXML
    public Label myPseudo;

    @FXML
    public Button confirmNewpseudo;

    @FXML
    public ScrollPane messageScrollPane;


    public void initialize() {
        NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());
    }

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
                    sleep(10);//todo attendre réponse du premier avec future ou promesse
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

    public void changePseudoButtonClick() throws IOException {
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/MessageFrame.fxml"));
        messageLoader.load();
        Node node;
        node = (Node)messageLoader.getNamespace().get("messageFrameContainer");
        Label messageToDisplay = (Label)node.lookup("#messageContent");
        messageToDisplay.setText("message");
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Reçu à " + "HEURE");
        HBox hbox =(HBox)node.lookup("#messageFrameContainer");
        hbox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        HBox hbox2 = (HBox)node.lookup("#messageFrame");
        HBox.setMargin(hbox2, new Insets(0,300,0,10));
        messageZone.getChildren().add(node);
        messageScrollPane.applyCss();
        messageScrollPane.layout();
        messageScrollPane.setVvalue(1.0d);
        

        textFieldNewPseudo.setVisible(true);
        confirmNewpseudo.setVisible(true);
        textFieldNewPseudo.setDisable(false);
        confirmNewpseudo.setDisable(false);

    }

    public void SendButtonAction(ActionEvent event) throws IOException {
        /*Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);*/

        String message = textToSend.getText();
        afficherMessageEnvoye(message);
        textToSend.clear();
        System.out.println(ListContact.listContact);
        InetAddress inetAddress= ListContact.listContact.get(0).getUserAddress().getAddress();
        try {

            ThreadCom threadCom= (ThreadCom) NetworkManagerTCP.getInstance().getThreadManager().getThreadFromName(inetAddress.toString());
            System.out.println("on envoie "+message);
            threadCom.send(message);
        } catch (ThreadNotFoundException e) {
            throw new RuntimeException(e);
        }
/*
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

*/
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

    public synchronized void deleteAffUser(User user){
        int index = -1;
        String pseudo = user.getPseudo();
        for (Node child : listWindow.getChildren()) {
            Label label = (Label)child.lookup("#pseudoUser");
            if (label.getText().equals(pseudo)){
                index = listWindow.getChildren().indexOf(child);
            }

        }
        listWindow.getChildren().remove(index);
        ListContact.listContact.remove(user);
    }

    public synchronized void updatePseudo(String oldPseudo, String newPseudo){
        int index = -1;
        for (Node child : listWindow.getChildren()) {
            Label label = (Label)child.lookup("#pseudoUser");
            if (label.getText().equals(oldPseudo)){
                index = listWindow.getChildren().indexOf(child);
            }

        }
        HBox contactToUpdate = (HBox)listWindow.getChildren().get(index);
        Label pseudoToUpdate = (Label)contactToUpdate.lookup("#pseudoUser");
        pseudoToUpdate.setText(newPseudo);
    }

    public void deconnexionButtonClickAction(){
        /*Label label = (Label)listWindow.getChildren().get(0).lookup("#pseudoUser");
        System.out.println(label.getText());
        listWindow.getChildren().remove(0);
        Label label2 = (Label)listWindow.getChildren().get(1).lookup("#pseudoUser");
        label2.setText("ChangedPseudo");*/
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.DECONNECTION);
        //NetworkManagerUDP.getInstance().
        //ToDo Fermer socket + client


    }

    public void confirmNewPseudoEnteredAction(){
        confirmNewpseudo.setStyle("-fx-background-color: #424549");

    }
    public void confirmNewPseudoExitAction(){
        confirmNewpseudo.setStyle("-fx-background-color: #1e2124");

    }

    public void confirmNewPseudoClickAction(){
        textFieldNewPseudo.setVisible(false);
        confirmNewpseudo.setVisible(false);
        textFieldNewPseudo.setDisable(true);
        confirmNewpseudo.setDisable(true);
        myPseudo.setText(textFieldNewPseudo.getText());
        ListContact.selfUser.setPseudo(textFieldNewPseudo.getText());
        NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.CHANGEPSEUDO);
        textFieldNewPseudo.clear();



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
        //ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);//sert a rien car Un seul
        //thread1.start();
    }
}
