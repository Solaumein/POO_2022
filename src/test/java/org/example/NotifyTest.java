package org.example;

import junit.framework.TestCase;

import static java.lang.Thread.sleep;

public class NotifyTest extends TestCase {
    public void testNotify() throws InterruptedException {
        ThreadComUDP thread2 = new ThreadComUDP(ThreadComUDP.UdpIntent.NOTIFY);
        thread2.join();
        //NetworkManagerUDP  manageUDP = new NetworkManagerUDP();
        //manageUDP.sendnotify(State.state.CONNECTION);
    }

}
