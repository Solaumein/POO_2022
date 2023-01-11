package org.example.Network;


import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ThreadComUDP extends Thread {
    private static int i =0;
    Consumer<String> invalidPseudoCallback;
    public ThreadComUDP(Consumer<String> invalidPseudoCallback){
        super("thread"+i);
        i++;
        this.invalidPseudoCallback=invalidPseudoCallback;
    }



    private void listentIntent(NetworkManagerUDP managerUDP, Consumer<String> invalidPseudoCallback) {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1024);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            Packet info = managerUDP.listenNotify(ds);
            //cas connexion
            traitementPacket traitementPacket = new traitementPacket(info,invalidPseudoCallback);
            //toDo answer(reponse) où reponse contient mon pseudo, mon port dédié pour tcp, state (ValidPseudo) or InvalidPseudo
        }
        //ds.close();
    }

//    private void notifyIntent() throws SocketException, UnknownHostException {
//        NetworkManagerUDP managerUDP = new NetworkManagerUDP();
//        //toDO pouvoir changer le state en arg
//        boolean notify = managerUDP.sendnotify(org.example.State.state.CONNECTION);
//
//    }

    @Override
    public void run() {
        //super.run();
            listentIntent(NetworkManagerUDP.getInstance(),invalidPseudoCallback);}



}
