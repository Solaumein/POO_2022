package org.example;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ListContact {
    public static UserAddress selfAddr;

    static {
        try {
            selfAddr = new UserAddress(Inet4Address.getLocalHost(),42069); //todo faire fonction de choix de port libre
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pseudoDur="tengui";
    public static User selfUser = new User(selfAddr, pseudoDur);


    public static ArrayList<User> listContact;

    public static void addContact(User user){
        listContact.add(user);

    }

//    public User getSelfUser() {
//        return selfUser;
//    }
}
