package org.example.Network;

import org.example.User.ListContact;

import java.io.IOException;
import java.net.*;

public class NetworkManagerUDP {

    private static final NetworkManagerUDP instance = new NetworkManagerUDP();

    public static NetworkManagerUDP getInstance() {
        return instance;
    }

    private NetworkManagerUDP(){   }

   /* public synchronized boolean Notify(State.state s){
        return true;
    }
    public synchronized boolean Answer(State.state s){
        return true;
    }
*/
    static final int portUDP=1111;
    public synchronized Packet listenNotify(DatagramSocket ds) {
        //toDo ignorer le packet s'il vient de moi (car broadcast me l'envoie aussi)
        try {
            Packet packet = new Packet();
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, portUDP);
            ds.receive(dp); //a garder ici
            //ds.close();
            String data = new String(dp.getData(), 0, dp.getLength());
            String[] packetstr = data.split(",");
            packet.pseudo = packetstr[0];
            String parsedAddr=dp.getAddress().toString().split("/")[1];
            System.out.println();
            packet.addr = InetAddress.getByName(parsedAddr) ;// InetAddress.getByName(packetstr[1].split("/")[1]);
            packet.portcomtcp = Integer.parseInt(packetstr[2]);
            packet.state = State.stringToState(packetstr[3]);
            System.out.println(packet);

            return packet;
        } catch (IOException e) {

            throw new RuntimeException(e);
            //e.printStackTrace();
            //return new Packet();
        }
    }

    public synchronized boolean sendNotify(State.state state){
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
            String data = packet.toString();
            System.out.println(data);
            InetAddress ip = null;
            try {
                ip = InetAddress.getByName("255.255.255.255");
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, portUDP);
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

    public synchronized boolean sendAnswer(State.state state, InetAddress addr ){
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
            String data = packet.toString();
            System.out.println(data);
            InetAddress ip = null;
            ip = addr;

            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, portUDP);//port par defaut de l'application (car pas de communication avant udp)
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

    public synchronized State.state listenAnswer(){
        return State.state.VALIDPSEUDO;
    }

}
