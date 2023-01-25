package org.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.Exception.ThreadNotFoundException;
import org.example.Message.LocalDbManager;
import org.example.Message.Message;
import org.example.Message.MessageHistory;
import org.example.Message.SQLiteHelper;
import org.example.Network.*;
import org.example.User.ListContact;

import org.example.User.User;

import java.io.IOException;
import java.net.InetAddress;

import static java.lang.Thread.sleep;

public class MainScreenController{
    @FXML
    public Button changePseudoButton;
    @FXML
    public Button deconnectButton;

    @FXML
    public TextField textFieldNewPseudo;
    @FXML
    public VBox listWindow;
    //@FXML
    //public Button SendButton;
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
    @FXML
    public Button sendButton;
    @FXML
    public HBox contactFrame;

    private User selectedUser;
    public boolean pseudoLibre=true;
    private final MessageReceivedHandler messageReceivedHandlerInit= (messageInString, address) -> {
        Message message=new Message(messageInString,true,address);
        LocalDbManager.getInstance().addMessage(message);
        System.out.println("message recu "+message+ " par l'address "+address);
    };
    public void initialize() {
        NetworkManagerTCP.setMessageReceivedHandler(messageReceivedHandlerInit);
        NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());
        SQLiteHelper.getInstance().createTableMessage();
        LocalDbManager.getInstance().updateSavedMessages();
    }

    /*public void deconnectButtonAction(ActionEvent event) {
        notifyDeconection();//send a notify of deconnection

        Stage mainStage = (Stage) deconnectButton.getScene().getWindow();
        String pathMainScreenFXML="src/main/resources/Login.fxml";
        Stage decoStage= GUIController.openNewWindow(mainStage,pathMainScreenFXML,"Connexion");
        //todo mettre le textfield de la co a son ancien pseudo
    }*///remplacé par deconnexionButtonClickAction()
/*
    public void changePseudoButtonAction(ActionEvent event) {
        if(!textFieldNewPseudo.isVisible())textFieldNewPseudo.setVisible(true);
        else{
            String newPseudo=textFieldNewPseudo.getText();
            notifyChangePseudo();//send a "notify" of deconnection
            //demarrage de l'attente de réponse
            waitForPseudoLibre(newPseudo);
        }
    }
*/
    private void alertInvalid(String pseudo) {
        textFieldNewPseudo.clear(); textFieldNewPseudo.setPromptText("Pseudo "+pseudo+" deja pris choisissez en un autre");//todo a tester
    }

    private void afficherMessageEnvoye(Message message) {//todo mettre fonction dans GUI controller
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/MessageFrameSent.fxml"));
        try {
            messageLoader.load();
        } catch (IOException e) {
            //popup de message pas pu être loader
        }
        Node node;
        node = (Node)messageLoader.getNamespace().get("messageFrameContainer");
        Label messageToDisplay = (Label)node.lookup("#messageContent");
        messageToDisplay.setText(message.getContenu());
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Envoyé à " + message.getDateMessage());
        messageZone.getChildren().add(node);
        messageScrollPane.applyCss();
        messageScrollPane.layout();
        messageScrollPane.setVvalue(1.0d);

    }

    public void changePseudoButtonClick() {
        textFieldNewPseudo.setPromptText("New pseudo here");
        pseudoSelectedContact.setPrefWidth(0);
        textFieldNewPseudo.setVisible(true);
        confirmNewpseudo.setVisible(true);
        textFieldNewPseudo.setDisable(false);
        confirmNewpseudo.setDisable(false);

    }

    public void SendButtonAction() throws IOException {
        /*Text messageBubble = new Text(message);
        messageBubble.setFont(Font.font(20));
        messageBubble.setTextAlignment(TextAlignment.RIGHT);
        messageZone.getChildren().add(messageBubble);*/
        if(selectedUser!=null) {

            String messageInString = textToSend.getText();

            System.out.println(ListContact.listContact);
            InetAddress inetAddress = selectedUser.getUserAddress().getAddress();
            try {

                SendMessageTCPThread threadCom = (SendMessageTCPThread) ThreadManager.getInstance().getThreadSendFromName(inetAddress.toString());//todo ligne changée avant test fonctionnel
                System.out.println("on envoie " + messageInString);
                if(!threadCom.send(messageInString)){//le message s'est pas send
                    throw new RuntimeException("message pas envoyé : "+messageInString);
                }

            } catch (ThreadNotFoundException e) {
                throw new RuntimeException(e);
            }
            //pour remetre la zone de texte d'envoie à zero
            textToSend.clear();

            //une fois que message a ete envoyer on peut sauvegarder le message envoyé
            Message message=new Message(messageInString,false,inetAddress);
            LocalDbManager.getInstance().addMessage(message);

            //affiche le message envoyé
            afficherMessageEnvoye(message);
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

            NetworkManagerTCP.setMessageReceivedHandler((messageStr, address) -> {
                //on le montre dans la boite de dialogue
                Platform.runLater(() -> {
                    System.out.println("on est dans MessageReceivedHandler");
                    //on save le message
                    Message message=new Message(messageStr,false,address);
                    LocalDbManager.getInstance().addMessage(message);
                    System.out.println("on est dans runlater de reception de msg");
                    if(selectedUser.getUserAddress().getAddress().equals(address))displayReceivedMessage(message);
                });
            });
        System.out.println(selectedUser);
    }

    public void afficherNouveauUser(User user){
        //listWindow.getChildren().add(user.getNode());
        Node userNode = user.getNode();
        userNode.setOnMouseClicked(
                event -> {
                    selectedUser = user;//todo on pourrait pas tout mettre dans le contactFrameClickAction ?
                    contactFrameClickAction();
                    //System.out.println("la bdd ");
                    //SQLiteHelper.getInstance().printAll();
                    MessageHistory messageHistoryOfSelectedUser= LocalDbManager.getInstance().getMessageHistory(selectedUser.getUserAddress().getAddress());
                    System.out.println("on a recup les messages de "+selectedUser+" \nmessage "+ messageHistoryOfSelectedUser);
                    loadHistoryOfMessage(messageHistoryOfSelectedUser);
                    pseudoSelectedContact.setText(user.getPseudo());
                }
        );
        listWindow.getChildren().add(userNode);
    }

    private void loadHistoryOfMessage(MessageHistory messageHistoryOfSelectedUser) {
        for (Message message : messageHistoryOfSelectedUser.getListMessage()) {
            System.out.println("on a un message sauvegardé "+message);
            if(message.isRecu()){
                displayReceivedMessage(message);
            }else {
                    afficherMessageEnvoye(message);

            }
        }
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
        notifyDeconection();
        NetworkManagerTCP.getInstance().reset();
        ThreadManager.getInstance().reset();
        System.exit(0);
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
            String newPseudo=textFieldNewPseudo.getText();
            myPseudo.setText(newPseudo);
            ListContact.selfUser.setPseudo(textFieldNewPseudo.getText());
            NetworkManagerUDP.getInstance().sendNotify(State.state.CHANGEPSEUDO);
            waitForPseudoLibre(newPseudo);
            textFieldNewPseudo.clear();
        }
        else {
            textFieldNewPseudo.clear();
            textFieldNewPseudo.setPromptText("Pas de virgule svp");
        }
    }

    private void waitForPseudoLibre(String newPseudo) {
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

    private void notifyDeconection() {
        NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.DECONNECTION);
    }
    public void displayReceivedMessage(Message message){
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
        messageToDisplay.setText(message.getContenu());
        Label messageTime = (Label)node.lookup("#messageTime");
        messageTime.setText("Reçu à " + message.getDateMessage());//todo mettre date
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

}
