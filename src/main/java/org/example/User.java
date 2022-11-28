package org.example;

import java.io.Serializable;

public class User implements Serializable{
    private UserAddress userAddress;
    private String pseudo;

    public User(UserAddress userAddress, String pseudo) {
        this.userAddress = userAddress;
        this.pseudo = pseudo;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }


}
