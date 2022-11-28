package org.example;

import org.example.Exception.ThreadComNotFoundException;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadManager {

    static ArrayList<ThreadCom> listThreadCom;
    ThreadManager(){listThreadCom=new ArrayList<>();}

    ArrayList<ThreadCom> getListThread(){
        return listThreadCom;
    }

    static void createThreadCommunication(Socket socket){
       ThreadCom threadCom = new ThreadCom(socket);
        listThreadCom.add(threadCom);
    }

    ThreadCom getThreadFromIP(InetAddress ip) throws ThreadComNotFoundException{
        for (ThreadCom threadCom : listThreadCom) {
            System.out.println(threadCom);
            if(ip.toString().equals(threadCom.getName())) return threadCom;
        }
        throw new ThreadComNotFoundException();
    }


}
