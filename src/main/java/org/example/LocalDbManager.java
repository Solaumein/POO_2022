package org.example;

import java.net.InetAddress;
import java.util.HashMap;

public class LocalDbManager {
    HashMap<InetAddress,MessageHistory> messageHistoryDB;

    public synchronized MessageHistory getMessageHistory(InetAddress inetAddress){
        return messageHistoryDB.get(inetAddress);
    }
    public synchronized MessageHistory getMessageHistory(User user){
        InetAddress inetAddress=user.getUserAddress().getAddress();
        return getMessageHistory(inetAddress);
    }
    private static final LocalDbManager instance = new LocalDbManager();

    public static LocalDbManager getInstance() {
        return instance;
    }
    private LocalDbManager() {}

}
