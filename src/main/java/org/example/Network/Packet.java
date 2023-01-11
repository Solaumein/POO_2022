package org.example.Network;

import java.net.InetAddress;

public class Packet {
        State.state state;
        InetAddress addr;
        int portcomtcp;
        String pseudo;

    public String toString(){
        String str = (this.pseudo + "," + this.addr.toString() + "," + this.portcomtcp + "," + this.state.toString());
        return str;
    }
}
