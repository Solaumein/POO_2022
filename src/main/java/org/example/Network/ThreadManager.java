package org.example.Network;

import org.example.Exception.ThreadNotFoundException;

import java.net.Socket;
import java.util.ArrayList;

public class ThreadManager {

    private ArrayList<Thread> listThread =new ArrayList<>();
    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }
    private ThreadManager() {}
    public synchronized ArrayList<Thread> getListThread(){
        return listThread;
    }

    public synchronized void addThread(Thread thread){
        this.listThread.add(thread);
    }
    public synchronized void killThread(Thread thread){
        try {
            Thread threadSaved= this.getThreadFromName(thread.getName());
            threadSaved.interrupt();
            this.listThread.remove(threadSaved);
        } catch (ThreadNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Thread getThreadFromName(String name) throws ThreadNotFoundException {
        for (Thread thread : listThread) {
            if(name.equals(thread.getName())) return thread;
        }
        throw new ThreadNotFoundException();
    }

    public synchronized ThreadCom createThreadCommunication(Socket socket) {//todo demander au prof si c bon
        ThreadCom threadCom= new ThreadCom(socket);
        this.listThread.add(threadCom);
        threadCom.start();
        return threadCom;
    }
}
