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
    Communication communication;
    @Override
    protected void setUp()  {//est executé avant chaque test (evite redondance de code)
        try{
            socket =  new Socket(InetAddress.getByName("google.fr"), 80);
            //System.out.println("c bon sock co");
        }catch (IOException e ) {
           fail();
        }
         communication= ThreadManager.getInstance().createThreadCommunication(socket,messageReceivedHandler);
    }
    MessageReceivedHandler messageReceivedHandler=new MessageReceivedHandler() {
        @Override
        public void newMessageArrivedFromAddr(String message) {
            }
    };
    public void testGetThreadFromIP(){

        assertEquals(ThreadManager.getInstance().getListThread().size(),2);//2 car ecoute et envoie
        try {
            ListenMessageTCPThread listenMessageTCPThread= (ListenMessageTCPThread) ThreadManager.getInstance().getThreadListenFromName(socket.getInetAddress().toString());
            SendMessageTCPThread sendMessageTCPThread=(SendMessageTCPThread) ThreadManager.getInstance().getThreadSendFromName(socket.getInetAddress().toString());
            assert(communication.getListenMessageTCPThread().equals(listenMessageTCPThread));
            assert(communication.getSendMessageTCPThread().equals(sendMessageTCPThread));
        } catch (ThreadNotFoundException e ) {
            fail();
        }
        try {
            SendMessageTCPThread threadCom1=(SendMessageTCPThread) ThreadManager.getInstance().getThreadSendFromName(InetAddress.getLocalHost().toString());
            ListenMessageTCPThread threadCom2=(ListenMessageTCPThread) ThreadManager.getInstance().getThreadListenFromName(InetAddress.getLocalHost().toString());
            assert false;//n'est pas censé les trouver
        } catch (ThreadNotFoundException ignored) {
        } catch (UnknownHostException e) {
            fail();
        }
        System.out.println(ThreadManager.getInstance().getListThread());
        ThreadManager.getInstance().killAllThread();
        System.out.println(ThreadManager.getInstance().getListThread());
    }
    public void testCreateAndKill(){
        System.out.println(ThreadManager.getInstance().getListThread());

        assertEquals(2,ThreadManager.getInstance().getListThread().size());

        try {
            NetworkManagerTCP.setMessageReceivedHandler(messageReceivedHandler);
            NetworkManagerTCP.getInstance().killCommunication(communication);
            assertEquals(0,ThreadManager.getInstance().getListThread().size());
        } catch (ThreadNotFoundException e) {
            fail();
        }
    }
}
