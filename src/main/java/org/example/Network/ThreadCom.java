package org.example.Network;

import org.example.Message.LocalDbManager;
import org.example.Message.Message;
import org.example.Message.MessageHistory;
import org.example.Message.SQLiteHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

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
            Message message=new Message(messageEnvoyer,false,sockCom.getInetAddress());
            //sauvegarde du message
            this.messageHistory.addMessage(message);
            SQLiteHelper.getInstance().insert(message);//dans la BDD
            //System.out.println(this + ": on a lancer un message "+s);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    private String readMultipleLine(BufferedReader in){
        StringBuilder SB = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {//car 0 pour simuler
                SB.append(line).append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return SB.toString();
    }
    String receive() {
        try {
            String stringRecu=in.readLine();
            //creation du message
            Message message=new Message(stringRecu,true,sockCom.getInetAddress());
            //sauvegarde du message
            this.messageHistory.addMessage(message);
            SQLiteHelper.getInstance().insert(message);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadCom threadCom = (ThreadCom) o;
        return Objects.equals(sockCom, threadCom.sockCom) && Objects.equals(messageHistory, threadCom.messageHistory) && Objects.equals(in, threadCom.in);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sockCom, messageHistory, in);
    }
}
