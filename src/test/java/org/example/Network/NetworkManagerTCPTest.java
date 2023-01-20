package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.User.UserAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class NetworkManagerTCPTest
    extends TestCase
{


    @Override
    public void setUp() throws InterruptedException {
        NetworkManagerTCP.setMessageReceivedHandler(messageReceivedHandler2);
        while(NetworkManagerTCP.getMessageReceivedHandler()==null){
            Thread.sleep(100);
        }
    }



    /*public void testListenConnection() {
        networkManagerTCP.launchListenThread(NetworkManagerTCP.getPortLibre());//on lance l'ecoute
        try {
            int portServTCP=networkManagerTCP.getPortListeningConnection();
            System.out.println("on va se co sur le port "+portServTCP);
            Socket socket=new Socket(InetAddress.getLoopbackAddress(), portServTCP);
            Thread.sleep(1000);//le temps que s'ajoute dans la liste le socket
            System.out.println(networkManagerTCP.getListSocket());
            assertEquals(networkManagerTCP.getListSocket().size(),1);
            Socket remoteSocketSaved=networkManagerTCP.getSocketFromIP(InetAddress.getLoopbackAddress());
            assertEquals(socket.getPort(),remoteSocketSaved.getLocalPort());
            assertEquals(socket.getLocalPort(),remoteSocketSaved.getPort());
        } catch (SocketComNotFoundException | IOException | InterruptedException e) {
            e.printStackTrace();
            fail();
        }
        networkManagerTCP.stopListenThread();
        try {
            Socket socket=new Socket(InetAddress.getLoopbackAddress(), networkManagerTCP.getPortListeningConnection());
            fail();
        } catch (IOException e) {
            assert true;
        }
    }*/
    static ArrayList<String> listMessageAtest=new ArrayList<>();;
    private void initListMSG(){
        listMessageAtest.add("bonjour");//message normale
        listMessageAtest.add("aie aie aie\n plusieurs ligne");
        listMessageAtest.add("\n");
        listMessageAtest.add("");//message vide a tester
        listMessageAtest.add("é#яйца");//ascii compliqué
    }
    ArrayList<String> messageRecu=new ArrayList<>();
    MessageReceivedHandler messageReceivedHandler2= new MessageReceivedHandler() {
        @Override
        public void newMessageArrivedFromAddr(String message,InetAddress address) {
            System.out.println("MESSSSSSSSSSSSSSSSSSSSsage " + message+" from Address "+address);
            messageRecu.add(message);
        }
    };

    public void testEchangeTCP() throws InterruptedException {//test tous mis les uns après les autres pour eviter les tests jenkins qui se font en thread pour eviter les acces concurrents aux listes
        ConnectionDecoTestTCP();
        setUp();
        EmissionReceptionTest();
    }



 public void EmissionReceptionTest(){

     System.out.println("list sock "+NetworkManagerTCP.getInstance().getListSocket());
     //emission et reception message
     initListMSG();
     messageRecu.clear();
     System.out.println("on a init les MSG");
     NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());
     System.out.println("on a lancé l'écoute");
     try {
         Thread.sleep(100);//le temps que l'écoute se lance
         //System.out.println("list sock "+networkManagerTCP.getListSocket());
         assertEquals(0,NetworkManagerTCP.getInstance().getListSocket().size());
         if(!NetworkManagerTCP.getInstance().connect(new UserAddress(InetAddress.getByName("127.0.0.1"),NetworkManagerTCP.getInstance().getPortListeningConnection()))){
             System.out.println("connection impossible");
             fail();
         }
         Thread.sleep(100);//le temps que s'ajoute dans la liste le socket
         //System.out.println("list sock "+networkManagerTCP.getListSocket());
         assertEquals(2,NetworkManagerTCP.getInstance().getListSocket().size());

         System.out.println("la list thread "+NetworkManagerTCP.getInstance().getThreadManager().getListThread());
         System.out.println("la list sock "+NetworkManagerTCP.getInstance().getListSocket());

         //ListenMessageTCPThread threadPuit= (ListenMessageTCPThread) NetworkManagerTCP.getInstance().getThreadManager().getThreadListenFromName(InetAddress.getLoopbackAddress().toString());
         SendMessageTCPThread threadSource= (SendMessageTCPThread) NetworkManagerTCP.getInstance().getThreadManager().getThreadSendFromName(InetAddress.getByName("127.0.0.1").toString());
         for (String s1 : listMessageAtest) {
             threadSource.send(s1);
         }
     } catch (InterruptedException e) {
         throw new RuntimeException(e);
     } catch (UnknownHostException | ThreadNotFoundException e) {
         e.printStackTrace();
         fail();
     }
     try {
         Thread.sleep(1000);
     } catch (InterruptedException e) {
         throw new RuntimeException(e);
     }
     assertEquals(listMessageAtest,messageRecu);
 }

public void ConnectionDecoTestTCP(){
    //connection et deconnection

    NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());//on lance l'ecoute
    try {
        int portServTCP=NetworkManagerTCP.getInstance().getPortListeningConnection();
        System.out.println("on va se co sur le port "+portServTCP);
        NetworkManagerTCP.getInstance().connect(new UserAddress(InetAddress.getLoopbackAddress(), portServTCP));//Socket socket=new Socket(InetAddress.getLoopbackAddress(), portServTCP);
        System.out.println("on s'est co "+NetworkManagerTCP.getInstance().getListSocket());
        Socket socket=NetworkManagerTCP.getInstance().getSocketFromIP(InetAddress.getLoopbackAddress());
        Thread.sleep(100);//le temps que s'ajoute dans la liste le socket
        System.out.println(NetworkManagerTCP.getInstance().getListSocket());
        assertEquals(2,NetworkManagerTCP.getInstance().getListSocket().size());
        Socket remoteSocketSaved=NetworkManagerTCP.getInstance().getSocketFromIP(InetAddress.getLoopbackAddress());
        assertEquals(socket.getPort(),remoteSocketSaved.getPort());
        assertEquals(socket.getLocalPort(),remoteSocketSaved.getLocalPort());
    } catch (SocketComNotFoundException  | InterruptedException e) {
        e.printStackTrace();
        fail();
    }
    NetworkManagerTCP.getInstance().stopListenThread();
    boolean co= NetworkManagerTCP.getInstance().connect(new UserAddress(InetAddress.getLoopbackAddress(), NetworkManagerTCP.getInstance().getPortListeningConnection()));
    //System.out.println("cooooo "+co);
    if( co){
        fail();
    }else{
        assert true;
    }

    NetworkManagerTCP.getInstance().stopAllSocket();
    assertEquals(0,NetworkManagerTCP.getInstance().getListSocket().size());

}

}
