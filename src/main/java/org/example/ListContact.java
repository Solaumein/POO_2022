package org.example;

import java.net.Inet4Address;
import java.net.InetAddress;
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


    public static ArrayList<User> listContact = new ArrayList<>();

    public static void addContact(User user){
        listContact.add(user);

    }

    private static int searchByAddress(InetAddress addr){
        int i=0;
        for (User user : listContact) {
            if(user.getUserAddress().getAddress()==addr){return i;}
            i++;
        }
        return -1;
    }

    public static void updatePseudoByAddr(InetAddress addr, String pseudo){
        int index = searchByAddress(addr);
        User user = listContact.get(index);
        user.setPseudo(pseudo);
        listContact.set(index, user);
    }

    public static boolean isPseudoInList(String pseudo){
        for (User user : listContact){
            if (user.getPseudo().equals(pseudo)) {
                return true;
            }
        }
        return false;
    }

    public static void removeContactByAddr(InetAddress addr){
        int index = searchByAddress(addr);
        listContact.remove(index);
    }

    public static boolean isContactinLIst(InetAddress addr){
        for(User user : listContact){
            if(user.getUserAddress().getAddress()==addr){
                return true;
            }
        }
        return false;
    }

//    public User getSelfUser() {
//        return selfUser;
//    }
}
