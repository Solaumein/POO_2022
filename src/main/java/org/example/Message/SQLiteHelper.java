package org.example.Message;



import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private synchronized Connection connect() {//etablie la connection avec la BDD en local
        Connection con=null;
        try {
            //path to local DB
            String url = "jdbc:sqlite:messages.db";
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {//pour plus de clareté de l'erreur nous ne remontons pas l'erreur mais nous montrons qu'il y a un pb de connexion
            System.out.println("pb de connexion a la BDD");
            e.printStackTrace();
            
        }
        return con;//return null if failed
    }
    public synchronized void createTableMessage() {
        String sqlQuerry = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " integer PRIMARY KEY,"
                + MESSAGE_CONTENT + " text NOT NULL,"
                + OTHER_HOST + " text NOT NULL,"
                + MESSAGE_DATE + " text NOT NULL,"
                + RECU + " boolean NOT NULL)";
        try {
            Connection connection=connect();
            if(connection!=null) {
                Statement statement = connection.createStatement();
                statement.execute(sqlQuerry);
            }
        } catch (SQLException e) {//si problème de creation de table alors comme on peut quand meme utiliser l'application on laisse faire en retournant null
            e.printStackTrace();
        }
    }

    public synchronized boolean insert(Message msg) {//return true if no error false if can't update the db
        String contentMessage=msg.getContenu();
        String otherHostSTR=msg.getOtherHost().toString();
        if(otherHostSTR.matches("(.)*/[^/]+")){//pour éviter la mauvaise sauvegarde (avec le '/' du tostring de InetAddress
            otherHostSTR= otherHostSTR.split("/")[1];
        }
        String dateMsg=dateFormat.format(msg.getDateMessage());
        boolean recu=msg.getRecu();
        //sql sera la requete en string du insert de l'objet message passé en argument
        String sql = "INSERT INTO " + TABLE_NAME + "(" + MESSAGE_CONTENT + ", "+ OTHER_HOST + ", "+ MESSAGE_DATE + ", "+ RECU + ") VALUES(?,?,?,?)";
        try {
            Connection connection=connect();
            if(connection!=null) {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, contentMessage);
                pstmt.setString(2, otherHostSTR);
                pstmt.setString(3, dateMsg);
                pstmt.setBoolean(4, recu);
                pstmt.executeUpdate();
                return true;
            }else{
                throw new SQLException();//si problème alors return false
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized void printAll(){//utile pour debug
        String sql="SELECT * FROM "+TABLE_NAME;
        System.out.println("voici la liste totale ");
        try {
            Connection connection=connect();
            if(connection!=null) {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    String idMSG=rs.getString(ID);
                    Message msg = parseMessageFromResultSet(rs);//
                    System.out.println(idMSG + ":Message : " + msg);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Message parseMessageFromResultSet(ResultSet rs) {//transforme un resultSet en objet message
        try {
            String contenuMessage = rs.getString(MESSAGE_CONTENT);
            InetAddress IPSender= getOtherHostFromRS(rs);
            java.util.Date dateMSG=dateFormat.parse(rs.getString(MESSAGE_DATE));
            boolean recu=rs.getBoolean(RECU);
            return new Message(contenuMessage,dateMSG,recu,IPSender);
        } catch (SQLException | ParseException e) {//ceci veut dire que la BDD a été vérolé donc on kill le process
            System.out.println("la BDD possède une ligne indésirable mieux vaut la reset");
            throw new RuntimeException(e);
        }
    }

    private InetAddress getOtherHostFromRS(ResultSet rs) {
        try {
            return InetAddress.getByName(rs.getString(OTHER_HOST));
        } catch (UnknownHostException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized MessageHistory getMessageHistoryFromUser(InetAddress otherUser){
        String addrOfOtherHost=otherUser.toString().substring(1);
        System.out.println("on cherche les message du user "+addrOfOtherHost);
        String sql="SELECT * FROM "+TABLE_NAME+" WHERE "+OTHER_HOST+" = '"+addrOfOtherHost+"'";
        MessageHistory savedMessageHistory =new MessageHistory();
        try {
            Connection connection=connect();
            if(connection!=null) {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {//tout les message du result set seront du OTHER_HOST=otherUser
                    Message message = parseMessageFromResultSet(rs);
                    savedMessageHistory.addMessage(message);
                }
            }
        } catch (SQLException e) {//si pb de connexion alors faire comme si BDD vide (on prefere que ca fonctionne en dégradé pour l'utilisateur)
            e.printStackTrace();
        }
        return savedMessageHistory;
    }


    public synchronized HashMap<InetAddress, MessageHistory> getMessageHistory(){
        String sql="SELECT * FROM "+TABLE_NAME;
        HashMap<InetAddress, MessageHistory> savedListMessageHistory =new HashMap<>();
        try {
            Connection connection=connect();
            if(connection!=null) {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    InetAddress addrOtherHost = getOtherHostFromRS(rs);
                    MessageHistory messageHistoryOfSavedUser = savedListMessageHistory.get(addrOtherHost);
                    if (messageHistoryOfSavedUser == null) {
                        messageHistoryOfSavedUser = new MessageHistory();
                        savedListMessageHistory.put(addrOtherHost, messageHistoryOfSavedUser);
                    }
                    Message message = parseMessageFromResultSet(rs);
                    messageHistoryOfSavedUser.addMessage(message);
                }
            }
        } catch (SQLException e) {//si pb de connexion alors faire comme si BDD vide (on prefere que ca fonctionne en dégradé pour l'utilisateur)
        }
        return savedListMessageHistory;
    }

    public synchronized void reset() {
        String sqlQuerry="DROP table "+TABLE_NAME;
        try {
            Connection connection=connect();
            if(connection!=null){
                PreparedStatement preparedStatement= connection.prepareStatement(sqlQuerry);
                preparedStatement.executeUpdate();
                System.out.println("BDD reset avec success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}