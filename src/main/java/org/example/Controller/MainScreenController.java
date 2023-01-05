package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MainScreenController {

    @FXML
    public Button SendButton;
    @FXML
    public TextArea textToSend;
    @FXML
    public VBox messageZone;

    public void SendButtonAction(ActionEvent event){

        String message = textToSend.getText();
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);
        textToSend.clear();
    }

    public void displayReceivedMessage(String message){
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.LEFT);
        messageZone.getChildren().add(messageBubble);
    }
}
