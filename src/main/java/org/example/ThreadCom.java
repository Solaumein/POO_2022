package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadCom extends Thread {
    Socket sockCom;
    ThreadCom(Socket s){
        super(s.getInetAddress().toString());
        this.sockCom=s;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        //System.out.println("le nouveau nom "+this.getName()+" "+threadName);
        //todo faire 2eme thread d'envoi avec send de nouveau port
    }

    boolean send(String s){
        System.out.println(this + ": on va lancer le write du message "+s);
        PrintWriter out;
        try {
            out = new PrintWriter(sockCom.getOutputStream(), true);
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
            BufferedReader in = new BufferedReader(new InputStreamReader(sockCom.getInputStream()));
            String res= in.readLine();
            System.out.println(this + ": on a recu un message "+res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean close() {
        try {
            sockCom.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    @Override
    public String toString() {
        return this.getName();
    }
}
