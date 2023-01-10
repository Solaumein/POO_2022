package org.example.MessageBDD;



import org.example.LocalDbManager;
import org.example.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelper {
    private static final String ID = "ID";
    private static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    private static final String MESSAGE_DATE = "MESSAGE_DATE";
    private static final String SENDER = "SENDER";
    private static final String TABLE_NAME = "message";

    private static final SQLiteHelper instance = new SQLiteHelper();

    public static SQLiteHelper getInstance() {
        return instance;
    }


    private static Connection connect() {//return null if failed
        //creer la bdd ou la load
        Connection con = null;
        try {
            //path to local DB
            String url = "jdbc:sqlite:messages.db";
            con = DriverManager.getConnection(url);
            if (con != null) {
                DatabaseMetaData metaData = con.getMetaData();
                System.out.println("The DB is located at " + metaData.getURL());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println("erreur SQL " + e.getMessage() + " state " + e.getSQLState());
        }
        return con;
    }


    public static void createTableMessage() {
        String sqlQuerry = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " integer PRIMARY KEY,"
                + MESSAGE_CONTENT + " text NOT NULL,"
                + MESSAGE_DATE + " text NOT NULL,"
                + SENDER + " text NOT NULL";
        try {
            Statement statement = connect().createStatement();
            statement.execute(sqlQuerry);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean insert(String contentMessage, String senderMessage) {//return true if no error
        String sql = "INSERT INTO " + TABLE_NAME + "(" + MESSAGE_CONTENT + ", " + SENDER + ") VALUES(?,?)";
        try {
            PreparedStatement pstmt = connect().prepareStatement(sql);
            pstmt.setString(1, contentMessage);
            pstmt.setString(2, senderMessage);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static void selectAll(){
        String sql="SELECT * FROM "+TABLE_NAME;
       LocalDbManager localDbManager=LocalDbManager.getInstance();

        try {
            Statement statement= connect().createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while (rs.next()){
                //todo faire un if sender deja ajouter dans la hashmap alors rajouter le message a la listMessage sinon creer hashmap
                Message message=new Message(rs.getString(SENDER));
                System.out.println("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}