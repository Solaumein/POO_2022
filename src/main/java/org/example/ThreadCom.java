package org.example;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadCom extends Thread {
    static int i =0;
    Socket sockCom;
    ThreadCom(Socket s){
        super();
        this.sockCom=s;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        String threadName="";
        try {
            threadName= "thread"+InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            this.interrupt();
        }
        this.setName(threadName);
        //todo faire 2eme thread d'envoie avec send de nouveau port
    }

}
