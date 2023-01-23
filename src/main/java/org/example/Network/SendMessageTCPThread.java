package org.example.Network;


import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SendMessageTCPThread extends Thread {
    private final Socket sockCom;
    private final OutputStream outputStream;
    public SendMessageTCPThread(Socket s){
        super(s.getInetAddress().toString()+ThreadManager.endNameSend);
        this.sockCom=s;
        try {
            outputStream=sockCom.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSockCom() {
        return sockCom;
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
        /*//chargement des anciens messages//todo sert a rien donc a enlever
        LocalDbManager localDbManager=LocalDbManager.getInstance();
        this.messageHistory=localDbManager.getMessageHistory(sockCom.getInetAddress());*/

    }

    public boolean send(String messageEnvoyer){
        System.out.println( "on lance un message "+messageEnvoyer);
        String messageSansRetourChariot=messageEnvoyer.replace("\n",""+(char)0);
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        //envoie le message
        printWriter.println(messageSansRetourChariot);
        /*//creation du message//todo g enlever ca avant test fonctionnel car utilise les couches superieures
        Message message=new Message(messageSansRetour,false,sockCom.getInetAddress());
        //sauvegarde du message
        this.messageHistory.addMessage(message);
        SQLiteHelper.getInstance().insert(message);//dans la BDD*/
        return true;


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
        return Objects.equals(sockCom, threadCom.sockCom) &&  Objects.equals(outputStream, threadCom.outputStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sockCom, outputStream);
    }
}
