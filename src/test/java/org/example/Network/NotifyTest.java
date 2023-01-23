package org.example.Network;

import junit.framework.TestCase;

public class NotifyTest extends TestCase {
    public void testNotify()  {
        for (int i = 0; i < 1; i++) {
            NetworkManagerUDP.getInstance().sendNotify(State.state.CONNECTION);
            assertTrue(true);
            //NetworkManagerUDP  manageUDP = new NetworkManagerUDP();
            //manageUDP.sendnotify(State.state.CONNECTION);
        }
    }
}
