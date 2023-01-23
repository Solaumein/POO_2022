package org.example.Network;

import junit.framework.TestCase;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class ListenNotifyTest extends TestCase {
    public void testListenNotify() throws InterruptedException {

        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback,validPseudoCallback);
        thread1.start();

        sleep(5000);
        thread1.interrupt();
        assertTrue(true);
    }
    Consumer<String> invalidPseudoCallback= s -> fail();
    Consumer<Packet> validPseudoCallback= s->assertTrue(true);
}
