package org.example.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class User  {
    private UserAddress userAddress;
    private String pseudo;
    private boolean connected;
    private Node node;

    public User(UserAddress userAddress, String pseudo) {
        this.userAddress = userAddress;
        this.pseudo = pseudo;
        this.connected = false;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ContactFrame.fxml"));

        System.out.println((String) loader.getRoot());
        try {
            loader.load();
            this.node = (Node) loader.getNamespace().get("contactFrame");
            Label label = (Label) this.node.lookup("#pseudoUser");
            label.setText(this.pseudo);
        } catch (IOException e) {
            System.out.println("erreur loader");
        }
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
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
    public String toString() {
        return "User{" +
                "userAddress=" + userAddress +
                ", pseudo='" + pseudo + '\'' +
                ", connected=" + connected +
                ", node=" + node +
                '}';
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

    public Node getNode() {
        return node;
    }
}

