package org.example.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
    private UserAddress userAddress;
    private String pseudo;

    private boolean connected;


    private FXMLLoader loader;


    private Node node;

    public User(UserAddress userAddress, String pseudo) {
        this.userAddress = userAddress;
        this.pseudo = pseudo;
        this.connected = false;
        //try {
            this.loader = new FXMLLoader();
            this.loader.setLocation(getClass().getResource("/ContactFrame.fxml"));
            //this.loader = new FXMLLoader((new File("src/main/java/org/example/GUI/ContactFrame.fxml").toURI().toURL()));
//        } catch (MalformedURLException e) {
//            System.out.print("erreur load fxml file");
//            throw new RuntimeException(e);
//        }
        System.out.println((String) this.loader.getRoot());
        try {
            this.loader.load();

            this.node = (Node)this.loader.getNamespace().get("contactFrame");
            Label label = (Label)this.node.lookup("#pseudoUser");
            label.setText(this.pseudo);
        } catch (IOException e) {
            System.out.println("erreur loader");
        }
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
                ", loader=" + loader +
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

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}

