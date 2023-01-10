package org.example;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String contenu;
    private Date dateMessage;
    private boolean recu;

    private InetAddress otherHost;

    public Message(String contenu, Date dateMessage, boolean recu, InetAddress otherUser ){
        this.contenu=contenu;
        this.dateMessage=dateMessage;
        this.otherHost=otherUser;
        this.recu=recu;
    }
    public Message(String contenu,boolean recu,InetAddress otherUser){
        this.contenu=contenu;

        this.dateMessage= new Date(System.currentTimeMillis());


        this.recu=recu;
        this.otherHost=otherUser;
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
        return "msg sent by"+getOtherHost()+" at "+ getDateInString()+" : "+getContenu();
    }
}
