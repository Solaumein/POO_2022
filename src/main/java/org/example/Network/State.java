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
       /* switch(str){
            case "CONNECTION":
                return state.CONNECTION;
            case "CHANGEPSEUDO":
                return state.CHANGEPSEUDO;
            case "DECONNECTION":
                return state.DECONNECTION;
            case "VALIDPSEUDO":
                return state.VALIDPSEUDO;
            case "INVALIDPSEUDO":
                return state.INVALIDPSEUDO;
            default :
                return null;
        }*/
    }
}
