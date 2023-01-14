package org.example.Message;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Message {
    private String contenu;
    private Date dateMessage;
    private boolean recu;
    private InetAddress otherHost;

    public Message(String contenu, Date dateMessage, boolean recu, InetAddress otherUser){
        this.contenu=contenu;
        this.dateMessage=dateMessage;
        this.otherHost=otherUser;
        this.recu=recu;

    }
    public Message(String contenu, boolean recu, InetAddress otherUser){
        this.contenu=contenu;
        this.dateMessage=new Date(System.currentTimeMillis());
        this.otherHost=otherUser;
        this.recu=recu;
    }

    public String getDateInString(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return formatter.format(this.dateMessage);
    }

    public String getContenu() {
        return contenu;
    }
    public Date getDateMessage() {
        return dateMessage;
    }
    public boolean getRecu() {
        return recu;
    }

    public boolean isRecu() {
        return recu;
    }

    public InetAddress getOtherHost() {
        return otherHost;
    }

    @Override
    public String toString() {
        String stateOfMsg=((recu) ? "Recu" : "envoyé");;
        return "msg "+stateOfMsg+ " par toi et l'addresse "+getOtherHost()+" at "+ getDateInString()+" : "+getContenu();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return recu == message.recu && Objects.equals(contenu, message.contenu) && Objects.equals(dateMessage, message.dateMessage) && Objects.equals(otherHost, message.otherHost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contenu, dateMessage, recu, otherHost);
    }
}
