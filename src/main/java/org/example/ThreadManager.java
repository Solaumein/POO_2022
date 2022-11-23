package org.example;

import java.util.ArrayList;

public class ThreadManager {

    ArrayList<ThreadCom> listThreadCom;
    ArrayList<ThreadCom> getListThread(){
        return listThreadCom;
    }
    void createThreadCommunication(String ip){
       ThreadCom threadCom = new ThreadCom(ip);
        listThreadCom.add(threadCom);
    }

}
