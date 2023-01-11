package org.example.Network;

import org.example.User.ListContact;
import org.example.User.User;
import org.example.User.UserAddress;

import java.util.function.Consumer;

public class traitementPacket {
    private Packet packet;

    traitementPacket(Packet packet, Consumer<String> invalidPseudoCallback){
        this.packet = packet;
        switch (packet.state){
            case CONNECTION -> connexion();
            case CHANGEPSEUDO -> changementPseudo();
            case DECONNECTION -> deconnexion();
            case INVALIDPSEUDO -> invalidPseudoCallback.accept("toto");
            default -> System.out.println(packet.state);
        }
    }





    private void deconnexion() {
        ListContact.removeContactByAddr(packet.addr);
    }

    private void changementPseudo() {
        if(ListContact.selfUser.getPseudo().equals(packet.pseudo) || ListContact.isPseudoInList(packet.pseudo)){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.addr);
        }
        else{
            ListContact.updatePseudoByAddr(packet.addr, packet.pseudo);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.addr);
        }
    }

    private void connexion() {
        if(ListContact.selfUser.getPseudo()== packet.pseudo || ListContact.isPseudoInList(packet.pseudo)){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.addr);

        }
        else{
            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            ListContact.addContact(user);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.addr);
        }
    }
}
