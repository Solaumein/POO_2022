package org.example;

public class ThreadTraitementPacket extends Thread{
    NetworkManagerUDP.Packet packet;
    NetworkManagerUDP managerUDP;
    ThreadTraitementPacket(NetworkManagerUDP.Packet packet, NetworkManagerUDP managerUDP){
        this.packet = packet;
        this.managerUDP = managerUDP;
        this.start();


    }


    @Override
    public void run(){
        switch (packet.state){
            case CONNECTION -> connexion();
            case CHANGEPSEUDO -> changementPseudo();
            case DECONNECTION -> deconnexion();
            default -> System.out.println(packet.state);
        }

    }

    private void deconnexion() {
        ListContact.removeContactByAddr(packet.addr);
    }

    private void changementPseudo() {
        if(ListContact.selfUser.getPseudo()== packet.pseudo || ListContact.isPseudoInList(packet.pseudo)){
            managerUDP.sendanswer(org.example.State.state.INVALIDPSEUDO, packet.addr);
        }
        else{
            ListContact.updatePseudoByAddr(packet.addr, packet.pseudo);
            managerUDP.sendanswer(org.example.State.state.VALIDPSEUDO, packet.addr);
        }
    }

    private void connexion() {
        if(ListContact.selfUser.getPseudo()== packet.pseudo || ListContact.isPseudoInList(packet.pseudo)){
            managerUDP.sendanswer(org.example.State.state.INVALIDPSEUDO, packet.addr);

        }
        else{
            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            ListContact.addContact(user);
            managerUDP.sendanswer(org.example.State.state.VALIDPSEUDO, packet.addr);
        }
    }
}
