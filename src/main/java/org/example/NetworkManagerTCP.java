package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManagerTCP {
    ArrayList<Socket> lisSocket;
    ServerSocket serverAccept;
    NetworkManagerTCP(){//init la liste de socket
        lisSocket=new ArrayList<>();
        //todo ServerSocket serverSocketStartCo()
        /*while(true){
           accept
        }*/
    }

    public boolean start(){
        try {
            serverAccept=new ServerSocket(42069);
            while(true){
                Socket s=listening();
                addSocket(s);
                startThread(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startThread(Socket s) {
        ThreadCom threadCom=new ThreadCom(s);
    }

    private Socket listening() {
        try {
            System.out.println("on se met en ecoute...");
            Socket socket =  serverAccept.accept();
            InetAddress addr= socket.getInetAddress();
            System.out.println("on s'est co avec"+addr);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //argument sera utile pour rajouter/modifier la liste des contact
    //boolean sera utile pour montrer si pb de co


    boolean connect(User u){
        InetAddress hostname=u.getUserAddress().getAddress();
        try {
            System.out.println("on demande a user de pseudo "+u.getPseudo());
            Socket socket =  new Socket(hostname, u.getUserAddress().getPort());
            System.out.println("il a accept on ecoute sur le port "+socket.getPort());
            addSocket(socket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean send(String s){
        System.out.println(this + ": on va lancer le write du message "+s);
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(s);
            System.out.println(this + ": on a lancer un message "+s);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    String receive() {
        try {
            System.out.println(this + ": on va lancer le read");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String res= in.readLine();
            System.out.println(this + ": on a recu un message "+res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSocket(Socket s){this.lisSocket.add(s);}

    @Override
    public String toString() {
        return socket.getInetAddress().toString()+" ";
    }
}
