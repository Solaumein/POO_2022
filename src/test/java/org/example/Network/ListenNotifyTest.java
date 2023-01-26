package org.example.Network;

import junit.framework.TestCase;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class ListenNotifyTest extends TestCase {
    public void testListenNotify() {

        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback,validPseudoCallback, null);
        thread1.start();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            fail();
        }
        thread1.interrupt();
        assertTrue(true);
    }
    Consumer<String> invalidPseudoCallback= s -> fail();
    Consumer<Packet> validPseudoCallback= s->assertTrue(true);
}
