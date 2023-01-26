package org.example.User;

import org.example.Network.NetworkManagerTCP;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class ListContact {//todo pk tout en static ? sinon au lieu du bloc static c plus propre de faire dans le initialize mainScreenController le getPortLibre
    private static final UserAddress selfAddr;
    private static User selectedContact;

    public synchronized static void setSelectedContact(User selectedContact) {
        ListContact.selectedContact = selectedContact;
    }

    public synchronized static User getSelectedContact() {
        return selectedContact;
    }

    public static UserAddress getSelfAddr() {
        return selfAddr;
    }

    static {
        try {
            int port = NetworkManagerTCP.getPortLibre();
            if(port==-1) {
                throw new RuntimeException("pas de port libre pour le moment reesayez plus tard");
            }
            selfAddr = new UserAddress(Inet4Address.getLocalHost(),port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String selfPseudo="Arthur si tu nous vois";
    public static User selfUser = new User(selfAddr, selfPseudo);

    public static ArrayList<User> listContact = new ArrayList<>();

    private static final List<ContactEventHandler> handlers = new ArrayList<> ();
    private static final List<ContactEventHandlerDeco> handlersDeco = new ArrayList<>();

    private static final List<ContactEventHandlerUpdatePseudo> handlersUpdate = new ArrayList<>();
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

    public static int getUserByName(String pseudo){
        int i=0;
        for (User user : listContact) {
            if(user.getPseudo().equals(pseudo)){return i;}
            i++;
        }
        return -1;
    }


}
