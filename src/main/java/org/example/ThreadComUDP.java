package org.example;


public class ThreadComUDP extends Thread {
    static int i =0;
    UdpIntent intent;
    enum UdpIntent{
        LISTEN,
        NOTIFY
    }
    ThreadComUDP(UdpIntent intent){
        super("thread"+i);
        i++;
        this.intent = intent;
        this.start();
    }
    private void listentIntent() {
        NetworkManagerUDP managerUDP = new NetworkManagerUDP();
        while (true) {
            NetworkManagerUDP.Packet info = managerUDP.listenNotify();
            // Son pseudo = mon pseudo ??
            //toDO Cas où timeout : on retourne au debut du while
            //toDO AddContact
            //toDo answer(reponse) où reponse contient mon pseudo, mon port dédié pour tcp, state (ValidPseudo) or InvalidPseudo

        }
    }

    private void notifyIntent(){

    }

    @Override
    public void run() {
        super.run();
        if(this.intent == UdpIntent.LISTEN){
            listentIntent();
        }
        else if(this.intent == UdpIntent.NOTIFY){
            notifyIntent();
        }
        }

}
