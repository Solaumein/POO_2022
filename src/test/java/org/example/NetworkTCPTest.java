package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.Exception.ThreadComNotFoundException;

import java.net.Socket;

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
    public void testApp()
    {
        ThreadManager threadManager=new ThreadManager();
       NetworkManagerTCP ntcp=new NetworkManagerTCP();
       ntcp.start();
       while (ntcp.listSocket.isEmpty()){
           System.out.println("pas de sock");
           try {
               Thread.sleep(4000);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }
       Socket s= ntcp.listSocket.get(0);
        try {
            ThreadCom tc= threadManager.getThreadFromIP(s.getInetAddress());
            String msgRecu =tc.receive();
            System.out.println("on a recu "+msgRecu);
            assertEquals("bonjour", msgRecu);
            tc.close();
        } catch (ThreadComNotFoundException e) {
            System.out.println("pb de co avec socket "+s.getInetAddress());
            fail();
        }
        System.out.println("test "+s);

    }

}
