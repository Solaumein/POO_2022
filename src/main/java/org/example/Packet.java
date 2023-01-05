package org.example;

import java.net.InetAddress;

public class Packet {
        State.state state;
        InetAddress addr;
        int portcomtcp;
        String pseudo;

    public String toString(Packet packet){
        String str = (packet.pseudo + "," + packet.addr.toString() + "," + packet.portcomtcp + "," + packet.state.toString());
        return str;
    }
}
