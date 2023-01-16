package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.SocketComNotFoundException;
import org.example.Exception.ThreadNotFoundException;
import org.example.Message.Message;
import org.example.Message.SQLiteHelper;
import org.example.User.User;
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

    NetworkManagerTCP networkManagerTCP;

    @Override
    protected void setUp() throws Exception {
        networkManagerTCP= NetworkManagerTCP.getInstance();

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

    public void testEchangeTCP(){
        //connection et deconnection
        networkManagerTCP.launchListenThread(NetworkManagerTCP.getPortLibre());//on lance l'ecoute
        try {
            int portServTCP=networkManagerTCP.getPortListeningConnection();
            System.out.println("on va se co sur le port "+portServTCP);
            Socket socket=new Socket(InetAddress.getLoopbackAddress(), portServTCP);
            Thread.sleep(1000);//le temps que s'ajoute dans la liste le socket
            System.out.println(networkManagerTCP.getListSocket());
            assertEquals(1,networkManagerTCP.getListSocket().size());
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
        networkManagerTCP.stopAllSocket();
        assertEquals(0,networkManagerTCP.getListSocket().size());

        //System.out.println("list sock "+networkManagerTCP.getListSocket());
        //emission et reception message
        initListMSG();
        System.out.println("on a init les MSG");
        networkManagerTCP.launchListenThread(NetworkManagerTCP.getPortLibre());
        System.out.println("on a lancé l'écoute");
        try {
            Thread.sleep(100);//le temps que l'écoute se lance
            //System.out.println("list sock "+networkManagerTCP.getListSocket());
            assertEquals(0,networkManagerTCP.getListSocket().size());
            networkManagerTCP.connect(new UserAddress(InetAddress.getByName("127.0.0.1"),networkManagerTCP.getPortListeningConnection()));
            Thread.sleep(100);//le temps que s'ajoute dans la liste le socket
            //System.out.println("list sock "+networkManagerTCP.getListSocket());
            assertEquals(2,networkManagerTCP.getListSocket().size());

            System.out.println("la list "+networkManagerTCP.getThreadManager().getListThread());
            ThreadCom threadPuit= (ThreadCom) networkManagerTCP.getThreadManager().getListThread().get(0);
            ThreadCom threadSource= (ThreadCom) networkManagerTCP.getThreadManager().getListThread().get(1);
            System.out.println("le thread Com "+threadPuit.getMessageHistory());
            for (String s1 : listMessageAtest) {
               threadSource.send(s1);
               String recu= threadPuit.receive();
               assertEquals(s1,recu);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail();
        }
    }
    /*
    public void testReception() {
        initListMSG();
        System.out.println("on a init les MSG");
        networkManagerTCP.launchListenThread(NetworkManagerTCP.getPortLibre());
        System.out.println("on a lancé l'écoute");
        try {
            Thread.sleep(100);//le temps que l'écoute se lance
            assertEquals(networkManagerTCP.getListSocket().size(),0);
            new Thread(this::launchSend).start();
            Thread.sleep(100);//le temps que s'ajoute dans la liste le socket
            assertEquals(networkManagerTCP.getListSocket().size(),1);
            System.out.println("la list "+networkManagerTCP.getThreadManager().getListThread());
            ThreadCom threadPuit= (ThreadCom) networkManagerTCP.getThreadManager().getThreadFromName(String.valueOf(InetAddress.getByName("127.0.0.1")));
            System.out.println("le thread Com "+threadPuit.getMessageHistory());
            for (String s1 : listMessageAtest) {
                testMessageRecu(threadPuit,s1,InetAddress.getByName("127.0.0.1"));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ThreadNotFoundException e) {
            fail();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testEmission() {
        initListMSG();
        System.out.println("on a init les MSG");
        networkManagerTCP.launchListenThread(NetworkManagerTCP.getPortLibre());
        System.out.println("on a lancé l'écoute");
        try {
            Thread.sleep(100);//le temps que l'écoute se lance
            new Thread(this::launchReceive).start();
            Thread.sleep(100);//le temps que s'ajoute dans la liste le socket
            System.out.println("la list "+networkManagerTCP.getThreadManager().getListThread());
            ThreadCom threadPuit= (ThreadCom) networkManagerTCP.getThreadManager().getThreadFromName(String.valueOf(InetAddress.getByName("127.0.0.1")));
            System.out.println("le thread Com "+threadPuit.getMessageHistory());
            for (String s1 : listMessageAtest) {
                threadPuit.send(s1);
            }
            //networkManagerTCP.deconnect(new User(new UserAddress(InetAddress.getByName("127.0.0.1"),threadPuit.getSockCom().getPort()),"google"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ThreadNotFoundException e) {
            fail();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail();
        }

    }

*/

    private void testMessageRecu(ThreadCom tc, String expectedString,InetAddress inetExpected) {
        String msgRecu =tc.receive();
        //Message message=new Message(msgRecu);
        System.out.println("on a recu "+msgRecu);
        assertEquals(expectedString, msgRecu);
        assertEquals(tc.getSockCom().getInetAddress(),inetExpected);
    }

    private void launchSend() {//simule la co et l'envoie en TCP d'un autre utilisateur
        try {
            InetAddress hostname= InetAddress.getByName("127.0.0.1");
            System.out.println("le send veut se co avec l'ip "+hostname);
            Socket socket =  new Socket(hostname, 1024);
            System.out.println("le send s'est co "+socket);
            PrintWriter out;
            out = new PrintWriter(socket.getOutputStream(), true);
            Thread.sleep(1000);
            //envoie chaque message
            for (String s : listMessageAtest) {
                System.out.println("on envoie "+s);
                out.println(s);
            }
        } catch (Exception e) {
           e.printStackTrace();
           fail();
        }
    }
    private void launchReceive() {
        try {
            InetAddress hostname= InetAddress.getByName("127.0.0.1");
            System.out.println("le receive veut se co avec l'ip "+hostname);
            Socket socket =  new Socket(hostname, 1024);
            System.out.println("le receive s'est co "+socket);
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            Thread.sleep(1000);
            //envoie chaque message
            for (String s : listMessageAtest) {
                try {
                    String stringRecu=in.readLine();
                    //creation du message
                   assertEquals(stringRecu,s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
