package org.example;


import java.net.Socket;
import java.util.ArrayList;

public class NetworkManager {
    ArrayList<Socket> lisSocket;
    NetworkManager(){
        this.lisSocket=new ArrayList<>();
    }

    public void addSocket(Socket s){this.lisSocket.add(s);}
}
