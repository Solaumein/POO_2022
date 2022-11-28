package org.example;

import junit.framework.TestCase;

public class NotifyTest extends TestCase {
    public void testNotify(){
        ThreadComUDP thread2 = new ThreadComUDP(ThreadComUDP.UdpIntent.NOTIFY);
    }

}
