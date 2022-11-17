import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PopulatingData {

// Connect to your database.
// Replace server name, username, and password with your credentials
public static void main(String[] args) {

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

  

    try (Connection connection = DriverManager.getConnection(connectionUrl);) {
        readCSV("D:\\Uni\\3rd year\\3380\\GroupProject\\Entertainment-Small.csv", connection);
    }
    catch (SQLException e) {
    e.printStackTrace();

        readCSV("D:\\UManitoba\\Fall 2022\\COMP 3380\\Project\\SmallData.csv");
    }

}

public static void readCSV(String fileName, Connection connection) {

    // reading the csv file
    int corruptedEntry = 0;
    int redundantEntry = 0;

    try {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineString = reader.readLine();
        while ((lineString = reader.readLine()) != null) {
            try {
                String[] line = lineString.split(",");
                String type = line[0];
                String title = line[1];
                boolean flag = true;
                int j;
                for (j = 2; j < line.length && flag; j++) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                addDirector(cleanData(line[j]), connection);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                        }
                    }
                } 
                flag = true;
                while (j < line.length && flag) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                addCast(cleanData(line[j]), connection);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                        }
                    }
                    j++;
                }
    
                flag = true;
                while (j < line.length && flag) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                addCountry(cleanData(line[j]), connection);
                            }
                        }catch (SQLException e){
                            redundantEntry++;

                        }
                    }
                    j++;
                }
                String date = line[j];
                String release_year = line[j + 1];
                String rated = line[j + 2];
                String duration = line[j + 3];
                String IMDB = line[j + 4];
                String rotten = line[j + 5];
                String platform = line[j + 6];
                ArrayList<String> listed_in = new ArrayList<String>();
                flag = true;
                for (j = j + 7; j < line.length && flag; j++) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                addGenre(cleanData(line[j]), connection);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                        }
                    }
                }
                String description = "";
                while (j < line.length) {
                    if (j == line.length - 1) {
                        description += line[j];
                    } else {
                        description += line[j] + ",";
                    }

                    j++;

                    System.out.println("Type: " + type);
                    System.out.println("Title: " + title);
                    // System.out.println("Directors: " + director);
                    // System.out.println("Cast: " + cast);
                    System.out.println("Country: " + country);
                    // System.out.println("Date: " + date);
                    // System.out.println("Release Year: " + release_year);
                    // System.out.println("Rated: " + rated);
                    // System.out.println("Duration: " + duration);
                    // System.out.println("IMDB: " + IMDB);
                    // System.out.println("Rotten: " + rotten);
                    // System.out.println("Platform: " + platform);
                    // System.out.println("Listed in: " + listed_in);
                    // System.out.println("Description: " + description.trim());
                    System.out.println("-------------------------------------------------");

                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error Corrupted Data: Bypassing corrupted row");
                    corruptedEntry++;

                }
          

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error Corrupted Data: Bypassing corrupted row");
                corruptedEntry++;
            } 


        }

        //printing director table
        System.out.println("Director Table");
        System.out.println("-------------------------------------------------");
        String query = "SELECT * FROM Director order by dirID";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("dirID") + " " + rs.getString("dirName"));
        }

        //printing country table
        System.out.println("Country Table");
        System.out.println("-------------------------------------------------");
        query = "SELECT * FROM Country order by countryID";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("countryID") + " " + rs.getString("countryName"));
        }

        //printing genre table
        System.out.println("Genre Table");
        System.out.println("-------------------------------------------------");
        query = "SELECT * FROM Genre order by genreID";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("genreID") + " " + rs.getString("genreName"));
        }

        //printing cast table
        System.out.println("Cast Table");
        System.out.println("-------------------------------------------------");
        query = "SELECT * FROM Cast order by castID";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("castID") + " " + rs.getString("castName"));
        }


    } catch (IOException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
   
    System.out.println("Number of corrupted rows bypassed: " + corruptedEntry);
    System.out.println("Number of redundant elements bypassed: " + redundantEntry);

}

    public static void addDirector( String directorName, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into director (dirName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, directorName);
        statement.executeUpdate();

    }

    public static void addCountry( String countryName, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into country (countryName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, countryName);
        statement.executeUpdate();

    }

    public static void addGenre( String genreName, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into genre (genreName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, genreName);
        statement.executeUpdate();

    }

    public static void addCast( String castName, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into cast (castName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setString(1, castName);
        statement.executeUpdate();

    }

    public static String cleanData(String data){
        if(data.charAt(0) == '"'){
            data = data.substring(1);
        }

        if(data.charAt(data.length()-1) == '"'){
            data = data.substring(0, data.length()-1);
        }

        return data.trim();
    }

    //adding data into their respective tables
    public void addDirector( String directorName){
     
        try{
            String insertQuery = "Insert Into director Values ( ? );";

            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, directorName);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                System.out.println( " Director Name : " + resultSet.getString("dirName"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void addCountry (String countryName) 
    {
        try{
            String insertQuery = "Insert Into country (countryName) Values ( ? );";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, countryName);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next())
                System.out.println("Country Name " + resultSet.getString("countryName") );  

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    
}
