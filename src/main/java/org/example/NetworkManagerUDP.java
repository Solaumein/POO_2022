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
    }

    boolean Notify(State.state s){
        return true;
    }
    boolean Answer(State.state s){
        return true;
    }

    public Packet listenNotify() {
        try {
            System.out.println("Hello World!");
            Packet packet = new Packet();
            System.out.println("Hello World!22222222");
            DatagramSocket ds = new DatagramSocket(1024);
            System.out.println("Hello World!3333333333333333");
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            ds.receive(dp);
            ds.close();
            String data = new String(dp.getData(), 0, dp.getLength());
            String[] packetstr = data.split(",");
            packet.pseudo = packetstr[0];
            packet.addr = InetAddress.getByName(packetstr[1]);
            packet.portcomtcp = Integer.valueOf(packetstr[2]);
            packet.state = State.stringToState(packetstr[3]);

            return packet;
        } catch (IOException e) {

             throw new RuntimeException(e);
        }
    }

    boolean notify(State.state state){
        System.out.println("notify");
        DatagramSocket ds = null;
        System.out.println("notify2");
        try {
            ds = new DatagramSocket();
            System.out.println("notify3");
            Packet packet = new Packet();
            System.out.println("notify4");
            System.out.println("notify411");
            try {
                System.out.println("notify49");
                packet.addr = InetAddress.getByName("localhost");
                System.out.println("notify5");
            } catch (Exception e) {
                System.out.println("Toto");
                System.err.println(e.getMessage());
                //throw new RuntimeException(e);
            }
            System.out.println("maybe");
            //toDo  determiner un port
            packet.portcomtcp = 11111;

            //toDO  getpseudo user local
            packet.pseudo ="PseudoTest";
            String data = packetToString(packet);
            InetAddress ip = null;
            System.out.println("notify6");
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

            System.out.println("notify end");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    State.state listenAnswer(){
        return State.state.VALIDPSEUDO;
    }

}
