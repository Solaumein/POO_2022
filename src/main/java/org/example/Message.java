package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String contenu;
    private Date dateMessage;
    private boolean recu;

    Message(String contenu,Date dateMessage,boolean recu){
        this.contenu=contenu;
        this.dateMessage=dateMessage;

    }
    Message(String contenu,boolean recu){
        this.contenu=contenu;
        this.dateMessage= new Date();

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

    @Override
    public String toString() {
        return getDateInString()+" : "+getContenu();
    }
}
