package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    String contenu;
    Date dateMessage;

    Message(String contenu,Date dateMessage){
        this.contenu=contenu;
        this.dateMessage=dateMessage;

    }
    Message(String contenu){
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

    @Override
    public String toString() {
        return getDateInString()+" : "+getContenu();
    }
}
