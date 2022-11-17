package org.example;

public class State {
    enum StateNotify {
        CONNECTION,
        CHANGEPSEUDO,
        DECONNECTION
    }
    enum StateAnswer {
        VALIDPSEUDO,
        INVALIDPSEUDO
    }

}
