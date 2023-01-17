package org.example.Network;

import java.net.InetAddress;

public interface MessageReceivedHandler {
    void newMessageArrivedFromAddr(String message, InetAddress address);
}
