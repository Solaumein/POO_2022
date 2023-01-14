package org.example.User;

import org.example.Controller.MainScreenController;
import org.example.Network.NetworkManagerTCP;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ListContact {
    private static UserAddress selfAddr;

    public static UserAddress getSelfAddr() {
        return selfAddr;
    }

    static {
        try {
            int port = NetworkManagerTCP.getPortLibre();
            if(port==-1) {
                System.out.println("pas de port libre pour le moment reesayez plus tard");
                throw new RuntimeException();
            }
            selfAddr = new UserAddress(Inet4Address.getLocalHost(),port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String selfPseudo="tengui";
    public static User selfUser = new User(selfAddr, selfPseudo);

    public static ArrayList<User> listContact = new ArrayList<>();

    private static List<ContactEventHandler> handlers = new ArrayList<> ();
    private static List<ContactEventHandlerDeco> handlersDeco = new ArrayList<>();

    private static List<ContactEventHandlerUpdatePseudo> handlersUpdate = new ArrayList<>();
    public static void addHandler(ContactEventHandler handler) {
        handlers.add(handler);
    }
    public static void addHandlerDeco(ContactEventHandlerDeco handler) {
        handlersDeco.add(handler);
    }
    public static void addHandlerUpdatePseudo(ContactEventHandlerUpdatePseudo handler){handlersUpdate.add(handler);}
    public static void addContact(User user){
        listContact.add(user);

        for (ContactEventHandler handler : handlers) {
            handler.newContactAdded(user);
        }
    }

    public static int searchByAddress(InetAddress addr){
        int i=0;
        for (User user : listContact) {
            if(user.getUserAddress().getAddress().equals(addr)){return i;}
            i++;
        }
        return -1;
    }

    public static void updatePseudoByAddr(InetAddress addr, String pseudo, String oldPseudo){
        int index = searchByAddress(addr);
        User user = listContact.get(index);
        user.setPseudo(pseudo);
        listContact.set(index, user);
        for (ContactEventHandlerUpdatePseudo handler : handlersUpdate) {
            handler.updatecontact(oldPseudo,pseudo);
        }
    }

    public static boolean isPseudoInList(String pseudo){
        for (User user : listContact){
            if (user.getPseudo().equals(pseudo)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAdressInList(InetAddress addr){
        for (User user : listContact){
            if (user.getUserAddress().getAddress().equals(addr)) {
                return true;
            }
        }
        return false;
    }

    public static void removeContactByAddr(InetAddress addr){
        int index = searchByAddress(addr);
        for (ContactEventHandlerDeco handler : handlersDeco) {
            handler.deleteContact(listContact.get(index));
        }

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
