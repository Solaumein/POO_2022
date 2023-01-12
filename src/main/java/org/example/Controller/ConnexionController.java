package org.example.Controller;

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

public class ConnexionController {

    @FXML
    public Button connectButton;
    @FXML
    public TextField textFieldPseudo;

    @FXML
    public Text textInvalidMsg;
    public void connectButtonAction(ActionEvent event){
        String pseudo=textFieldPseudo.getText();
        System.out.println("on lance un notify de notre pseudo : "+pseudo);
        ListContact.selfUser.setPseudo(pseudo);

        notifyConnectionUsers();//send the notify in broadcast
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
            Stage mainScreenStage=(Stage) connectButton.getScene().getWindow();
            String mainScreenTitle="Clavardage Entre Pote";
            GUIController guiController = new GUIController();

            MainScreenController mainScreenController = guiController.openAndGetController(mainScreenStage, mainScreenTitle);
            System.out.println("on init le handler");
            ContactEventHandler contactEventHandler= user -> mainScreenController.afficherNouveauUser(user);
            ListContact.addHandler(contactEventHandler);
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
