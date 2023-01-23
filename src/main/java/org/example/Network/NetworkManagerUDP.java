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
        //toDo peut etre scindé en plusieurs fonctions et aussi regrouper avec sendNotify (14ligne duplicated askip)

        try {
            Packet packet = new Packet();
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, portUDP);
            ds.receive(dp); //a garder ici
            //ds.close();
            String data = new String(dp.getData(), 0, dp.getLength());
            String[] packetstr = data.split(",");//todo check if virgule dans pseudo
            packet.setPseudo(packetstr[0]);
            String parsedAddr=dp.getAddress().toString().split("/")[1];
            System.out.println();
            packet.setAddr(InetAddress.getByName(parsedAddr)) ;// InetAddress.getByName(packetstr[1].split("/")[1]);
            packet.setPortcomtcp(Integer.parseInt(packetstr[2]));
            packet.setState(State.stringToState(packetstr[3]));
            System.out.println(packet);

            return packet;
        } catch (IOException e) {
            throw new RuntimeException(e);
            //e.printStackTrace();
            //return new Packet();
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            System.out.println("on a recu un paquet malformé " );
            return null;//todo a changer peut etre
        }
    }

    public void sendNotify(State.state state){
        System.out.println("sendnotify");
        DatagramSocket ds ;
        try {
            ds = new DatagramSocket();
            Packet packet = new Packet();
            try {
                packet.setAddr(InetAddress.getLocalHost()) ;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //throw new RuntimeException(e);
            }
            packet.setPortcomtcp(ListContact.selfUser.getUserAddress().getPort());

            packet.setPseudo(ListContact.selfUser.getPseudo()) ;
            packet.setState(state);
            String data = packet.toString();
            System.out.println(data);
            InetAddress ip;
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

    }

    public void sendAnswer(State.state state, InetAddress addr ){
        System.out.println("sendanswer");
        DatagramSocket ds;
        try {
            ds = new DatagramSocket();
            Packet packet = new Packet();
            try {
                packet.setAddr(InetAddress.getLocalHost());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //throw new RuntimeException(e);
            }
            packet.setPortcomtcp(ListContact.selfUser.getUserAddress().getPort());
            packet.setPseudo(ListContact.selfUser.getPseudo());
            packet.setState(state);
            String data = packet.toString();
            System.out.println(data);

            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), addr, portUDP);//port par defaut de l'application (car pas de communication avant udp)
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

    }

}
