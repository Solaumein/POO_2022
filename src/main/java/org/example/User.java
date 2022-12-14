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
