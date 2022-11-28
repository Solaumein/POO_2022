package org.example;

import org.example.Exception.ThreadComNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class App
{

    public static void main( String[] args ) {

        ThreadManager threadManager=new ThreadManager();
        NetworkManagerTCP ntcp=new NetworkManagerTCP();

        //ntcp.start();
        UserAddress userAddress;
        try {
            userAddress= new UserAddress(InetAddress.getLocalHost(), 42069);
        }catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        User u=new User(userAddress,"oui");
        ntcp.connect(u);
        Socket socket= ntcp.listSocket.get(0);
        try {
            ThreadCom tc= threadManager.getThreadFromIP(socket.getInetAddress());
            String msgEnvoyer ="bonjour";
            tc.send(msgEnvoyer);
            tc.close();
            //assertEquals("bonjour", msgRecu);
        } catch (ThreadComNotFoundException e) {

            System.out.println("pb de co avec socket "+socket.getInetAddress()+ " list "+threadManager.getListThread());
            e.printStackTrace();
            //fail();
        }

    }

}
