package org.example.User;

import org.example.Controller.MainScreenController;
import org.example.Network.NetworkManagerTCP;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ListContact {
    public static UserAddress selfAddr;

    static {
        try {
            int port = NetworkManagerTCP.getPortLibre();
            if(port==-1) ;//throw new PortNotFreeException();todo faire qquchose qd ca marche pas
            selfAddr = new UserAddress(Inet4Address.getLocalHost(),port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String selfPseudo="tengui";
    public static User selfUser = new User(selfAddr, selfPseudo);


    public static ArrayList<User> listContact = new ArrayList<>();

    private static List<ContactEventHandler> handlers = new ArrayList<> ();
    public static void addHandler(ContactEventHandler handler) {
        handlers.add(handler);
    }

    public static void addContact(User user){
        listContact.add(user);

        for (ContactEventHandler handler : handlers) {
            handler.newContactAdded(user);
        }
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
