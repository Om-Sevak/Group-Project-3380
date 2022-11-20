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
        readCSV("D:\\Uni\\3rd year\\3380\\GroupProject\\Entertainment-Medium.csv", connection);
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

}

public static void readCSV(String fileName, Connection connection) {

    // reading the csv file
    int corruptedEntry = 0;
    int redundantEntry = 0;
    int count = 0;

    try {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineString = reader.readLine();
        while ((lineString = reader.readLine()) != null) {
            System.out.println(++count);
            try {
                String[] line = lineString.split(",");
                String type = line[0];
                String title = line[1];
                String date = line[2];
                String releaseYear = line[3];
                String rated = line[4];
                String duration = line[5];
                String IMDB = line[6];
                String rotten = line[7];
                String platform = line[8];
                boolean flag = true;
                String cleanedData = "";
                int j;
                int mediaID ;
                boolean isMediaDuplicate = false;

                String description = "";
                for (j = 9; j < line.length && flag; j++) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        if (j == line.length - 1) {
                            description += line[j];
                        } else {
                            description += line[j] + ",";
                        }
                    }
                }
               
                mediaID = checkDuplicate("mediaID", "mediaName", "entertainment", title, connection);
                try{
                //entering data in entertainment table and getting mediaID
                    if(mediaID == -1){
                        mediaID = addMedia(title, releaseYear, rated, rotten, IMDB, duration, description, connection);
                        addIsA(mediaID, type, connection);
                        addStreamedOn(mediaID, platform, date, connection);
                    } else {
                        isMediaDuplicate = true;
                        addStreamedOn(mediaID, platform, date, connection);
                    }
                }catch(SQLException e){
                    //do nothing
                }

              
                int temp;
                flag = true;
                for (j = j; j < line.length && flag; j++) {
                    
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                cleanedData = cleanData(line[j]);
                                temp = checkDuplicate("dirID", "dirName", "director", cleanedData, connection);
                                if(temp == -1){
                                    temp = addDirector(cleanedData, connection);
                                } else{
                                    redundantEntry++;
                                }
                                if(!isMediaDuplicate){
                                    addDirectedBy(mediaID, temp, connection);
                                }
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
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
                                cleanedData = cleanData(line[j]);
                                temp = checkDuplicate("castID", "castName", "cast", cleanedData, connection);
                                if(temp == -1){
                                    temp = addCast(cleanedData, connection);
                                } else{
                                    redundantEntry++;
                                }
                                if(!isMediaDuplicate){
                                    addCastInvolved(mediaID, temp, connection);
                                }
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
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
                                cleanedData = cleanData(line[j]);
                                temp = checkDuplicate("countryID", "countryName", "country", cleanedData, connection);
                                if(temp == -1){
                                    temp = addCountry(cleanedData, connection);
                                } else{
                                    redundantEntry++;
                                }
                                if(!isMediaDuplicate){
                                    addMadeIn(mediaID, temp, connection);
                                }
                            }
                        }catch (SQLException e){
                           e.printStackTrace();
                        }
                    }
                    j++;
                }
              
                flag = true;
                for (j = j; j < line.length && flag; j++) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                cleanedData = cleanData(line[j]);
                                temp = checkDuplicate("genreID", "genreName", "genre", cleanedData, connection);
                                if(temp == -1){
                                    temp = addGenre(cleanedData, connection);
                                } else{
                                    redundantEntry++;
                                }
                               if(!isMediaDuplicate){
                                    addMediaGenre(mediaID,temp, connection);
                                }
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
                     
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error Corrupted Data: Bypassing corrupted row");
                corruptedEntry++;
            } 


        }
        
        //printing director table
        System.out.println("Director Table");
        String query = "SELECT * FROM director order by dirID";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            System.out.println(rs.getInt("dirID") + " " + rs.getString("dirName"));
        }

      
    } catch (IOException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
   
    System.out.println("Number of corrupted rows bypassed: " + corruptedEntry);
    System.out.println("Number of redundant elements bypassed: " + redundantEntry);

}


    public static int addDirector( String directorName, Connection connection) throws SQLException
    {
        
       
        int dirID = 0;
        String insertQuery = "Insert Into director (dirName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, directorName);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            dirID = rs.getInt(1);
        }
        statement.close();
       
        
        return dirID;
    }

    public static int addCountry( String countryName, Connection connection) throws SQLException
    {
    
       
        int countryID = 0;
        String insertQuery = "Insert Into country (countryName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, countryName);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            countryID = rs.getInt(1);
        }
        statement.close();
       
        
        return countryID;

    }

    public static int addGenre( String genreName, Connection connection) throws SQLException
    {
       
        int genreID = 0;
        String insertQuery = "Insert Into genre (genreName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, genreName);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            genreID = rs.getInt(1);
        }
        statement.close();
        
        return genreID;
    }

    public static int addCast( String castName, Connection connection) throws SQLException
    {
    
        int castID = 0;
        String insertQuery = "Insert Into cast (castName) Values (?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, castName);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if(rs.next()){
            castID = rs.getInt(1);
        }
        statement.close();
        
        
        return castID;
    }

    public static int addMedia(String mediaName, String releaseYear, String rated, String rottenTomatoes, String IMDB, String duration, String mediaDescription, Connection connection) throws SQLException {
       
       
        int mediaID = -1;
        String inserQuery = "Insert Into entertainment (mediaName, releaseYear, rated, rottenTomatoes, IMDB, duration, mediaDescription) Values (?,?,?,?,?,?,?);";
        PreparedStatement statement = connection.prepareStatement(inserQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, mediaName);
        statement.setString(2, releaseYear);
        statement.setString(3, rated);
        statement.setString(4, rottenTomatoes);
        statement.setString(5, IMDB);
        statement.setString(6, duration);
        statement.setString(7, mediaDescription);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            mediaID = rs.getInt(1);
        }
        statement.close();
       
        
        return mediaID;
    }

    public static void addIsA(int mediaID, String type, Connection connection) throws SQLException
    {
       // disableForeignKeyChecks("isA","mediaID",connection);
       
        int typeID ;
        
        if(type.equals("Movie")){
            typeID = 1;
        } else{
            typeID = 2;
        }

        String insertQuery = "Insert Into isA (mediaID, mediaTypeID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, typeID);
        statement.executeUpdate();
        statement.close();
       // enableForeignKeyChecks("isA","mediaID",connection);
        
    }

    public static void addStreamedOn(int mediaID, String streamingService, String dateAdded, Connection connection) throws SQLException
    {
        //disableForeignKeyChecks("streamedOn","mediaID",connection);
       
        int streamingServiceID = Integer.parseInt(streamingService);
        String insertQuery = "Insert Into streamedOn (mediaID, platformID, dateAdded) Values (?,?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, streamingServiceID);
        statement.setString(3, dateAdded);
        statement.executeUpdate();
        statement.close();
       // enableForeignKeyChecks("streamedOn","mediaID",connection);
        
    }

    public static void addDirectedBy(int mediaID, int directorID, Connection connection) throws SQLException
    {
       // disableForeignKeyChecks("directedBy","mediaID",connection);
       
        String insertQuery = "Insert Into directedBy (mediaID, dirID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, directorID);
        statement.executeUpdate();
        statement.close();
       // enableForeignKeyChecks("directedBy","mediaID",connection);
        
    }

    public static void addCastInvolved(int mediaID, int castID, Connection connection) throws SQLException
    {
       // disableForeignKeyChecks("castInvolved","mediaID",connection);
       
        String insertQuery = "Insert Into castInvolved (mediaID, castID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, castID);
        statement.executeUpdate();
        statement.close();
       // enableForeignKeyChecks("castInvolved","mediaID",connection);
        
    }

    public static void addMediaGenre(int mediaID, int genreID, Connection connection) throws SQLException
    {
        //disableForeignKeyChecks("mediaGenre","mediaID",connection);
       
        String insertQuery = "Insert Into mediaGenre (mediaID, genreID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, genreID);
        statement.executeUpdate();
        statement.close();
       // enableForeignKeyChecks("mediaGenre","mediaID",connection);
        
    }

    public static void addMadeIn(int mediaID, int countryID, Connection connection) throws SQLException
    {
        //disableForeignKeyChecks("madeIn","mediaID",connection);
       
        String insertQuery = "Insert Into madeIn (mediaID, countryID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, countryID);
        statement.executeUpdate();
        statement.close();
        // enableForeignKeyChecks("madeIn","countryID",connection);
        
    }


    public static String cleanData(String data){
        if(data.charAt(0) == '"'){
            data = data.substring(1);
        }

        if(data.length() > 0 && data.charAt(data.length()-1) == '"'){
            data = data.substring(0, data.length()-1);
        }

        return data.trim();
    }

    //make a function to check if the data is already in the database and return the id of the data else return -1
    public static int checkDuplicate(String coloumnID, String coloumnName, String table, String value, Connection connection) throws SQLException
    {
        int dirID = -1;
        String selectQuery = "Select " + coloumnID +"  From "+ table + " Where " + coloumnName + " = ?;";
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setString(1, value);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            dirID = rs.getInt(1);
        }
        statement.close();
        return dirID;
    }

    //function disabling foreign key checks
    public static void disableForeignKeyChecks(String table, String fkName, Connection connection) throws SQLException
    {
        String disableQuery = "ALTER TABLE " + table + " NOCHECK CONSTRAINT " +fkName +";";
        PreparedStatement statement = connection.prepareStatement(disableQuery);
        statement.executeUpdate();
        statement.close();

    }

    //function enabling foreign key checks
    public static void enableForeignKeyChecks(String table, String fkName, Connection connection) throws SQLException
    {
        String enableQuery = "Alter Table "+ table +" Check Constraint "+ fkName +";";
        PreparedStatement statement = connection.prepareStatement(enableQuery);
        statement.executeUpdate();
        statement.close();

    }

    //function to disable triggers
    public static void disableTriggers(Connection connection) throws SQLException
    {
        String disableQuery = "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;";
        PreparedStatement statement = connection.prepareStatement(disableQuery);
        statement.executeUpdate();
        statement.close();
    }

    //function to enable triggers
    public static void enableTriggers(Connection connection) throws SQLException
    {
        String enableQuery = "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;";
        PreparedStatement statement = connection.prepareStatement(enableQuery);
        statement.executeUpdate();
        statement.close();
    }

}
