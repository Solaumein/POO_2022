package org.example.Network;

import org.example.User.User;

import java.net.InetAddress;

public interface MessageReceivedHandler {

    void newMessageArrivedFromAddr(String message, InetAddress address);
}
