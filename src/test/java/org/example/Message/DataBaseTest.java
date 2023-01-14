package org.example.Message;

import junit.framework.TestCase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DataBaseTest extends TestCase {
    public void testGetListMessageHistory() throws InterruptedException {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance();
        sqLiteHelper.reset();
        LocalDbManager localDbManager=LocalDbManager.getInstance();
        sqLiteHelper.createTableMessage();
        //SQLiteHelper.selectAll();
        ArrayList<Message> listMsg=new ArrayList<>();
        ArrayList<InetAddress> listAddr=new ArrayList<>();
        try {
            listAddr.add(InetAddress.getByName("142.251.37.227"));
            listAddr.add(InetAddress.getByName("172.65.229.194"));
            listAddr.add(InetAddress.getByName("195.83.9.85"));
            listMsg.add(new Message("Bonjour de gogle",true, listAddr.get(0)));
            listMsg.add(new Message("salut je suis deepl",true, listAddr.get(1)));
            listMsg.add(new Message("salut ent",true, listAddr.get(2)));
            listMsg.add(new Message("reponse de moi a deepl",false,listAddr.get(1)));
            listMsg.add(new Message("reponse de ent",false, listAddr.get(2)));
            for (Message message : listMsg) {
                sqLiteHelper.insert(message);
                localDbManager.addMessage(message);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        sqLiteHelper.printAll();
        HashMap<InetAddress, MessageHistory> bddSaved=sqLiteHelper.getMessageHistory();

        System.out.println("BDD saved "+bddSaved);
        System.out.println("history saved "+localDbManager.getMessageHistoryDB());
        for (InetAddress inetAddress : listAddr) {
            MessageHistory messageHistoryLocal=localDbManager.getMessageHistory(inetAddress);
            MessageHistory messageHistoryBDD=bddSaved.get(inetAddress);
            //System.out.println("equals ? "+messageHistoryBDD.equals(messageHistoryLocal));
            //System.out.println(""+messageHistoryBDD+messageHistoryLocal);
            assertEquals(messageHistoryBDD,messageHistoryLocal);
        }
    }
   // private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//même que dans SQLiteHelper

    public void testDateFormat(){
        Date date= new Date();
        String dateInFormatted=SQLiteHelper.dateFormat.format(date);
        System.out.println(dateInFormatted);
        try {
            assertEquals(date,SQLiteHelper.dateFormat.parse(dateInFormatted));
        } catch (ParseException e) {
            fail();
        }
    }

    public void testSelectContact(){
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance();
        sqLiteHelper.reset();
        LocalDbManager localDbManager=LocalDbManager.getInstance();
        sqLiteHelper.createTableMessage();
        //SQLiteHelper.selectAll();
        ArrayList<Message> listMsg=new ArrayList<>();
        MessageHistory listMsgFromDeepl=new MessageHistory();
        ArrayList<InetAddress> listAddr=new ArrayList<>();
        try {
            listAddr.add(InetAddress.getByName("142.251.37.227"));
            listAddr.add(InetAddress.getByName("172.65.229.194"));
            listAddr.add(InetAddress.getByName("195.83.9.85"));
            listMsg.add(new Message("Bonjour de gogle",true, listAddr.get(0)));
            Message msg1deepl=new Message("salut je suis deepl",true, listAddr.get(1));
            Message msg2deepl=new Message("reponse de moi a deepl",false,listAddr.get(1));
            listMsg.add(msg1deepl);
            listMsgFromDeepl.getListMessage().add(msg1deepl);
            listMsg.add(new Message("salut ent",true, listAddr.get(2)));
            listMsg.add(msg2deepl);
            listMsgFromDeepl.getListMessage().add(msg2deepl);

            listMsg.add(new Message("reponse de ent",false, listAddr.get(2)));
            for (Message message : listMsg) {
                sqLiteHelper.insert(message);
                localDbManager.addMessage(message);
            }
            System.out.println(listMsgFromDeepl+"\n"+SQLiteHelper.getInstance().getMessageHistoryFromUser(listAddr.get(1)));
            assertEquals(listMsgFromDeepl,SQLiteHelper.getInstance().getMessageHistoryFromUser(listAddr.get(1)));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


        //System.out.println("BDD saved "+bddSaved);
        System.out.println("history saved "+localDbManager.getMessageHistoryDB());

    }
}
