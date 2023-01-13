package org.example.Network;

import org.example.User.ListContact;
import org.example.User.User;
import org.example.User.UserAddress;

import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class traitementPacket {
    private Packet packet;

    traitementPacket(Packet packet, Consumer<String> invalidPseudoCallback){
        this.packet = packet;
        switch (packet.state){
            case CONNECTION -> connexion();
            case VALIDPSEUDO -> {
                try {
                    validPseudoCallback();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            case CHANGEPSEUDO -> changementPseudo();
            case DECONNECTION -> deconnexion();
            case INVALIDPSEUDO -> invalidPseudoCallback.accept("toto");
            default -> System.out.println(packet.state);
        }
    }

    private void validPseudoCallback() throws InterruptedException {
        if(!ListContact.isAdressInList(packet.addr)){
            System.out.println("ValidPseudoCallback");

            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            sleep(1000);
            ListContact.addContact(user);

            System.out.println("on ajoute le user "+packet.pseudo);
            //ok, merci le sleep
        }
    }


    private void deconnexion() {
        ListContact.removeContactByAddr(packet.addr);
    }

    private void changementPseudo() {
        if(ListContact.selfUser.getPseudo().equals(packet.pseudo) || ListContact.isPseudoInList(packet.pseudo)){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.addr);
        }else{
            int index = ListContact.searchByAddress(packet.addr);
            String oldPseudo = ListContact.listContact.get(index).getPseudo();
            ListContact.updatePseudoByAddr(packet.addr, packet.pseudo, oldPseudo);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.addr);
        }
    }
//todo passer en paquet port TCP
    private void connexion() {
        if(ListContact.selfUser.getPseudo().equals(packet.pseudo) || ListContact.isPseudoInList(packet.pseudo)){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.addr);
        }else{
            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            ListContact.addContact(user);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.addr);
        }
    }
}
