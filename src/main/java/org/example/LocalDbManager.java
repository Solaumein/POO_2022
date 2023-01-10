package org.example;

import org.example.MessageBDD.SQLiteHelper;

import java.net.InetAddress;
import java.util.HashMap;

public class LocalDbManager {
    private HashMap<InetAddress,MessageHistory> messageHistoryDB;

    public synchronized MessageHistory getMessageHistory(InetAddress inetAddress){
        return messageHistoryDB.get(inetAddress);
    }
    public synchronized void updateSavedMessages(){
        this.messageHistoryDB= SQLiteHelper.getInstance().getMessageHistory();
    }
    public synchronized MessageHistory getMessageHistory(User user){
        InetAddress inetAddress=user.getUserAddress().getAddress();
        return getMessageHistory(inetAddress);
    }
    private static final LocalDbManager instance = new LocalDbManager();

    public HashMap<InetAddress, MessageHistory> getMessageHistoryDB() {
        return messageHistoryDB;
    }

    public static LocalDbManager getInstance() {
        return instance;
    }
    private LocalDbManager() {}

}
