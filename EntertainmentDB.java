import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class EntertainmentDB {

    private static Connection connection;
    public static void main(String[] args) {
        
        String connectionUrl = setupConnection();
        
        try{
            connection = DriverManager.getConnection(connectionUrl);
            PopulateDB pDB = new PopulateDB(connection);
            pDB.populate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String setupConnection(){
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
        FileInputStream configFile = new FileInputStream(fileName);
        prop.load(configFile);
        configFile.close();
        } catch (FileNotFoundException ex) {
        System.out.println("Could not find config file.");
        System.exit(1);
        } catch (IOException ex) {
        System.out.println("Error reading config file.");
        System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));
    
        if (username == null || password == null){
        System.out.println("Username or password not provided.");
        System.exit(1);
        }
    
        String connectionUrl =
        "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
        + "database=cs3380;"
        + "user=" + username + ";"
        + "password="+ password +";"
        + "encrypt=false;"
        + "trustServerCertificate=false;"
        + "loginTimeout=30;";

        return connectionUrl;
    }
}