package org.example.Message;

import java.util.ArrayList;
import java.util.Objects;

public class MessageHistory {
    private final ArrayList<Message> listMessage;
    //InetAddress destinataire; //redondant

    public MessageHistory(){
        this.listMessage=new ArrayList<>();
    }

    public void addMessage(Message message){
        this.listMessage.add(message);
    }

    @Override
    public String toString() {
        StringBuilder listMessageInString=new StringBuilder().append("MESSAGEHISTORY : \n");
        for (Message message : listMessage) {
            if(message.getRecu()) listMessageInString.append("Destinataire :").append(message.getOtherHost()).append(" ");
            else listMessageInString.append("Vous :");
            listMessageInString.append(message.getDateInString()).append(" contenu ").append(message.getContenu()).append("\n");
        }
        return listMessageInString.toString();
    }

    public ArrayList<Message> getListMessage() {
        return listMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() ||this.listMessage.size()!=((MessageHistory) o).listMessage.size()) return false;
        MessageHistory that = (MessageHistory) o;
        boolean result=true;
        for (int i = 0; i < this.listMessage.size() && result ; i++) {//si le result est false alors la boucle est cassée et result est false
            result= this.listMessage.get(i).equals(that.listMessage.get(i));
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listMessage);
    }
}
