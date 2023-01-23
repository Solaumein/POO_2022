package org.example.Network;

public class State {
    public enum state {
        CONNECTION,
        CHANGEPSEUDO,
        DECONNECTION,
        VALIDPSEUDO,
        INVALIDPSEUDO
    }

    public static state stringToState(String str) {
        return state.valueOf(str);
    }
}
