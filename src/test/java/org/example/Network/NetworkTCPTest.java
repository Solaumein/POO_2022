package org.example.Network;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.Exception.ThreadComNotFoundException;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class NetworkTCPTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NetworkTCPTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( NetworkTCPTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    static ArrayList<String> listMessageAtest=new ArrayList<>();;
    private void initListMSG(){
        listMessageAtest.add("bonjour");//message normale
        //listMessageAtest.add("aie aie aie\n plusieurs ligne");//todo a tester quand on delimittera a la main les message avecv de cara de valeur ascii 0
        //listMessageAtest.add("\n");
        listMessageAtest.add("");//message vide a tester
        listMessageAtest.add("é#яйца");//ascii compliqué
    }

    public void testApp()
    {
        System.out.println("on commence le test de networkTCP");
        initListMSG();
        System.out.println("on a init les MSG");

        Thread testTCP=new Thread(NetworkTCPTest::launchSend);//lance le thread d'envoi des messages
        testTCP.start();
        System.out.println("on a lancé le thread de launch");

        //Init de la partie RZO
        ThreadManager threadManager=ThreadManager.getInstance();
        NetworkManagerTCP ntcp=NetworkManagerTCP.getInstance();
        ntcp.start();
        System.out.println("on a lancer le networkmanagerTCP");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("on a attendu 100 ms");

        while (ntcp.getListSocket().isEmpty()){//attend la creation du socket
            System.out.println("pas de sock");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("la liste de sock n'est plus vide");

        Socket s= ntcp.getListSocket().get(0);//recupere le premier socket(le seul)
        try {
            ThreadCom tc= threadManager.getThreadFromIP(s.getInetAddress());
            for (String s1 : listMessageAtest) {
                testReception(tc,s1,InetAddress.getLocalHost());
            }
            tc.close();
        } catch (ThreadComNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (UnknownHostException e) {
            System.out.println("pb de getlocalHost");
            e.printStackTrace();
            fail();
        }
    }


    private static void launchSend() {//simule la co et l'envoie en TCP d'un autre utilisateur
        try {
            InetAddress hostname= InetAddress.getLocalHost();
            Socket socket =  new Socket(hostname, 42069);
            PrintWriter out;
            out = new PrintWriter(socket.getOutputStream(), true);
            //envoie chaque message
            for (String s : listMessageAtest) {
                out.println(s);
            }
        } catch (Exception e) {
           e.printStackTrace();
           fail();
        }
    }

    void testReception(ThreadCom tc,String expectedString,InetAddress inetExpected){
        String msgRecu =tc.receive();
        //Message message=new Message(msgRecu);
        System.out.println("on a recu "+msgRecu);
        assertEquals(expectedString, msgRecu);
        assertEquals(tc.sockCom.getInetAddress(),inetExpected);
    }
}
