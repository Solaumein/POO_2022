package org.example;

public class ThreadCom extends Thread {
    static int i =0;
    ThreadCom(String ip){
        super("thread"+i);
        i++;
        this.start();

    }

    @Override
    public void run() {
        super.run();
    }
}
