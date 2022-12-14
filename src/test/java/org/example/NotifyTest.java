package org.example;

import junit.framework.Assert;
import junit.framework.TestCase;

import static java.lang.Thread.sleep;

public class NotifyTest extends TestCase {
    public void testNotify() throws InterruptedException {
        NetworkManagerUDP managerUDP = new NetworkManagerUDP();
        for (int i = 0; i < 1; i++) {
            managerUDP.sendnotify(State.state.CONNECTION);
            assertTrue(true);
            //NetworkManagerUDP  manageUDP = new NetworkManagerUDP();
            //manageUDP.sendnotify(State.state.CONNECTION);
        }

    }
}
