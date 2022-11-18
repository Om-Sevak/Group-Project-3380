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
import java.util.concurrent.SynchronousQueue;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.plaf.synth.SynthScrollBarUI;

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
            try {
                String[] line = lineString.split(",");
                String type = line[0];
                String title = line[1];
                boolean flag = true;
                int j;
                ArrayList<Integer> dirID = new ArrayList<Integer>();
                int temp;
                for (j = 2; j < line.length && flag; j++) {
                    
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                temp = addDirector(line[j], connection);
                                dirID.add(temp);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                            if(e.getErrorCode() == 2627){
                                try{
                                    String query = "SELECT dirID FROM director WHERE dirName = ?";
                                    PreparedStatement statement = connection.prepareStatement(query);
                                    statement.setString(1, line[j]);
                                    ResultSet rs = statement.executeQuery();
                                    rs.next();
                                    temp = rs.getInt("dirID");  
                                    dirID.add(temp);
                                }catch(SQLException e1){
                                    //do nothing
                                }
                            }
                        }
                    }
                } 
                flag = true;
                String cleanedData = "";
                ArrayList<Integer> castID = new ArrayList<Integer>();
                while (j < line.length && flag) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                cleanedData = cleanData(line[j]);
                                temp = addCast( line[j], connection);
                                castID.add(temp);
                            }
                        }catch (SQLException e){
                           
                            redundantEntry++;
                            if(e.getErrorCode() == 2627){
                                try{
                                    String query = "SELECT castID FROM cast WHERE castName = ?";
                                    PreparedStatement statement = connection.prepareStatement(query);
                                    statement.setString(1, cleanedData);
                                    ResultSet rs = statement.executeQuery();
                                    rs.next();
                                    temp = rs.getInt("castID");  
                                    castID.add(temp);
                                }catch(SQLException e1){
                                    //do nothing
                             
                                }
                            }
                        }
                    }
                    j++;
                }
    
                flag = true;
                ArrayList<Integer> countryID = new ArrayList<Integer>();
                while (j < line.length && flag) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                cleanedData = cleanData(line[j]);
                                temp = addCountry(cleanedData, connection);
                                countryID.add(temp);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                            try{
                                String query = "SELECT countryID FROM country WHERE countryName = ?";
                                PreparedStatement statement = connection.prepareStatement(query);
                                statement.setString(1, cleanedData);
                                ResultSet rs = statement.executeQuery();
                                rs.next();
                                temp = rs.getInt("countryID");  
                                countryID.add(temp);
                            }catch(SQLException e1){
                                //do nothing
                         
                            }
                        }
                    }
                    j++;
                }
                String date = line[j];
                String releaseYear = line[j + 1];
                String rated = line[j + 2];
                String duration = line[j + 3];
                String IMDB = line[j + 4];
                String rotten = line[j + 5];
                String platform = line[j + 6];
               
                ArrayList<Integer> genreID = new ArrayList<Integer>();
                flag = true;
                for (j = j + 7; j < line.length && flag; j++) {
                    if (line[j].equals("|")) {
                        flag = false;
                    } else {
                        try{
                            if(!(line[j].equals(""))){
                                cleanedData = cleanData(line[j]);
                                temp = addGenre(cleanedData, connection);
                                genreID.add(temp);
                            }
                        }catch (SQLException e){
                            redundantEntry++;
                            if(e.getErrorCode() == 2627){
                                try{
                                    String query = "SELECT genreID FROM genre WHERE genreName = ?";
                                    PreparedStatement statement = connection.prepareStatement(query);
                                    statement.setString(1, cleanedData);
                                    ResultSet rs = statement.executeQuery();
                                    rs.next();
                                    temp = rs.getInt("genreID");  
                                    genreID.add(temp);
                                }catch(SQLException e1){
                                    //do nothing
                             
                                }
                            }
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
                }


                try {
                    int mediaID = addMedia(title, releaseYear, rated, rotten, IMDB, duration, description, connection);
                    count++;
                    System.out.println(count);
                    addIsA(mediaID, type, connection);
                    addStreamedOn(mediaID, platform, date, connection);
                    for (int i = 0; i < dirID.size(); i++) {
                        addDirectedBy(mediaID, dirID.get(i), connection);
                    }
                    for(int i = 0; i < castID.size(); i++){
                        addCastInvolved(mediaID, castID.get(i), connection);
                    }
                    for(int i = 0; i < genreID.size(); i++){
                        addMediaGenre(mediaID, genreID.get(i), connection);
                    }
                    for(int i = 0; i < countryID.size(); i++){
                        addMadeIn(mediaID, countryID.get(i), connection);
                    }

                } catch (SQLException e) {
                
                    if(e.getErrorCode() == 2627){
                        try{
                            String query = "SELECT mediaID FROM entertainment where mediaName = (?);";
                            PreparedStatement ps = connection.prepareStatement(query);
                            ps.setString(1, title);
                            ResultSet rs = ps.executeQuery();
                            int mediaID = 0;
                            while(rs.next()){
                                mediaID = rs.getInt("mediaID");
                            }
                            addStreamedOn(mediaID, platform, date, connection);
                        }catch (SQLException e1){
                            //do nothing
                        }
                    }
                }
                
               
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error Corrupted Data: Bypassing corrupted row");
                corruptedEntry++;
            } 


        }

      
    } catch (IOException e) {
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

        return mediaID;
    }

    public static void addIsA(int mediaID, String type, Connection connection) throws SQLException
    {
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
    }

    public static void addStreamedOn(int mediaID, String streamingService, String dateAdded, Connection connection) throws SQLException
    {
        int streamingServiceID = Integer.parseInt(streamingService);
        String insertQuery = "Insert Into streamedOn (mediaID, platformID, dateAdded) Values (?,?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, streamingServiceID);
        statement.setString(3, dateAdded);
        statement.executeUpdate();
    }

    public static void addDirectedBy(int mediaID, int directorID, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into directedBy (mediaID, dirID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, directorID);
        statement.executeUpdate();
    }

    public static void addCastInvolved(int mediaID, int castID, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into castInvolved (mediaID, castID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, castID);
        statement.executeUpdate();
    }

    public static void addMediaGenre(int mediaID, int genreID, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into mediaGenre (mediaID, genreID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, genreID);
        statement.executeUpdate();
    }

    public static void addMadeIn(int mediaID, int countryID, Connection connection) throws SQLException
    {
        String insertQuery = "Insert Into madeIn (mediaID, countryID) Values (?,?);";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, mediaID);
        statement.setInt(2, countryID);
        statement.executeUpdate();
    }


    public static String cleanData(String data){
        if(data.charAt(0) == '"'){
            data = data.substring(1);
        }

        if(data.length() > 0 && data.charAt(data.length()-1) == '"'){
            data = data.substring(0, data.length()-1);
        }

        return data;
    }
}
