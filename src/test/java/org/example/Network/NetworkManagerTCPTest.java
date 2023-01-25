package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.User.UserAddress;

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
        NetworkManagerTCP.getInstance().setMessageReceivedHandler(messageReceivedHandler2);
        while(NetworkManagerTCP.getInstance().getMessageReceivedHandler()==null){
            Thread.sleep(100);
        }
        System.out.println("c bon c init");
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
    static ArrayList<String> listMessageAtest=new ArrayList<>();
    private void initListMSG(){
        listMessageAtest.add("bonjour");//message normale
        listMessageAtest.add("aie aie aie\n plusieurs ligne");
        listMessageAtest.add("\n");
        listMessageAtest.add("");//message vide a tester
        listMessageAtest.add("é#яйца");//ascii compliqué
    }
    ArrayList<String> messageRecu=new ArrayList<>();
    MessageReceivedHandler messageReceivedHandler2= (message, address) -> {
        System.out.println("MESSSSSSSSSSSSSSSSSSSSsage " + message+" from Address "+address);
        messageRecu.add(message);
    };

    public void testEchangeTCP() throws InterruptedException {//test tous mis les uns après les autres pour eviter les tests jenkins qui se font en thread pour eviter les acces concurrents aux listes
        ConnectionDecoTestTCP();
        resetAll();
        setUp();
        EmissionReceptionTest();
        resetAll();
    }

    private void resetAll() {
        NetworkManagerTCP.getInstance().reset();
        ThreadManager.getInstance().reset();
    }


    private void EmissionReceptionTest(){

        System.out.println("list sock "+NetworkManagerTCP.getInstance().getListSocket());
        //emission et reception message
        initListMSG();
        messageRecu.clear();
        System.out.println("on a init les MSG");
        System.out.println("la list thread "+ThreadManager.getInstance().getListThread());

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

         System.out.println("la list thread "+ThreadManager.getInstance().getListThread());
         System.out.println("la list sock "+NetworkManagerTCP.getInstance().getListSocket());

         //ListenMessageTCPThread threadPuit= (ListenMessageTCPThread) NetworkManagerTCP.getInstance().getThreadManager().getThreadListenFromName(InetAddress.getLoopbackAddress().toString());
         SendMessageTCPThread threadSource= (SendMessageTCPThread) ThreadManager.getInstance().getThreadSendFromName(InetAddress.getByName("127.0.0.1").toString());
         for (String s1 : listMessageAtest) {
             if(!threadSource.send(s1)){
                 fail();
             }
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

private void ConnectionDecoTestTCP() throws InterruptedException {
    //connection et deconnection

    NetworkManagerTCP.getInstance().launchListenThread(NetworkManagerTCP.getPortLibre());//on lance l'ecoute
    try {
        int portServTCP=NetworkManagerTCP.getInstance().getPortListeningConnection();
        System.out.println("on va se co sur le port "+portServTCP);
        NetworkManagerTCP.getInstance().connect(new UserAddress(InetAddress.getLoopbackAddress(), portServTCP));//Socket socket=new Socket(InetAddress.getLoopbackAddress(), portServTCP);
        System.out.println("on s'est co "+NetworkManagerTCP.getInstance().getListSocket());
        Socket socket=NetworkManagerTCP.getInstance().getSocketFromIP(InetAddress.getLoopbackAddress());
        Thread.sleep(1000);//le temps que s'ajoute dans la liste le socket
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
    Thread.sleep(100);
    boolean co= NetworkManagerTCP.getInstance().connect(new UserAddress(InetAddress.getLoopbackAddress(), NetworkManagerTCP.getInstance().getPortListeningConnection()));
    System.out.println("cooooo "+co);
    if( co){
        fail();
    }else{
        assert true;
    }

    NetworkManagerTCP.getInstance().stopAllSocket();
    assertEquals(0,NetworkManagerTCP.getInstance().getListSocket().size());

}

}
