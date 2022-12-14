package org.example;

import org.example.Exception.ThreadComNotFoundException;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadManager {

    private ArrayList<ThreadCom> listThreadCom=new ArrayList<>();
    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }
    private ThreadManager() {}
    ArrayList<ThreadCom> getListThread(){
        return listThreadCom;
    }

    public synchronized void createThreadCommunication(Socket socket){
       ThreadCom threadCom = new ThreadCom(socket);
       //threadCom.start();todo a remettre plus tard
        listThreadCom.add(threadCom);
    }

    public synchronized ThreadCom getThreadFromIP(InetAddress ip) throws ThreadComNotFoundException{
        for (ThreadCom threadCom : listThreadCom) {
            System.out.println(threadCom);
            if(ip.toString().equals(threadCom.getName())) return threadCom;
        }
        throw new ThreadComNotFoundException();
    }

}
