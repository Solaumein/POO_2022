package org.example.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenConnectTCPThread extends Thread{
    private ServerSocket serverAccept;
    private int serverPort;

    private MessageReceivedHandler messageReceivedHandler;
    public ListenConnectTCPThread(int serverPort,MessageReceivedHandler messageReceivedHandler){
        this.serverPort=serverPort;

    }
    @Override
    public void run() {
        try {
            System.out.println("on lance la creation du servSock");
            this.serverAccept=new ServerSocket(serverPort);
            System.out.println("serveur sock creer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int i=0;
        while (true){
            i++;
            Socket socket= null;
            try {
                //Thread.sleep(100);
                socket = listening();
                NetworkManagerTCP.getInstance().getThreadManager().createThreadCommunication(socket,messageReceivedHandler);
                NetworkManagerTCP.getInstance().addSocket(socket);
            } catch (IOException e) {
                //e.printStackTrace();
                //throw new RuntimeException(e);//sera throw au moment de interrupt
           }
            //System.out.println("i="+i);
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    @Override
    public void interrupt() {//pk synchronised ne matrche pas ?
        try {
            this.serverAccept.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.interrupt();
    }

    private synchronized Socket listening() throws IOException {
        //System.out.println("on se met en ecoute...");//todo demander au prof pk ca print a l'infini

        Socket socket =  serverAccept.accept();
        InetAddress addr= socket.getInetAddress();
        System.out.println("on s'est co avec"+addr);
        return socket;
    }
}
