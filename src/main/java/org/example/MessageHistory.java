package org.example;

import java.net.InetAddress;
import java.util.ArrayList;

public class MessageHistory {
    ArrayList<Message> listMessage;
    //InetAddress destinataire; //redondant

    public MessageHistory(){
        this.listMessage=new ArrayList<>();
    }

    public void addMessage(Message message){
        this.listMessage.add(message);
    }

    @Override
    public String toString() {
        StringBuilder listMessageInString=new StringBuilder();
        for (Message message : listMessage) {
            if(message.getRecu()) listMessageInString.append("Destinataire :");
            else listMessageInString.append("Vous :");
            listMessageInString.append(message).append("\n");
        }
        return listMessageInString.toString();
    }
}
