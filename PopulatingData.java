import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

        // Properties prop = new Properties();
        // String fileName = "auth.cfg";
        // try {
        //     FileInputStream configFile = new FileInputStream(fileName);
        //     prop.load(configFile);
        //     configFile.close();
        // } catch (FileNotFoundException ex) {
        //     System.out.println("Could not find config file.");
        //     System.exit(1);
        // } catch (IOException ex) {
        //     System.out.println("Error reading config file.");
        //     System.exit(1);
        // }
        // String username = (prop.getProperty("username"));
        // String password = (prop.getProperty("password"));

        // if (username == null || password == null){
        //     System.out.println("Username or password not provided.");
        //     System.exit(1);
        // }

        // String connectionUrl =
        //         "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
        //         + "database=cs3380;"
        //         + "user=" + username + ";"
        //         + "password="+ password +";"
        //         + "encrypt=false;"
        //         + "trustServerCertificate=false;"
        //         + "loginTimeout=30;";

        // ResultSet resultSet = null;

        // try (Connection connection = DriverManager.getConnection(connectionUrl);
        //         Statement statement = connection.createStatement();) {

        //     // Create and execute a SELECT SQL statement.
        //     String selectSql = "SELECT firstname, lastname, provinces.name from people join provinces on people.provinceID = provinces.provinceID;";
        //     resultSet = statement.executeQuery(selectSql);

        //     // Print results from select statement
        //     while (resultSet.next()) {
        //         System.out.println(resultSet.getString(1) + 
        //         " " + resultSet.getString(2) +
        //         " lives in " + resultSet.getString(3));
        //     }
        // }
        // catch (SQLException e) {
        //     e.printStackTrace();
        // }

        readCSV("D:\\Uni\\3rd year\\3380\\GroupProject\\Entertainment-Small.csv");
    }

 
    public static void readCSV(String fileName) {
        
        //reading the csv file
        List<String> file1 = new ArrayList<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lineString = reader.readLine();
            while ((lineString = reader.readLine()) != null) {
                
                String[] line = lineString.split(",");
                String type = line[0];
                String title = line [1];
                ArrayList<String> director = new ArrayList<String>();
                boolean flag = true;
                int j;
                for(j = 2; j < line.length && flag; j++){
                    if(line[j].equals("|")){
                        flag = false;
                    }
                    else{
                        director.add(line[j]);
                    }
                }
                ArrayList<String> cast = new ArrayList<String>();
                flag = true;
                while(j < line.length && flag){
                    if(line[j].equals("|")){
                        flag = false;
                    }
                    else{
                        cast.add(line[j]);
                    }
                    j++;
                }
                ArrayList<String> country = new ArrayList<String>();
                flag = true;
                while(j < line.length && flag){
                    if(line[j].equals("|")){
                        flag = false;
                    }
                    else{
                        country.add(line[j]);
                    }
                    j++;
                }
                String date = line[j];
                String release_year = line[j+1];
                String rated = line[j+2];
                String duration = line[j+3];
                String IMDB = line[j+4];
                String rotten = line[j+5];
                String platform = line[j+6];
                ArrayList<String> listed_in = new ArrayList<String>();
                flag = true;
                for(j = j+7; j < line.length && flag; j++){
                    if(line[j].equals("|")){
                        flag = false;
                    }
                    else{
                        listed_in.add(line[j]);
                    }
                }
                String description = ""; 
                while(j < line.length){
                    if(j == line.length-1){
                        description += line[j];
                    }
                    else{
                        description += line[j] + ",";
                    }
                    j++;
                }
                System.out.println("Type: "+type);
                System.out.println("Title: "+title);
                System.out.println("Directors: "+director);
                System.out.println("Cast: "+cast);
                System.out.println("Country: " +  country);
                System.out.println("Date: "+date);
                System.out.println("Release Year: "+release_year);
                System.out.println("Rated: "+rated);
                System.out.println("Duration: "+duration);
                System.out.println("IMDB: "+IMDB);
                System.out.println("Rotten: "+rotten);
                System.out.println("Platform: "+platform);
                System.out.println("Listed in: "+listed_in);
                System.out.println("Description: "+ description.trim());
                System.out.println("-------------------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
