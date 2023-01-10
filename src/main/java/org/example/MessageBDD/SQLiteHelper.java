package org.example.MessageBDD;



import org.example.LocalDbManager;
import org.example.Message;
import org.example.MessageHistory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelper {
    private static final String ID = "ID";
    private static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    private static final String MESSAGE_DATE = "MESSAGE_DATE";
    private static final String OTHER_HOST = "OTHER_HOST";//l'addresse IP est unique par utilisateur
    private static final String RECU = "RECU";
    private static final String TABLE_NAME = "message";

   private static final SQLiteHelper instance = new SQLiteHelper();

    public static SQLiteHelper getInstance() {
        return instance;
    }

    protected static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private synchronized Connection connect() {//return null if failed
        //creer la bdd ou la load
        Connection con = null;
        try {
            //path to local DB
            String url = "jdbc:sqlite:messages.db";
            con = DriverManager.getConnection(url);
            if (con != null) {
                DatabaseMetaData metaData = con.getMetaData();
                //System.out.println("The DB is located at " + metaData.getURL());
                //System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //System.out.println("erreur SQL " + e.getMessage() + " state " + e.getSQLState());
        }
        return con;
    }


    public synchronized void createTableMessage() {
        String sqlQuerry = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " integer PRIMARY KEY,"
                + MESSAGE_CONTENT + " text NOT NULL,"
                + OTHER_HOST + " text NOT NULL,"
                + MESSAGE_DATE + " text NOT NULL,"
                + RECU + " boolean NOT NULL)";
        try {
            Statement statement = connect().createStatement();
            statement.execute(sqlQuerry);
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public synchronized boolean insert(Message msg) {//return true if no error
        String contentMessage=msg.getContenu();
        String otherHostSTR=msg.getOtherHost().toString();

        String dateMsg=dateFormat.format(msg.getDateMessage());
System.out.println(dateMsg);
        boolean recu=msg.getRecu();

        String sql = "INSERT INTO " + TABLE_NAME + "(" + MESSAGE_CONTENT + ", "+ OTHER_HOST + ", "+ MESSAGE_DATE + ", "+ RECU + ") VALUES(?,?,?,?)";
        try {
            PreparedStatement pstmt = connect().prepareStatement(sql);
            pstmt.setString(1, contentMessage);
            pstmt.setString(2, otherHostSTR);
            pstmt.setString(3,dateMsg);
            pstmt.setBoolean(4,recu);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public synchronized void printAll(){
        String sql="SELECT * FROM "+TABLE_NAME;
        //ArrayList<String> listMSGInSTring=new ArrayList<>();
        System.out.println("voici la liste totale ");
        try {
            Statement statement = connect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String idMSG=rs.getString(ID);
                Message msg = parseMessageFromResultSet(rs);//
                System.out.println(idMSG + ":Message : " + msg);

            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Message parseMessageFromResultSet(ResultSet rs) {
        try {
            String contenuMessage = rs.getString(MESSAGE_CONTENT);
            InetAddress IPSender= getOtherHostFromRS(rs);
            System.out.println(rs.getString(MESSAGE_DATE));
            java.util.Date dateMSG=dateFormat.parse(rs.getString(MESSAGE_DATE));
            boolean recu=rs.getBoolean(RECU);
            return new Message(contenuMessage,dateMSG,recu,IPSender);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private InetAddress getOtherHostFromRS(ResultSet rs) {
        try {
            return InetAddress.getByName(rs.getString(OTHER_HOST).split("/")[1]);
        } catch (UnknownHostException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized HashMap<InetAddress, MessageHistory> getMessageHistory(){
        String sql="SELECT * FROM "+TABLE_NAME;
        HashMap<InetAddress, MessageHistory> savedListMessageHistory =new HashMap<>();
        try {
            Statement statement= connect().createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while (rs.next()){
                InetAddress addrOtherHost= getOtherHostFromRS(rs);
                MessageHistory  messageHistoryOfSavedUser= savedListMessageHistory.get(addrOtherHost);
                if(messageHistoryOfSavedUser==null){
                    messageHistoryOfSavedUser=new MessageHistory();
                    savedListMessageHistory.put(addrOtherHost,messageHistoryOfSavedUser);
                }

                //String idMSG=rs.getString(ID);
                Message message=parseMessageFromResultSet(rs);
                messageHistoryOfSavedUser.addMessage(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return savedListMessageHistory;
    }

    public synchronized void reset() {
        String sqlQuerry="DROP table "+TABLE_NAME;
        try {
            PreparedStatement preparedStatement= connect().prepareStatement(sqlQuerry);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("BDD reset avec success");

    }

}