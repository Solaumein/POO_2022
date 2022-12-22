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
        String str = (packet.pseudo + "," + packet.addr.toString() + "," + packet.portcomtcp + "," + packet.state.toString());
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

    public Packet listenNotify(DatagramSocket ds) {
        //toDo ignorer le packet s'il vient de moi (car broadcast me l'envoie aussi)
        try {
            Packet packet = new Packet();
            //DatagramSocket ds = new DatagramSocket(1024);
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            ds.receive(dp); //a garder ici
            //ds.close();
            String data = new String(dp.getData(), 0, dp.getLength());
            String[] packetstr = data.split(",");
            packet.pseudo = packetstr[0];
            packet.addr = InetAddress.getByName(packetstr[1].split("/")[1]);
            packet.portcomtcp = Integer.valueOf(packetstr[2]);
            packet.state = State.stringToState(packetstr[3]);
            System.out.println(packetToString(packet));

            return packet;
        } catch (IOException e) {

            throw new RuntimeException(e);
            //e.printStackTrace();
            //return new Packet();
        }
    }

    boolean sendnotify(State.state state){
        System.out.println("sendnotify");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            Packet packet = new Packet();
            try {
                packet.addr = InetAddress.getLocalHost();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //throw new RuntimeException(e);
            }
            packet.portcomtcp = ListContact.selfUser.getUserAddress().getPort();

            packet.pseudo = ListContact.selfUser.getPseudo();
            packet.state = state;
            String data = packetToString(packet);
            System.out.println(data);
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

            System.out.println("sendnotify end");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    boolean sendanswer(State.state state, InetAddress addr ){
        System.out.println("sendanswer");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            Packet packet = new Packet();
            try {
                packet.addr = InetAddress.getLocalHost();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //throw new RuntimeException(e);
            }
            packet.portcomtcp = ListContact.selfUser.getUserAddress().getPort();

            packet.pseudo = ListContact.selfUser.getPseudo();
            packet.state = state;
            String data = packetToString(packet);
            System.out.println(data);
            InetAddress ip = null;
            ip = addr;

            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, 1024);//todo pk 1024
            try {
                ds.send(dp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ds.close();

            System.out.println("sendanswer end");
        }catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    State.state listenAnswer(){
        return State.state.VALIDPSEUDO;
    }

}
