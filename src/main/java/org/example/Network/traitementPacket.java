package org.example.Network;

import org.example.User.ListContact;
import org.example.User.User;
import org.example.User.UserAddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class traitementPacket {
    private Packet packet;

    private String addrToStr(InetAddress addr){
        String str= addr.toString();
        str = str.substring(1);

        return str;}

    traitementPacket(Packet packet, Consumer<String> invalidPseudoCallback){
        this.packet = packet;
        boolean thisIsMyAdress = false;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {

                        System.out.println(ni.getName() + ": " + address.getHostAddress() + "      " +addrToStr(packet.addr));
                        if(address.getHostAddress().equals(addrToStr(packet.addr))){thisIsMyAdress=true;}
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if(!thisIsMyAdress){
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
    }

    private void validPseudoCallback() throws InterruptedException {
        if(!ListContact.isAdressInList(packet.addr)){
            System.out.println("ValidPseudoCallback");

            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            sleep(1000);
            ListContact.addContact(user);
            UserAddress userAddress=new UserAddress(packet.addr,packet.portcomtcp);
            NetworkManagerTCP.getInstance().connect(userAddress);
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
