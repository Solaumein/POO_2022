package org.example.Network;


import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ThreadComUDP extends Thread {
    private static int i =0;
    private final Consumer<String> invalidPseudoCallback;
    private final Consumer<Packet> validPseudoCallback;
    public ThreadComUDP(Consumer<String> invalidPseudoCallback,Consumer<Packet> validPseudoCallback){
        super("thread"+i);
        i++;
        this.invalidPseudoCallback=invalidPseudoCallback;
        this.validPseudoCallback=validPseudoCallback;
    }



    private void listentIntent() {

        DatagramSocket ds;
        try {
            ds = new DatagramSocket(NetworkManagerUDP.portUDP);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            Packet info = NetworkManagerUDP.getInstance().listenNotify(ds);
            //cas connexion
            new traitementPacket(info,invalidPseudoCallback,validPseudoCallback);
            //toDo answer(reponse) où reponse contient mon pseudo, mon port dédié pour tcp, state (ValidPseudo) or InvalidPseudo
        }
        //ds.close();
    }

    @Override
    public void run() { listentIntent();}
}
