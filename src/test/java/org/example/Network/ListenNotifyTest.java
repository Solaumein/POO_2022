package org.example.Network;

import junit.framework.TestCase;
import org.example.Network.ThreadComUDP;

import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class ListenNotifyTest extends TestCase {
    public void testListenNotify() throws InterruptedException {

        ThreadComUDP thread1 = new ThreadComUDP(invalidPseudoCallback);
        thread1.start();

        thread1.join();
        assertTrue(true);
    }
    Consumer<String> invalidPseudoCallback= s -> fail();
}
