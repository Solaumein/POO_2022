package org.example.Message;

import junit.framework.TestCase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class MessageTest extends TestCase {
    public void testMessageEqual() {
       Date dateMsg= new Date(System.currentTimeMillis());
        ArrayList<Message> listDeMessagePasBon=new ArrayList<>();
        try {
            Message message1= new Message("coucou",dateMsg,true, InetAddress.getLocalHost());
            Message messagebon= new Message("coucou",dateMsg,true, InetAddress.getLocalHost());
            listDeMessagePasBon.add(new Message("coucou",dateMsg,false, InetAddress.getLocalHost()));
            listDeMessagePasBon.add(new Message("coucouX",dateMsg,true, InetAddress.getLocalHost()));
            listDeMessagePasBon.add(new Message("coucou",new Date(System.currentTimeMillis()+1),true, InetAddress.getLocalHost()));
            listDeMessagePasBon.add(new Message("coucou",dateMsg,true, InetAddress.getByName("google.fr")));
            assert(message1.equals(messagebon));
            for (Message message : listDeMessagePasBon) {
                assert(!message1.equals(message));
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
