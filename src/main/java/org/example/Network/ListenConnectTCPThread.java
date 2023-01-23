package org.example.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ListenConnectTCPThread extends Thread{
    private ServerSocket serverAccept;
    private final int serverPort;
    private final Consumer<Socket> connectionCallback;

    public int getServerPort() {
        return serverPort;
    }

    public ListenConnectTCPThread(int serverPort,Consumer<Socket> connectionCallback){
        this.serverPort=serverPort;
        this.connectionCallback=connectionCallback;
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
        while (true){
            Socket socket;
            try {
                Thread.sleep(100);
                socket = listening();
                connectionCallback.accept(socket);
            } catch (SocketException e) {//cette erreur survient lorsque le ServerSocket est fermé mais que le thread continue (arrive qd reset de NetworkTCPManager)
                this.interrupt();
           } catch (InterruptedException e) {
                //throw new RuntimeException(e);//sera throw au moment de interrupt
            } catch (IOException e) {//erreur d'ecriture il faut arreter l'application
                throw new RuntimeException(e);
            }
        }
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
        //System.out.println("on se met en ecoute...");

        Socket socket =  serverAccept.accept();
        InetAddress addr= socket.getInetAddress();
        System.out.println("on s'est co avec"+addr);
        return socket;
    }
}
