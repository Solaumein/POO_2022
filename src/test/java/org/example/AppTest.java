package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        int port=12345;
        new Thread(new Runnable() {//thread du accept
            @Override
            public void run() {
                try {
                    UserAddress uAddr = new UserAddress(InetAddress.getLocalHost(),port);
                    User u=new User(uAddr,"toto");
                    NetworkManagerTCP tcp=new NetworkManagerTCP();
                    assertTrue( tcp.start(u,true));
                    assertTrue( tcp.receive().equals("coucou"));
                } catch (UnknownHostException e){
                    e.printStackTrace();
                    fail();
                    throw new RuntimeException(e);
                }
            }
        }).start();
        try {
            UserAddress uAddr = new UserAddress(InetAddress.getLocalHost(),port);
            User u=new User(uAddr,"toto");
            NetworkManagerTCP tcp=new NetworkManagerTCP();
            assertTrue( tcp.start(u,false));
            tcp.send("coucou");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail();
            throw new RuntimeException(e);
        }
    }
}
