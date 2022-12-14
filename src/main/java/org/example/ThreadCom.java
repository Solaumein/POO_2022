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

    BufferedReader in;
    ThreadCom(Socket s){
        super(s.getInetAddress().toString());
        this.sockCom=s;

        try {
            in=new BufferedReader(new InputStreamReader(sockCom.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("ca commence");
        //String messageSTR=receive();
        //Message message=new Message(messageSTR);
        //todo traitement des message
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
    private String readMultipleLine(BufferedReader in){
        StringBuilder everything = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {//car 0 pour simuler
                everything.append(line).append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return everything.toString();
    }
    String receive() {
        try {
            System.out.println(this + ": on va lancer le read");
            String res=   in.readLine();
            Message message=new Message(res);
            System.out.println(sockCom.getInetAddress()+" a envoyer le message suivant :\n"+message.toString());
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
