package org.example;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
    private UserAddress userAddress;
    private String pseudo;

    private boolean connected;

    public User(UserAddress userAddress, String pseudo) {
        this.userAddress = userAddress;
        this.pseudo = pseudo;
        this.connected = false;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUserAddress().equals(user.getUserAddress()) && getPseudo().equals(user.getPseudo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserAddress(), getPseudo());
    }
}
