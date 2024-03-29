package org.example.Network;

import junit.framework.TestCase;
import org.example.Exception.ThreadNotFoundException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadManagerTest extends TestCase {
    private Socket socket = null;
    private Communication communication;
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
    MessageReceivedHandler messageReceivedHandler= (message, address) -> {};


    public void testThread(){//test tous mis les uns après les autres pour eviter les tests jenkins qui se font en thread pour eviter les acces concurrents aux listes
        GetThreadFromIPtest();
        setUp();
        CreateAndKilltest();
        resetAll();
    }

    private void resetAll() {
        ThreadManager.getInstance().reset();
        NetworkManagerTCP.getInstance().reset();
    }




    public void GetThreadFromIPtest(){
        System.out.println("list Thread "+ThreadManager.getInstance().getListThread());
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
            ThreadManager.getInstance().getThreadSendFromName(InetAddress.getLocalHost().toString());
            ThreadManager.getInstance().getThreadListenFromName(InetAddress.getLocalHost().toString());
            assert false;//n'est pas censé les trouver
        } catch (ThreadNotFoundException ignored) {
        } catch (UnknownHostException e) {
            fail();
        }
        System.out.println(ThreadManager.getInstance().getListThread());
        ThreadManager.getInstance().killAllThread();
        System.out.println(ThreadManager.getInstance().getListThread());
    }
    private void CreateAndKilltest(){
        System.out.println(ThreadManager.getInstance().getListThread());

        assertEquals(2,ThreadManager.getInstance().getListThread().size());

        try {
            NetworkManagerTCP.getInstance().setMessageReceivedHandler(messageReceivedHandler);
            NetworkManagerTCP.getInstance().killCommunication(communication);
            assertEquals(0,ThreadManager.getInstance().getListThread().size());
        } catch (ThreadNotFoundException e) {
            fail();
        }
    }
}
