package org.example.Message;

import junit.framework.TestCase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class MessageHistoryTest extends TestCase {
    public void testMessageHistoryEqual() {
        //Date dateMsg= new Date(System.currentTimeMillis());
        ArrayList<Message> listDeMessage=new ArrayList<>();
        try {
            listDeMessage.add(new Message("coucou",false,  InetAddress.getByName("google.fr")));
            listDeMessage.add(new Message("ca va",true,  InetAddress.getByName("google.fr")));
            listDeMessage.add(new Message("oui et toi",false,  InetAddress.getByName("google.fr")));
            listDeMessage.add(new Message("trkl",true,  InetAddress.getByName("google.fr")));
            listDeMessage.add(new Message("a la prochaine",false, InetAddress.getByName("google.fr")));
            listDeMessage.add(new Message("je suis le laitier",true, InetAddress.getByName("google.fr")));
            MessageHistory messageHistory1=new MessageHistory();
            MessageHistory messageHistory2=new MessageHistory();
            for (Message message : listDeMessage) {
                messageHistory1.addMessage(message);
                messageHistory2.addMessage(message);
            }
            
            assertEquals(messageHistory1,messageHistory2);
            messageHistory1.addMessage(listDeMessage.get(1));
            assertFalse(messageHistory1.equals(messageHistory2));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
