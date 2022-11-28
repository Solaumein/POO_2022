package org.example;

import java.io.IOException;
import java.net.*;

public class NetworkManagerUDP {
    public class Packet{
        State.state state;
        InetAddress addr;
        int portcomtcp;
        String pseudo;

    }
    public String packetToString(Packet packet){
        String str = (packet.pseudo + "," + packet.addr.toString() + "," + packet.portcomtcp + packet.state.toString());
        return str;
    }
    NetworkManagerUDP(){
        super();
        ThreadComUDP test = new ThreadComUDP(ThreadComUDP.UdpIntent.LISTEN);
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
            DatagramSocket ds = new DatagramSocket(10245);
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            ds.receive(dp);
            String data = new String(dp.getData(), 0, dp.getLength());
            String[] packetstr = data.split(",");
            packet.pseudo = packetstr[0];
            packet.addr = InetAddress.getByName(packetstr[1]);
            packet.portcomtcp = Integer.valueOf(packetstr[2]);
            packet.state = State.stringToState(packetstr[3]);

            return packet;
        }   catch (SocketException e) {

             throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean notify(State.state state){
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            Packet packet = new Packet();
            try {
                packet.addr = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            //toDo  determiner un port
            packet.portcomtcp = 11111;
            //toDO  getpseudo user local
            packet.pseudo ="PseudoTest";
            String data = packetToString(packet);
            InetAddress ip = null;
            try {
                ip = InetAddress.getByName("255.255.255.255");
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, 1024);
            try {
                ds.send(dp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ds.close();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    State.state listenAnswer(){
        return State.state.VALIDPSEUDO;
    }

}
