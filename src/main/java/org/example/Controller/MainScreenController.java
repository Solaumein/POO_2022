package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.User;
import org.example.UserAddress;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreenController {

    @FXML
    public Button SendButton;
    @FXML
    public TextArea textToSend;
    @FXML
    public VBox messageZone;

    @FXML
    public VBox listWindow;

    @FXML
    public Button sendButton;

    @FXML
    public HBox contactFrame;

    public void SendButtonAction(ActionEvent event) throws IOException {

        /*Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);*/

        String message = textToSend.getText();
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/MessageFrame.fxml"));
        messageLoader.load();
        Node node;
        node = (Node)messageLoader.getNamespace().get("messageFrame");
        Label messageToDisplay = (Label)node.lookup("#messageContent");
        messageToDisplay.setText(message);
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Envoyé à " + "HEURE");
        messageZone.getChildren().add(node);
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

    public void contactFrameEnteredAction(){
        contactFrame.setStyle("-fx-background-color: #424549;-fx-background-radius: 10px");}

    public void contactFrameExitAction(){
        contactFrame.setStyle("-fx-background-color:  #282b30;-fx-background-radius: 10px");}



    public void afficherNouveauUser(User user){
        listWindow.getChildren().add(user.getNode());
    }

    public void displayReceivedMessage(String message){
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.LEFT);
        messageZone.getChildren().add(messageBubble);

    }
}
