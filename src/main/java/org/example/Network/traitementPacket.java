package org.example.Network;

import org.example.User.ListContact;
import org.example.User.User;
import org.example.User.UserAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.function.Consumer;


public class traitementPacket {
    private final Packet packet;
    private String addrToStr(InetAddress addr){
        String str= addr.toString();
        str = str.substring(1);

        return str;
    }

    public traitementPacket(Packet packet, Consumer<String> invalidPseudoCallback,Consumer<Packet> validPseudoCallBack,Consumer<Packet> decoCallback){
        this.packet = packet;
        boolean thisIsMyAdress = false;
        try {//todo faire fonction qui fait le test de thisIsMyAddress
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && !address.getHostAddress().contains(":")) {

                        System.out.println(ni.getName() + ": " + address.getHostAddress() + "      " +addrToStr(packet.getAddr()));
                        if(address.getHostAddress().equals(addrToStr(packet.getAddr()))){thisIsMyAdress=true;}
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if(!thisIsMyAdress){
            switch (packet.getState()){
                case CONNECTION : connexion(); break;
                case VALIDPSEUDO : validPseudoCallBack.accept(packet); break;
                case CHANGEPSEUDO : changementPseudo(); break;
                case DECONNECTION : decoCallback.accept(packet); break;
                case INVALIDPSEUDO : invalidPseudoCallback.accept(""); break;
                default : System.out.println(packet.getState()); break;
            }
        }
    }

    /*private void validPseudoCallback()  {
        if(!ListContact.isAdressInList(packet.addr)){
            System.out.println("ValidPseudoCallback");

            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                //le thread se fait kill donc on ignore
            }
            ListContact.addContact(user);
            UserAddress userAddress=new UserAddress(packet.addr,packet.portcomtcp);
            NetworkManagerTCP.getInstance().connect(userAddress);
            System.out.println("on ajoute le user "+packet.pseudo);
        }
    }*/



    private void changementPseudo() {
        if(ListContact.selfUser.getPseudo().equals(packet.getPseudo()) || ListContact.isPseudoInList(packet.getPseudo())){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.getAddr());
        }else{
            int index = ListContact.searchByAddress(packet.getAddr());
            String oldPseudo = ListContact.listContact.get(index).getPseudo();
            ListContact.updatePseudoByAddr(packet.getAddr(), packet.getPseudo(), oldPseudo);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.getAddr());
        }
    }
    private void connexion() {
        if(ListContact.selfUser.getPseudo().equals(packet.getPseudo()) || ListContact.isPseudoInList(packet.getPseudo())){
            NetworkManagerUDP.getInstance().sendAnswer(State.state.INVALIDPSEUDO, packet.getAddr());
        }else{
            UserAddress addr = new UserAddress(packet.getAddr(), packet.getPortcomtcp());
            User user = new User(addr, packet.getPseudo());
            ListContact.addContact(user);
            NetworkManagerUDP.getInstance().sendAnswer(State.state.VALIDPSEUDO, packet.getAddr());
        }
    }
}
