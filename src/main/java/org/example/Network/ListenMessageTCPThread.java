package org.example.Network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListenMessageTCPThread extends Thread {

    private final MessageReceivedHandler messageReceivedHandler;
    private final Socket sockCom;
    private final BufferedReader in;

    public ListenMessageTCPThread(Socket socket, MessageReceivedHandler messageReceivedHandler) {
        super(socket.getInetAddress().toString()+ThreadManager.endNameListen);
        //System.out.println("on creer le thread "+this.getName());

        this.messageReceivedHandler = messageReceivedHandler;
        this.sockCom=socket;
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
            String messageWithMultipleLine =cleanStringRecu(stringRecu);
            //System.out.println("message apres parse "+messageWithMultipleLine);
            //creation du message
            //Message message = new Message(messageWithMultipleLine, true, sockCom.getInetAddress());
               /* //sauvegarde du message
                this.messageHistory.addMessage(message);
                SQLiteHelper.getInstance().insert(message);*/
            System.out.println("message recccu " + messageWithMultipleLine);
            return messageWithMultipleLine;

        } catch (IOException e) {
           return null;
        }
    }

    private String cleanStringRecu(String stringRecu) {
        return stringRecu.replace("" + (char) 0, "\n");
    }

    @Override
    public void run() {
        while (true) {
            String msgReceived= receive();
            if(msgReceived==null || messageReceivedHandler==null ) {
                try {
                    sleep(100);
                } catch (InterruptedException ignored) {} //ignored car lors du kill ca fait erreur
            }else {
                messageReceivedHandler.newMessageArrivedFromAddr(msgReceived,sockCom.getInetAddress());
            }
        }
    }
    @Override
    public String toString() {
        return this.getName();
    }

}
