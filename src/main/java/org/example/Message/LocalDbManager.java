package org.example.Message;

import org.example.User.User;

import java.net.InetAddress;
import java.util.HashMap;

public class LocalDbManager {
    private HashMap<InetAddress, MessageHistory> messageHistoryDB;
    public synchronized MessageHistory getMessageHistory(InetAddress inetAddress){
        if(messageHistoryDB.get(inetAddress)==null)return new MessageHistory();
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

    public synchronized void addMessage(Message message){
        InetAddress user=message.getOtherHost();
        if(this.messageHistoryDB.get(user)==null){
            this.messageHistoryDB.put(user,new MessageHistory());
        }
        this.messageHistoryDB.get(user).addMessage(message);
        SQLiteHelper.getInstance().insert(message);
        //System.out.println("new message added "+user);
    }
    public synchronized HashMap<InetAddress, MessageHistory> getMessageHistoryDB() {
        return messageHistoryDB;
    }
    public static LocalDbManager getInstance() {
        return instance;
    }
    private LocalDbManager() {
        this.messageHistoryDB=new HashMap<>();
    }
}
