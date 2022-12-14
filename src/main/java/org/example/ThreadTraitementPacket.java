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
        }

    }

    private void deconnexion() {
    }

    private void changementPseudo() {
        
    }

    private void connexion() {
        if(ListContact.selfUser.getPseudo()== packet.pseudo){
            managerUDP.sendanswer(org.example.State.state.INVALIDPSEUDO, packet.addr);

        }
        else if(ListContact.selfUser.getPseudo()!= packet.pseudo){
            UserAddress addr = new UserAddress(packet.addr, packet.portcomtcp);
            User user = new User(addr, packet.pseudo);
            managerUDP.sendanswer(org.example.State.state.VALIDPSEUDO, packet.addr);
        }
    }
}
