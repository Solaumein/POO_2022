package org.example;

import junit.framework.TestCase;
import org.example.Network.NetworkManagerUDP;
import org.example.Network.State;

import static java.lang.Thread.sleep;

public class NotifyTest extends TestCase {
    public void testNotify() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            NetworkManagerUDP.getInstance().sendNotify(State.state.CONNECTION);
            assertTrue(true);
            //NetworkManagerUDP  manageUDP = new NetworkManagerUDP();
            //manageUDP.sendnotify(State.state.CONNECTION);
        }
    }
}
