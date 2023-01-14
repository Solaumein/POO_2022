package org.example.Network;

import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.User.ListContact;
import org.example.User.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManagerTCP extends Thread{
    private ArrayList<Socket> listSocket =new ArrayList<>();
    private ServerSocket serverAccept;

    private final ThreadManager threadManager =ThreadManager.getInstance();
    private static final NetworkManagerTCP instance = new NetworkManagerTCP();

    public ThreadManager getThreadManager() {
        return threadManager;
    }

    public static NetworkManagerTCP getInstance() {
        return instance;
    }
    private NetworkManagerTCP(){}
    private ListenConnectTCPThread listenConnectTCPThread;
    public synchronized void launchListenThread(int port){
        listenConnectTCPThread=new ListenConnectTCPThread(port);
        listenConnectTCPThread.start();
    }
    public void stopListenThread(){
        if (listenConnectTCPThread!=null)listenConnectTCPThread.interrupt();
    }
    public synchronized int getPortListeningConnection(){
        return this.listenConnectTCPThread.getServerPort();
    }
    public synchronized void stopAllSocket(){
        System.out.println("on deco de tt le monde");
        for (int i = 0; i < getListSocket().size(); i++) {
            deconnect(getListSocket().get(i).getInetAddress());
        }
    }

    public static int getPortLibre(){
        for (int port=1024; port<=65353; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                return port;//todo peut etre enlever le if
            } catch (IOException ignored) {}
        }
        return -1;//aucun port trouvé
    }


    public synchronized boolean connect(User u){
        System.out.println("on se co au user "+u);
        InetAddress hostname=u.getUserAddress().getAddress();
        try {
            //System.out.println("on demande a user de pseudo "+u.getPseudo());
            Socket socket =  new Socket(hostname, u.getUserAddress().getPort());
            //System.out.println("il a accept on ecoute sur le port "+socket.getPort());
            addSocket(socket);
            threadManager.createThreadCommunication(socket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deconnect(InetAddress address){
        System.out.println("on se deco de addr "+address);
        try {
            ThreadCom threadCom= (ThreadCom) threadManager.getThreadFromName(address.toString());
            Socket sock=getSocketFromIP(address);
            threadManager.killThread(threadCom);
            threadCom.getSockCom().close();
            listSocket.remove(sock);
            return true;
        } catch (ThreadNotFoundException | IOException | SocketComNotFoundException e) {
            return false;
        }
    }
    public synchronized boolean deconnect(User u){
        System.out.println("on se deco au user "+u);
        InetAddress address=u.getUserAddress().getAddress();
        try {
            ThreadCom threadCom= (ThreadCom) threadManager.getThreadFromName(address.toString());
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
