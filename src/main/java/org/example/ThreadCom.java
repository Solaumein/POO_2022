package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ThreadCom extends Thread {
    Socket sockCom;
    MessageHistory messageHistory;
    BufferedReader in;
    ThreadCom(Socket s){
        super(s.getInetAddress().toString());
        this.sockCom=s;
        this.messageHistory=new MessageHistory();
        try {
            in=new BufferedReader(new InputStreamReader(sockCom.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("ca commence");
        //chargement des anciens messages
        LocalDbManager localDbManager=LocalDbManager.getInstance();
        this.messageHistory=localDbManager.getMessageHistory(sockCom.getInetAddress());

    }

    boolean send(String messageEnvoyer){
        try {
            //System.out.println(this + ": on va lancer le write du message "+s);
            PrintWriter out;
            out = new PrintWriter(sockCom.getOutputStream(), true);
            //envoie le message
            out.println(messageEnvoyer);
            //creation du message
            Message message=new Message(messageEnvoyer,false);
            //sauvegarde du message
            this.messageHistory.addMessage(message);
            //System.out.println(this + ": on a lancer un message "+s);
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
            //System.out.println(this + ": on va lancer le read");

            String stringRecu=   in.readLine();
            //creation du message
            Message message=new Message(stringRecu,true);
            //sauvegarde du message
            this.messageHistory.addMessage(message);

            //System.out.println(sockCom.getInetAddress()+" a envoyer le message suivant :\n"+message.toString());
            return stringRecu;
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
