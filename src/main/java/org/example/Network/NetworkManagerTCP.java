package org.example.Network;

import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.User.User;
import org.example.User.UserAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class NetworkManagerTCP extends Thread{
    private final ArrayList<Socket> listSocket =new ArrayList<>();

    private final ThreadManager threadManager =ThreadManager.getInstance();
    private static final NetworkManagerTCP instance = new NetworkManagerTCP();

    private static MessageReceivedHandler messageReceivedHandler=null;

    public synchronized static MessageReceivedHandler getMessageReceivedHandler() {
        return messageReceivedHandler;
    }

    public synchronized ThreadManager getThreadManager() {
        return threadManager;
    }
    public static synchronized void setMessageReceivedHandler(MessageReceivedHandler messageReceivedHandler){
        NetworkManagerTCP.messageReceivedHandler=messageReceivedHandler;
    }

    public static NetworkManagerTCP getInstance() {
        if(NetworkManagerTCP.messageReceivedHandler==null){
            throw new RuntimeException("pas de messageHandler Initialisé");
        }
        return instance;
    }
    private NetworkManagerTCP(){}
    private ListenConnectTCPThread listenConnectTCPThread;
    private final Consumer<Socket> connectionCallback= socket -> {
        ThreadManager.getInstance().createThreadCommunication(socket,NetworkManagerTCP.getMessageReceivedHandler());
        NetworkManagerTCP.getInstance().addSocket(socket);
    };
    public synchronized void launchListenThread(int port){
        listenConnectTCPThread=new ListenConnectTCPThread(port,connectionCallback);
        listenConnectTCPThread.start();
    }

    public synchronized void stopListenThread(){
        if (listenConnectTCPThread!=null)listenConnectTCPThread.interrupt();
    }
    public synchronized int getPortListeningConnection(){
        return this.listenConnectTCPThread.getServerPort();
    }
    public synchronized void stopAllSocket(){
        System.out.println("on deco de tt le monde");
        ArrayList<Socket> listSock= new ArrayList<>(getListSocket());
        for (Socket socket : listSock) {
            if(!closeSock(socket.getInetAddress())){//erreur de close
                //todo peut etre faire action
                System.out.println("on a pas pu close le sock "+socket);
            }
        }
    }

    public static int getPortLibre(){
        for (int port=1024; port<=65353; port++) {
            try (ServerSocket ignored1 = new ServerSocket(port)) {
                return port;
            } catch (IOException ignored) {}
        }
        return -1;//aucun port trouvé
    }


    public synchronized boolean connect(UserAddress userAddress){
        System.out.println("on veut se co au user "+userAddress);
        try {
            //System.out.println("on demande a user de pseudo "+u.getPseudo());
            Socket socket =  new Socket(userAddress.getAddress(), userAddress.getPort());
            System.out.println("il a accept on ecoute sur le port "+socket.getPort());
            addSocket(socket);
            threadManager.createThreadCommunication(socket,NetworkManagerTCP.messageReceivedHandler);
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("co pas marche");
            return false;
        }
    }
    public void killCommunication(Communication communication) throws ThreadNotFoundException {

        threadManager.killThread(communication.getListenMessageTCPThread());
        threadManager.killThread(communication.getSendMessageTCPThread());
    }

    public synchronized boolean closeSock(InetAddress address){
        System.out.println("on se deco de addr "+address);
        try{
            SendMessageTCPThread threadCom= (SendMessageTCPThread) threadManager.getThreadSendFromName(address.toString());
            Socket sock=getSocketFromIP(address);
            threadCom.getSockCom().close();
            listSocket.remove(sock);
            return true;
        } catch (ThreadNotFoundException | IOException | SocketComNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public synchronized void reset(){
        this.stopListenThread();
        this.stopAllSocket();
        NetworkManagerTCP.setMessageReceivedHandler(null);

    }

    public synchronized boolean closeSock(User u){
        System.out.println("on se deco au user "+u);
        InetAddress address=u.getUserAddress().getAddress();
        try {
            SendMessageTCPThread threadCom= (SendMessageTCPThread) threadManager.getThreadSendFromName(address.toString());
            Socket sock=getSocketFromIP(address);
            threadManager.killThread(threadCom);
            threadCom.getSockCom().close();
            listSocket.remove(sock);
            return true;
        } catch (ThreadNotFoundException | IOException | SocketComNotFoundException e) {
            return false;
        }
    }

    public synchronized ArrayList<Socket> getListSocket() {
        return listSocket;
    }

    public Socket getSocketFromIP(InetAddress ip) throws SocketComNotFoundException{//demander au prof si on peut mettre 1 synchro ou 2
        for (Socket socket : getListSocket()) {
            if (ip.equals(socket.getInetAddress())){
                return socket;
            }
        }
        throw new SocketComNotFoundException();
    }
//    boolean send(String s){
//        System.out.println(this + ": on va lancer le write du message "+s);
//        PrintWriter out = null;
//        try {
//            out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(s);
//            System.out.println(this + ": on a lancer un message "+s);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//    String receive() {
//        try {
//            System.out.println(this + ": on va lancer le read");
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String res= in.readLine();
//            System.out.println(this + ": on a recu un message "+res);
//            return res;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public synchronized void addSocket(Socket s){
        System.out.println("on rajoute le sock "+s);
        this.listSocket.add(s);
    }

    @Override
    public synchronized String toString() {
        return listSocket.toString();
    }

}
