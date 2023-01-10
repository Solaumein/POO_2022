package org.example.MessageBDD;

import junit.framework.TestCase;
import org.example.Message;
import org.example.MessageBDD.SQLiteHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BDDtest extends TestCase {
    public void testSQLiteBDD() throws InterruptedException {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance();
        sqLiteHelper.reset();

        sqLiteHelper.createTableMessage();
        //SQLiteHelper.selectAll();
        ArrayList<Message> listMsg=new ArrayList<>();
        try {
            listMsg.add(new Message("1er message",true, InetAddress.getByName("google.fr")));
            listMsg.add(new Message("2eme message",false, InetAddress.getLocalHost()));
            listMsg.add(new Message("3eme message",false, InetAddress.getLocalHost()));
            for (Message message : listMsg) {
                sqLiteHelper.insert(message);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        sqLiteHelper.printAll();
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

}
