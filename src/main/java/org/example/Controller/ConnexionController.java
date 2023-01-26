package org.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.example.Message.LocalDbManager;
import org.example.Message.Message;
import org.example.Network.*;
import org.example.User.*;

import java.net.InetAddress;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class ConnexionController {

    @FXML
    public Button connectButton;
    @FXML
    public TextField textFieldPseudo;

    @FXML
    public Text textInvalidMsg;

    boolean pseudoLibre=true;
    private final Consumer<String> invalidPseudoCallback= s -> pseudoLibre=false;
    private final Consumer<Packet> validPseudoCallback= packet -> {
        if(!ListContact.isAdressInList(packet.getAddr())){
            System.out.println("ValidPseudoCallback");

            UserAddress addr = new UserAddress(packet.getAddr(), packet.getPortcomtcp());
            User user = new User(addr, packet.getPseudo());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                //le thread se fait kill donc on ignore
            }
            ListContact.addContact(user);
            UserAddress userAddress=new UserAddress(packet.getAddr(),packet.getPortcomtcp());
            NetworkManagerTCP.getInstance().connect(userAddress);
            System.out.println("on ajoute le user "+packet.getPseudo());
        }
    };

    private boolean threadStarted =false;


    public void connectButtonAction() {
        pseudoLibre=true;
        String pseudo=textFieldPseudo.getText();
        if(!textFieldPseudo.getText().contains(",")) {
            System.out.println("on lance un notify de notre pseudo : " + pseudo);
            ListContact.selfUser.setPseudo(pseudo);

            notifyConnectionUsers();//send the "notify" in broadcast
            int temps = 0;
            while (temps < 10 && pseudoLibre) {
                temps++;
                try {//toute les 10ms ont test
                    sleep(10);//todo attendre réponse du premier avec future ou promesse
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (pseudoLibre) {
                Stage mainScreenStage = (Stage) connectButton.getScene().getWindow();
                mainScreenStage.setOnCloseRequest(evt -> {
                    NetworkManagerUDP networkManagerUDP = NetworkManagerUDP.getInstance();
                    networkManagerUDP.sendNotify(State.state.DECONNECTION);
                    System.exit(0);
                });
                String mainScreenTitle = "Clavardage Entre Pote";
                GUIController guiController = new GUIController();

                MainScreenController mainScreenController = guiController.openAndGetController(mainScreenStage, mainScreenTitle);
                mainScreenController.myPseudo.setText(ListContact.selfUser.getPseudo());
                System.out.println("on init le handler");

                ContactEventHandler contactEventHandler = user -> Platform.runLater(() -> {
                    System.out.println("On entre dans le runLater");
                    mainScreenController.afficherNouveauUser(user);
                /*if(ListContact.listContact!=null){
                    for (User user : ListContact.listContact) {
                        //User usersave = user;
                        ListContact.listContact.remove(user);
                        ListContact.listContact.add(user);
                    }}*/


                    System.out.println("Initiated contact of "+user);
                });

                ContactEventHandlerDeco contactEventHandlerDeco = user -> Platform.runLater(() -> {
                    System.out.println("On entre dans le runLater");
                    mainScreenController.deleteAffUser(user);

                    System.out.println("Initiated");
                });

                ContactEventHandlerUpdatePseudo contactEventHandlerUpdate = (oldPseudo, newPseudo) -> Platform.runLater(() -> {
                    System.out.println("On entre dans le runLater");
                    mainScreenController.updatePseudo(oldPseudo, newPseudo);

                    System.out.println("Initiated");
                });

                NetworkManagerTCP.getInstance().setMessageReceivedHandler(new MessageReceivedHandler() {
                    @Override
                    public void newMessageArrivedFromAddr(String messageInString, InetAddress address) {
                        System.out.println("on va rentrer dans le runlater de plateforme");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Message message=new Message(messageInString,true,address);
                                LocalDbManager.getInstance().addMessage(message);
                                System.out.println("on est dans runlater de reception handler");
                                if(ListContact.getSelectedContact()!=null && ListContact.getSelectedContact().getUserAddress().getAddress().equals(address)){
                                    System.out.println("le message s'affiche car "+ListContact.getSelectedContact()+" est le meme que "+address+" message "+messageInString);
                                    mainScreenController.displayReceivedMessage(message);
                                }
                            }
                        });
                    }
                });



                ListContact.addHandler(contactEventHandler);
                ListContact.addHandlerDeco(contactEventHandlerDeco);
                ListContact.addHandlerUpdatePseudo(contactEventHandlerUpdate);
                //ListContact.addContact(new User(null,"test"));


            } else {
                alertInvalid();
            }
        }
        else {
            textInvalidMsg.setText("Pas de virgule svp!");
        }
    }

    private void alertInvalid() {

        //todo popup ou pas
        //Popup popupInvalidPseudo=GUIController.getPopupInvalidPseudo(pseudo);
        //popupInvalidPseudo.show((Stage) connectButton.getScene().getWindow());
        textInvalidMsg.setText("Pseudo already taken !");
    }


    private void notifyConnectionUsers() {
        System.out.print("threaddemarre : "+ this.threadStarted + "   gjoifghgesfrihoier");
        if(!this.threadStarted){
            ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback,validPseudoCallback);
            thread1.start();
            ThreadManager.getInstance().addThread(thread1);
            this.threadStarted =true;
        }
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.CONNECTION);
    }


}
