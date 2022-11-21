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
        connection = SetUPConnection.getConnection();
    }

   
}