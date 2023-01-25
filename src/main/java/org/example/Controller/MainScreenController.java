package org.example.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.Exception.ThreadNotFoundException;
import org.example.Message.LocalDbManager;
import org.example.Message.Message;
import org.example.Message.MessageHistory;
import org.example.Message.SQLiteHelper;
import org.example.Network.*;
import org.example.User.ListContact;
import javafx.scene.text.Text;
import org.example.User.User;
import org.example.User.UserAddress;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.function.Consumer;

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
        @FXML
        public Label pseudoSelectedContact;
        private User selectedUser;
        public void initialize() {
            NetworkManagerTCP.getInstance().setMessageReceivedHandler(messageReceivedHandlerInit);
            NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());
            SQLiteHelper.getInstance().createTableMessage();//initalise la bdd

            LocalDbManager.getInstance().updateSavedMessages();//prends les msg de la bdd

        }
        private final MessageReceivedHandler messageReceivedHandlerInit=  new MessageReceivedHandler() {
            @Override
            public void newMessageArrivedFromAddr(String messageInString, InetAddress address) {
                Message message=new Message(messageInString,true,address);
                LocalDbManager.getInstance().addMessage(message);
                System.out.println("on est dans runlater de reception");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(selectedUser!=null && selectedUser.getUserAddress().getAddress().equals(address))displayReceivedMessage(messageInString);
                        System.out.println("message rekkkkku "+message+ " par l'address "+address);

                    }
                });
            }
        };
                /*(messageInString, address) -> {
            Message message=new Message(messageInString,true,address);
            LocalDbManager.getInstance().addMessage(message);
            System.out.println("message rekkkkku "+message+ " par l'address "+address);*/
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
        private void afficherMessageEnvoye(String message) throws IOException {//todo mettre fonction dans GUI controller
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
            messageScrollPane.applyCss();
            messageScrollPane.layout();
            messageScrollPane.setVvalue(1.0d);
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

            textFieldNewPseudo.setPromptText("New pseudo here");
            pseudoSelectedContact.setPrefWidth(0);
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
            if(selectedUser!=null) {
                String messageInString = textToSend.getText();
                afficherMessageEnvoye(messageInString);
                textToSend.clear();
                System.out.println(ListContact.listContact);
                InetAddress inetAddress = selectedUser.getUserAddress().getAddress();
                try {
                    SendMessageTCPThread threadCom = (SendMessageTCPThread) ThreadManager.getInstance().getThreadSendFromName(inetAddress.toString());
                    System.out.println("on envoie " + messageInString);
                    threadCom.send(messageInString);
                } catch (ThreadNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Message message=new Message(messageInString,false,inetAddress);
                LocalDbManager.getInstance().addMessage(message);
            }
        /*ArrayList<String> li = new ArrayList<>();
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
        }*/
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
    /*MessageReceivedHandler messageReceivedHandler=new MessageReceivedHandler() {
            @Override
            public void newMessageArrivedFromAddr(String message,InetAddress address) {
                System.out.println("on est dans handler");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("on est dans runlater de reception");
                        if(selectedUser.getUserAddress().getAddress().equals(address))displayReceivedMessage(message);
                    }
                });
            }
        };*/
        public void contactFrameClickAction(){
            System.out.println("contcact");
           // NetworkManagerTCP.getInstance().setMessageReceivedHandler(messageReceivedHandler);
            System.out.println(selectedUser);

            messageZone.getChildren().clear();
        }
        public void afficherNouveauUser(User user){
            //listWindow.getChildren().add(user.getNode());
            Node userNode = user.getNode();
            userNode.setOnMouseClicked(
                    event -> {
                        selectedUser = user;
                        contactFrameClickAction();
                        //System.out.println("la bdd ");
                        //SQLiteHelper.getInstance().printAll();
                        MessageHistory messageHistoryOfSelectedUser= LocalDbManager.getInstance().getMessageHistory(selectedUser.getUserAddress().getAddress());
                        System.out.println("on a recup les message de "+selectedUser+" \nmessage "+ messageHistoryOfSelectedUser);
                        for (Message message : messageHistoryOfSelectedUser.getListMessage()) {
                            //System.out.println("on a un message sauvegarder "+message);
                            if(message.getRecu()){
                                displayReceivedMessage(message.getContenu());
                            }else {
                                try {
                                    afficherMessageEnvoye(message.getContenu());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        pseudoSelectedContact.setText(user.getPseudo());
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
            System.exit(0);
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
            if(!textFieldNewPseudo.getText().contains(",")){
                textFieldNewPseudo.setVisible(false);
                confirmNewpseudo.setVisible(false);
                textFieldNewPseudo.setDisable(true);
                confirmNewpseudo.setDisable(true);
                pseudoSelectedContact.setPrefWidth(850);
                myPseudo.setText(textFieldNewPseudo.getText());
                ListContact.selfUser.setPseudo(textFieldNewPseudo.getText());
                NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
                networkManagerUDP.sendNotify(State.state.CHANGEPSEUDO);
                textFieldNewPseudo.clear();
            }
            else {textFieldNewPseudo.clear(); textFieldNewPseudo.setPromptText("Pas de virgule svp");}
        }
        private void notifyDeconection() {
            NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
            networkManagerUDP.sendNotify(State.state.DECONNECTION);
        }
        public void displayReceivedMessage(String message){
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/MessageFrame.fxml"));
            try {
                messageLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Node node;
            node = (Node)messageLoader.getNamespace().get("messageFrameContainer");
            Label messageToDisplay = (Label)node.lookup("#messageContent");
            messageToDisplay.setText(message);
            Label messageTime = (Label)node.lookup("#messageTime");
            messageTime.setText("Reçu à " + "HEURE");//todo mettre date
            HBox hbox =(HBox)node.lookup("#messageFrameContainer");
            hbox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(hbox, Priority.ALWAYS);
            HBox hbox2 = (HBox)node.lookup("#messageFrame");
            HBox.setMargin(hbox2, new Insets(0,300,0,10));
            messageZone.getChildren().add(node);
            messageScrollPane.applyCss();
            messageScrollPane.layout();
            messageScrollPane.setVvalue(1.0d);
        }
        private void notifyChangePseudo() {
            NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
            networkManagerUDP.sendNotify(State.state.CHANGEPSEUDO);
            //ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);//sert a rien car Un seul
            //thread1.start();
        }
    }