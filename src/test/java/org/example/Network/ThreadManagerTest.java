package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.ThreadComNotFoundException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class ThreadManagerTest extends TestCase {
    public void testGetThreadFromIP(){
        ThreadManager threadManager =ThreadManager.getInstance();
        Socket socket = null;
        try{
            socket =  new Socket(InetAddress.getByName("google.fr"), 80);
            //System.out.println("c bon sock co");
        }catch (IOException e ) {
            e.printStackTrace();
        }

        try {
            ThreadCom threadCom=new ThreadCom(socket);
            threadManager.getListThread().add(threadCom);
            ThreadCom threadCom1=threadManager.getThreadFromIP(socket.getInetAddress());
            assert(threadCom.equals(threadCom1));
        } catch (ThreadComNotFoundException e ) {
            fail();
        }
        try {
            ThreadCom threadCom=new ThreadCom(socket);
            threadManager.getListThread().add(threadCom);
            ThreadCom threadCom1=threadManager.getThreadFromIP(InetAddress.getLocalHost());
            assert false;
        } catch (ThreadComNotFoundException e ) {
            assert true;
        } catch (UnknownHostException e) {
            fail();
        }
    }
}
