package org.example;

import junit.framework.TestCase;

import static java.lang.Thread.sleep;

public class ListenNotifyTest extends TestCase {
    public void testListenNotify() throws InterruptedException {
        ThreadComUDP  thread1 = new ThreadComUDP(ThreadComUDP.UdpIntent.LISTEN);
        thread1.join();
        //NetworkManagerUDP manageUDP = new NetworkManagerUDP();
        //manageUDP.listenNotify();

    }
}
