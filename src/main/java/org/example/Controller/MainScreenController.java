package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.GUI.GUIController;
import org.example.ListContact;
import org.example.NetworkManagerUDP;
import org.example.State;
import org.example.ThreadComUDP;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

public class MainScreenController {



    @FXML
    public Button changePseudoButton;
    @FXML
    public Button deconnectButton;

    @FXML
    public TextField textFieldNewPseudo;



    public void deconnectButtonAction(ActionEvent event) {
        notifyDeconection();//send a notify of deconnection

        Stage mainStage = (Stage) deconnectButton.getScene().getWindow();
        String pathMainScreenFXML="src/main/java/org/example/GUI/Login.fxml";
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

    private void notifyDeconection() {
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.DECONNECTION);
    }


    private void notifyChangePseudo() {
        NetworkManagerUDP networkManagerUDP=NetworkManagerUDP.getInstance();
        networkManagerUDP.sendNotify(State.state.CHANGEPSEUDO);
        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);
        thread1.start();
    }
}
