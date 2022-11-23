package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class NetworkManagerUDP {
    public class Packet{
        State.state state;
        InetAddress addr;
        int portcomtcp;
        String pseudo;

    }
    NetworkManagerUDP(){
        super();
    }

    boolean Notify(State.state s){
        return true;
    }
    boolean Answer(State.state s){
        return true;
    }

    Packet listenNotify() {
        try {
            Packet packet = new Packet();
            DatagramSocket ds = new DatagramSocket(1024);
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            ds.receive(dp);
            packet.addr = dp.getAddress();
           //toDo packet.portcomtcp = 1111111111;
            //toDo packet.state = ?????????????
            //toDo packet.pseudo = ???????

            return packet;
        }   catch (SocketException e) {

             throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    State.state listenAnswer(){
        return State.state.VALIDPSEUDO;
    }

}
