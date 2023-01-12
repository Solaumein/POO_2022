package org.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import org.example.GUI.GUIController;
import org.example.Network.NetworkManagerUDP;
import org.example.Network.ThreadComUDP;
import org.example.User.ContactEventHandler;
import org.example.User.ListContact;
import org.example.Network.State;
import org.example.User.User;

import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class ConnexionController {

    @FXML
    public Button connectButton;
    @FXML
    public TextField textFieldPseudo;

    @FXML
    public Text textInvalidMsg;


    public void connectButtonAction(ActionEvent event) throws InterruptedException {
        String pseudo=textFieldPseudo.getText();
        System.out.println("on lance un notify de notre pseudo : "+pseudo);
        ListContact.selfUser.setPseudo(pseudo);

        notifyConnectionUsers();//send the notify in broadcast
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
            Stage mainScreenStage=(Stage) connectButton.getScene().getWindow();
            String mainScreenTitle="Clavardage Entre Pote";
            GUIController guiController = new GUIController();

            MainScreenController mainScreenController = guiController.openAndGetController(mainScreenStage, mainScreenTitle);
            mainScreenController.myPseudo.setText(ListContact.selfUser.getPseudo());
            System.out.println("on init le handler");

            System.out.println("on init le handler");
            ContactEventHandler contactEventHandler= user -> Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("On entre dans le runLater");
                    mainScreenController.afficherNouveauUser(user);
                    /*if(ListContact.listContact!=null){
                        for (User user : ListContact.listContact) {
                            //User usersave = user;
                            ListContact.listContact.remove(user);
                            ListContact.listContact.add(user);
                        }}*/
                    System.out.println("Initiated");
                }
            });

            ListContact.addHandler(contactEventHandler);
            ListContact.addContact(new User(null,"test"));



        }else{
            alertInvalid(ListContact.selfUser.getPseudo());
        }



    }

    private void alertInvalid(String pseudo) {

        //todo popup ou pas
        //Popup popupInvalidPseudo=GUIController.getPopupInvalidPseudo(pseudo);
        //popupInvalidPseudo.show((Stage) connectButton.getScene().getWindow());
        textInvalidMsg.setText("Pseudo already taken !");
    }

    boolean pseudoLibre=true;
    Consumer<String> invalidPseudoCallback= s -> pseudoLibre=false;
    private void notifyConnectionUsers() {
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.CONNECTION);
        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);
        thread1.start();
    }


}
