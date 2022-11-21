import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SetUPConnection {

    
    private static Connection connection = setUpConnection();

    public static Connection getConnection(){
        return connection;
    }

    private static Connection setUpConnection(){

        try{
            String connectionUrl =  getUrl();
            connection = DriverManager.getConnection(connectionUrl);

        } catch(SQLException e){
            e.printStackTrace();
        }


        return connection;
    }

    private static String getUrl(){
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
