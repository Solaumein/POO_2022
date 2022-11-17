package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class App
{

    public static void main( String[] args ) {
        System.out.println( "args : "+args[0]+" port "+args[1] );
        int port= Integer.parseInt(args[1]);
        String name=args[0];
        if(name.equals("clientSend")){
            System.out.println("je suis client qui envoie");
            Socket sock= null;
            try {
                DatagramSocket ds = new DatagramSocket();
                String str = "bonjour";
                InetAddress ip = InetAddress.getByName("192.168.252.255");

                DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, port);
                ds.send(dp);
                ds.close();
                /* implementation enTCP sans broadcast
                sock = new Socket(InetAddress.getLocalHost(),port);
                sock = new Socket("192.168.252.198" ,port);
                System.out.println(name +": on va lancer le connect");
                PrintWriter outpout=new PrintWriter(sock.getOutputStream(), true);
                System.out.println(name+": on s'est co");
                String s="bonjour";//String.valueOf(InetAddress.getLocalHost());
                System.out.println(name+ ": on va lancer le write du message "+s);
                outpout.println(s);
                System.out.println(name+": on a lancer un message "+s);
                sock.close();*/
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        if(args[0].equals("clientReceive")){
            System.out.println("je suis client qui recoit");
            try {
                DatagramSocket ds = new DatagramSocket(port);
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                ds.receive(dp);
                String str = new String(dp.getData(), 0, dp.getLength());
                System.out.println(str);
                ds.close();
                /* en TCP ya pas de broadcast
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println(name+": on s'est co");
                System.out.println(name+ ": on va lancer le read");
                String res= input.readLine();
                System.out.println(name+": on a recu un message "+res);*/
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

}
