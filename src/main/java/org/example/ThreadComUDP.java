package org.example;


import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ThreadComUDP extends Thread {
    static int i =0;
    NetworkManagerUDP managerUDP;

    ThreadComUDP(NetworkManagerUDP managerUDP){
        super("thread"+i);
        i++;
        this.managerUDP = managerUDP;
        this.start();
    }


    private void listentIntent(NetworkManagerUDP managerUDP) {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1024);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            NetworkManagerUDP.Packet info = managerUDP.listenNotify(ds);
            //cas connexion
            ThreadTraitementPacket traitement = new ThreadTraitementPacket(info, managerUDP);

            //toDo answer(reponse) où reponse contient mon pseudo, mon port dédié pour tcp, state (ValidPseudo) or InvalidPseudo
        }
        //ds.close();
    }

    private void notifyIntent() throws SocketException, UnknownHostException {
        NetworkManagerUDP managerUDP = new NetworkManagerUDP();
        //toDO pouvoir changer le state en arg
        boolean notify = managerUDP.sendnotify(org.example.State.state.CONNECTION);

    }

    @Override
    public void run() {
        //super.run();
            listentIntent(managerUDP);}



}
