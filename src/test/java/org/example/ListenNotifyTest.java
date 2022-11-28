package org.example;

import junit.framework.TestCase;

public class ListenNotifyTest extends TestCase {
    public void testListenNotify(){
        ThreadComUDP  thread1 = new ThreadComUDP(ThreadComUDP.UdpIntent.LISTEN);
    }
}
