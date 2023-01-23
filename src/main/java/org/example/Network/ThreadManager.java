package org.example.Network;

import org.example.Exception.ThreadNotFoundException;

import java.net.Socket;
import java.util.ArrayList;

public class ThreadManager {

    private ArrayList<Thread> listThread =new ArrayList<>();
    private static final ThreadManager instance = new ThreadManager();

    public static final String endNameListen="/Listen";
    public static final String endNameSend="/Send";
    public static ThreadManager getInstance() {
        return instance;
    }
    private ThreadManager() {}
    public synchronized ArrayList<Thread> getListThread(){
        return listThread;
    }

    private void setListThread(ArrayList<Thread> listThread) {
        this.listThread = listThread;
    }

    public synchronized void reset(){
        ThreadManager.getInstance().killAllThread();
        ThreadManager.getInstance().setListThread(new ArrayList<>());
    }
    public synchronized void addThread(Thread thread){
        this.listThread.add(thread);
    }
    public synchronized void killThread(Thread thread) throws ThreadNotFoundException {
        thread.interrupt();
        if(!this.getListThread().remove(thread)) throw new ThreadNotFoundException();
    }

    public synchronized Thread getThreadFromName(String name) throws ThreadNotFoundException {
        for (Thread thread : listThread) {
            if(name.equals(thread.getName())) return thread;
        }
        throw new ThreadNotFoundException();
    }

    public synchronized Thread getThreadSendFromName(String name) throws ThreadNotFoundException {
        String nameThread=name+endNameSend;
        for (Thread thread : listThread) {
            if(nameThread.equals(thread.getName())) return thread;
        }
        throw new ThreadNotFoundException();
    }
    public synchronized Thread getThreadListenFromName(String name) throws ThreadNotFoundException {
        String nameThread=name+endNameListen;
        for (Thread thread : listThread) {
            try {
                if(nameThread.equals(thread.getName())) return thread;
            }catch (ClassCastException ignore){}
        }
        throw new ThreadNotFoundException();
    }

    public synchronized Communication createThreadCommunication(Socket socket, MessageReceivedHandler messageReceivedHandler) {//todo demander au prof si c bon
        SendMessageTCPThread sendMessageTCPThread= new SendMessageTCPThread(socket);
        ListenMessageTCPThread listenMessageTCPThread=new ListenMessageTCPThread(socket,messageReceivedHandler);
        Communication communicationCreated=new Communication(listenMessageTCPThread,sendMessageTCPThread);
        this.listThread.add(sendMessageTCPThread);
        this.listThread.add(listenMessageTCPThread);
        sendMessageTCPThread.start();
        listenMessageTCPThread.start();
        return communicationCreated;
    }

    public synchronized void killAllThread()  {
        //System.out.println("on doit kill "+getListThread().size()+" threads : "+getListThread());
        ArrayList<Thread> listThread= new ArrayList<>(getListThread());
        for (Thread thread : listThread) {
          //  System.out.println("on kill lui "+thread+" list "+getListThread());
            try {
                killThread(thread);
                 } catch (ThreadNotFoundException e) {
                System.out.println("le thread "+thread+" n'a pas pu etre arreter car il n'existe pas");
            }
            //System.out.println("on l'a kill "+getListThread());
        }
        //System.out.println("fini de kill");
    }
}
