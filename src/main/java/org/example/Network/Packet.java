package org.example.Network;

import java.net.InetAddress;

public class Packet {
    private State.state state;
    private InetAddress addr;
    private int portcomtcp;
    private String pseudo;

    public void setState(State.state state) {
        this.state = state;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    public void setPortcomtcp(int portcomtcp) {
        this.portcomtcp = portcomtcp;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public State.state getState() {
        return state;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public int getPortcomtcp() {
        return portcomtcp;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String toString(){
        return (this.pseudo + "," + this.addr.toString() + "," + this.portcomtcp + "," + this.state.toString());
    }
}
