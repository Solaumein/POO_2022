package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;

public class MainScreenController {

    @FXML
    public Button SendButton;
    @FXML
    public TextArea textToSend;
    @FXML
    public VBox messageZone;

    @FXML
    public VBox listWindow;

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

    public void displayReceivedMessage(String message){
        Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.LEFT);
        messageZone.getChildren().add(messageBubble);
    }
}
