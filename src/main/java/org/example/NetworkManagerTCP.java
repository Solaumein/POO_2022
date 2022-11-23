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
    Socket socket = null;
    ArrayList<Socket> lisSocket;

    boolean accept;
    NetworkManagerTCP(){//init la liste de socket
        lisSocket=new ArrayList<>();
        //todo ServerSocket serverSocketStartCo()
        /*while(true){
           accept
        }*/
    }

    public boolean start(User u,boolean accept){//todo a remplacer apres le while true
        this.accept=accept;
        if(accept) return accept(u);//attend la connection du user
        else return connect(u);//se connecte au user
    }
    //argument sera utile pour rajouter/modifier la liste des contact
    //boolean sera utile pour montrer si pb de co
    private boolean accept(User u){
        ServerSocket serverSocket;
        try {
            serverSocket=new ServerSocket(u.getUserAddress().getPort());
            System.out.println("on se met en ecoute...");
            socket =  serverSocket.accept();
            System.out.println("on s'est co");
            addSocket(socket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean connect(User u){
        InetAddress hostname=u.getUserAddress().getAddress();
        try {
            System.out.println("on demande a user de pseudo "+u.getPseudo());
            socket =  new Socket(hostname, u.getUserAddress().getPort());
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
