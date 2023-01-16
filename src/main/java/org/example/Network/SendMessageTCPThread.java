package org.example.Network;

import org.example.Message.LocalDbManager;
import org.example.Message.Message;
import org.example.Message.MessageHistory;
import org.example.Message.SQLiteHelper;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SendMessageTCPThread extends Thread {
    private Socket sockCom;
    private MessageHistory messageHistory;
    private OutputStream outputStream;
    public SendMessageTCPThread(Socket s){
        super(s.getInetAddress().toString()+ThreadManager.endNameSend);
        this.sockCom=s;
        this.messageHistory=new MessageHistory();
        try {
            outputStream=sockCom.getOutputStream();
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
            this.outputStream.close();
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

    public boolean send(String messageEnvoyer){
        System.out.println( "on lance un message "+messageEnvoyer);
        String messageSansRetour=messageEnvoyer.replace("\n",""+(char)0);
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        //envoie le message
        printWriter.println(messageSansRetour);
        //creation du message
        Message message=new Message(messageSansRetour,false,sockCom.getInetAddress());
        //sauvegarde du message
        this.messageHistory.addMessage(message);
        SQLiteHelper.getInstance().insert(message);//dans la BDD
        return true;


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
        SendMessageTCPThread threadCom = (SendMessageTCPThread) o;
        return Objects.equals(sockCom, threadCom.sockCom) && Objects.equals(messageHistory, threadCom.messageHistory) && Objects.equals(outputStream, threadCom.outputStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sockCom, messageHistory, outputStream);
    }
}