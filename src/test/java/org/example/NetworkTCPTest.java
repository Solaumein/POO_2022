package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.Exception.ThreadComNotFoundException;

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
        listMessageAtest.add("bonjour");
        //listMessageAtest.add("aie aie aie\n plusieurs ligne");
        listMessageAtest.add("");
        //listMessageAtest.add("\n");
    }

    public void testApp()
    {
        initListMSG();

        Thread testTCP=new Thread(NetworkTCPTest::launchSend);//lance le thread d'envoi des messages
        testTCP.start();

        //Init de la partie RZO
        ThreadManager threadManager=new ThreadManager();
        NetworkManagerTCP ntcp=new NetworkManagerTCP();

        ntcp.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (ntcp.listSocket.isEmpty()){//attend la creation du socket
            System.out.println("pas de sock");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
       Socket s= ntcp.listSocket.get(0);//recupere le premier socket(le seul)
        try {
            ThreadCom tc= threadManager.getThreadFromIP(s.getInetAddress());
            for (String s1 : listMessageAtest) {
                testReception(tc,s1,InetAddress.getLocalHost());
            }
            tc.close();
        } catch (ThreadComNotFoundException e) {
            System.out.println("pb de co avec socket " + s.getInetAddress());
            fail();
        } catch (UnknownHostException e) {
            System.out.println("pb de getlocalHost");
            e.printStackTrace();
            fail();
        }
    }


    private static void launchSend() {
        //init partie
        ThreadManager threadManager=new ThreadManager();
        NetworkManagerTCP ntcp=new NetworkManagerTCP();
        UserAddress userAddress;
        try {
            userAddress= new UserAddress(InetAddress.getLocalHost(), 42069);
        }catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        User u=new User(userAddress,"oui");
        ntcp.connect(u);
        Socket socket= ntcp.listSocket.get(0);
        try {
            ThreadCom tc= threadManager.getThreadFromIP(socket.getInetAddress());
            for (String s : listMessageAtest) {
                tc.send(s);
            }
            assertTrue(true);
        } catch (ThreadComNotFoundException e) {
            System.out.println("pb de co avec socket "+socket.getInetAddress()+ " list "+threadManager.getListThread());
            e.printStackTrace();
            fail();
        }

    }

    void testReception(ThreadCom tc,String expectedString,InetAddress inetExpected){
        String msgRecu =tc.receive();
        Message message=new Message(msgRecu);
        System.out.println("on a recu "+message);
        assertEquals(expectedString, msgRecu);
        assertEquals(tc.sockCom.getInetAddress(),inetExpected);
    }
}
