package org.example;

import org.example.Exception.SocketComNotFoundException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManagerTCP extends Thread{
    private ArrayList<Socket> listSocket =new ArrayList<>();;
    private ServerSocket serverAccept;
    private ThreadManager threadManager =ThreadManager.getInstance();
    private static final NetworkManagerTCP instance = new NetworkManagerTCP();

    public static NetworkManagerTCP getInstance() {
        return instance;
    }
    private NetworkManagerTCP(){}

    public void run(){
        try {
            serverAccept=new ServerSocket(42069);
            while(true){
                Socket s=listening();
                addSocket(s);
                threadManager.createThreadCommunication(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private Socket listening() {
        try {
            //System.out.println("on se met en ecoute...");
            Socket socket =  serverAccept.accept();
            InetAddress addr= socket.getInetAddress();
            //System.out.println("on s'est co avec"+addr);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //argument sera utile pour rajouter/modifier la liste des contact
    //boolean sera utile pour montrer si pb de co


    public synchronized boolean connect(User u){
        InetAddress hostname=u.getUserAddress().getAddress();
        try {
            //System.out.println("on demande a user de pseudo "+u.getPseudo());
            Socket socket =  new Socket(hostname, 42069);
            //System.out.println("il a accept on ecoute sur le port "+socket.getPort());
            addSocket(socket);
            threadManager.createThreadCommunication(socket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized ArrayList<Socket> getListSocket() {
        return listSocket;
    }

    public synchronized Socket getSocketFromIP(InetAddress ip) throws SocketComNotFoundException{
        for (Socket socket : listSocket) {
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

    public synchronized void addSocket(Socket s){this.listSocket.add(s);}

    @Override
    public synchronized String toString() {
        return listSocket.toString();
    }
}
