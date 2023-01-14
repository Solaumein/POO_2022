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
    private Socket sockCom;
    private MessageHistory messageHistory;
    private BufferedReader in;
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

    public Socket getSockCom() {
        return sockCom;
    }

    public MessageHistory getMessageHistory() {
        return messageHistory;
    }

    @Override
    public void interrupt() {//before being killed it closes its socket
        try {
            this.in.close();
            this.sockCom.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.interrupt();
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
            System.out.println( "on lance un message "+messageEnvoyer);
            String messageSansRetour=messageEnvoyer.replace("\n",""+(char)0);
            PrintWriter out;
            out = new PrintWriter(sockCom.getOutputStream(), true);
            //envoie le message
            out.println(messageSansRetour);
            //creation du message
            Message message=new Message(messageSansRetour,false,sockCom.getInetAddress());
            //sauvegarde du message
            this.messageHistory.addMessage(message);
            SQLiteHelper.getInstance().insert(message);//dans la BDD
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    String receive() {
        try {
            String stringRecu=in.readLine();
            //System.out.println("message avant parse "+stringRecu);
            String messageWithMultipleLine=stringRecu.replace(""+(char)0,"\n");
            //System.out.println("message apres parse "+messageWithMultipleLine);
            //creation du message
            Message message=new Message(messageWithMultipleLine,true,sockCom.getInetAddress());
            //sauvegarde du message
            this.messageHistory.addMessage(message);
            SQLiteHelper.getInstance().insert(message);
            System.out.println("message recu "+message);
            return messageWithMultipleLine;
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
