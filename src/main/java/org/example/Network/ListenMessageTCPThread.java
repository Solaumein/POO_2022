package org.example.Network;

import org.example.Message.Message;
import org.example.Message.MessageHistory;
import org.example.Message.SQLiteHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListenMessageTCPThread extends Thread {

    private MessageReceivedHandler messageReceivedHandler;
    private Socket sockCom;
    private MessageHistory messageHistory;
    private BufferedReader in;

    public ListenMessageTCPThread(Socket socket, MessageReceivedHandler messageReceivedHandler) {
        super(socket.getInetAddress().toString()+ThreadManager.endNameListen);
        //System.out.println("on creer le thread "+this.getName());

        this.messageReceivedHandler = messageReceivedHandler;
        this.sockCom=socket;
        this.messageHistory=new MessageHistory();
        try {
            in=new BufferedReader(new InputStreamReader(sockCom.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receive() {

        try {
            String stringRecu= in.readLine();
            if(stringRecu==null) return null;
                String messageWithMultipleLine = stringRecu.replace("" + (char) 0, "\n");
                //System.out.println("message apres parse "+messageWithMultipleLine);
                //creation du message
                Message message = new Message(messageWithMultipleLine, true, sockCom.getInetAddress());
                //sauvegarde du message
                this.messageHistory.addMessage(message);
                SQLiteHelper.getInstance().insert(message);
                System.out.println("message recccu " + message);
                return messageWithMultipleLine;

        } catch (IOException e) {
           return null;
        }
    }
    @Override
    public void run() {
        while (true) {
            String msgReceived= receive();
            if(msgReceived==null || NetworkManagerTCP.getMessageReceivedHandler()==null ) {
                try {
                    sleep(100);
                } catch (InterruptedException ignored) {} //ignored car lors du kill ca fait erreur
            }else {
                NetworkManagerTCP.getMessageReceivedHandler().newMessageArrivedFromAddr(msgReceived,sockCom.getInetAddress());
            }
        }
    }
    @Override
    public String toString() {
        return this.getName();
    }

}
