package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.ThreadNotFoundException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadManagerTest extends TestCase {
    ThreadManager threadManager;
    Socket socket = null;
    ThreadCom threadCom;
    @Override
    protected void setUp()  {//est executé avant chaque test (evite redondance de code)
       threadManager= ThreadManager.getInstance();
        try{
            socket =  new Socket(InetAddress.getByName("google.fr"), 80);
            //System.out.println("c bon sock co");
        }catch (IOException e ) {
           fail();
        }
        threadCom= threadManager.createThreadCommunication(socket);

    }
    public void testGetThreadFromIP(){

        assertEquals(threadManager.getListThread().size(),1);
        try {
            ThreadCom threadCom1=(ThreadCom) threadManager.getThreadFromName(socket.getInetAddress().toString());
            assert(threadCom.equals(threadCom1));
        } catch (ThreadNotFoundException e ) {
            fail();
        }
        try {
            ThreadCom threadCom1=(ThreadCom) threadManager.getThreadFromName(InetAddress.getLocalHost().toString());
            assert false;//n'est pas censé le trouver
        } catch (ThreadNotFoundException ignored) {
        } catch (UnknownHostException e) {
            fail();
        }
    }
    public void testCreateAndKill(){
        assertEquals(threadManager.getListThread().size(),1);
        threadManager.killThread(threadCom);
        assertEquals(threadManager.getListThread().size(),0);
    }
}
