package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
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
                sock = new Socket(InetAddress.getLocalHost(),port);
                System.out.println(name +": on va lancer le connect");
                PrintWriter outpout=new PrintWriter(sock.getOutputStream(), true);
                System.out.println(name+": on s'est co");
                String s= String.valueOf(InetAddress.getLocalHost());
                System.out.println(name+ ": on va lancer le write du message "+s);
                outpout.println(s);
                System.out.println(name+": on a lancer un message "+s);

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        if(args[0].equals("clientReceive")){
            System.out.println("je suis client qui recoit");
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println(name+": on s'est co");
                System.out.println(name+ ": on va lancer le read");
                String res= input.readLine();
                System.out.println(name+": on a recu un message "+res);
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

}
