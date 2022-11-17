package org.example;

public class NetworkManagerUDP extends NetworkManager{
    NetworkManagerUDP(){
        super();
    }

    boolean Notify(State.StateNotify s){
        return true;
    }
    boolean Answer(State.StateAnswer s){
        return true;
    }

    State.StateNotify listenNotify(){
        return State.StateNotify.CONNECTION;
    }
    State.StateAnswer listenAnswer(){
        return State.StateAnswer.VALIDPSEUDO;
    }

}
