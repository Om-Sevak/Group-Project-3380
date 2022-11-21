import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;


public class PopulateDB {
    private static Connection connection;

// Connect to your database.
// Replace server name, username, and password with your credentials
public PopulateDB(Connection connection){
    PopulateDB.connection = connection;
}
public void populate(){
   
        long startime = System.currentTimeMillis();

        System.out.println("GOOD THINGS TAKE TIME...");

        populateEntertainment("DatabaseFiles\\Entertainment.csv");
        populateCast("DatabaseFiles\\Cast.csv");
        populateDirectors("DatabaseFiles\\Director.csv");
        populateCountry("DatabaseFiles\\Country.csv");
        populateGenre("DatabaseFiles\\Genre.csv");
        populateIsA("DatabaseFiles\\IsA.csv");
        populateStreamedOn("DatabaseFiles\\StreamedOn.csv");
        populateDirectedBy("DatabaseFiles\\DirectedBy.csv");
        populateMadeIn("DatabaseFiles\\MadeIn.csv");
        populateCastInvolved("DatabaseFiles\\CastInvolved.csv");
        populateMediaGenre("DatabaseFiles\\MediaGenre.csv");

        long endtime = System.currentTimeMillis();
        long secs = (endtime - startime)/1000;
        System.out.println("It took " + secs + " seconds to populate the database.");
}

public static void populateEntertainment(String filName){
    int corruptedRows = 0;
    int totalRowInserted = 0;
    Random rd;
    boolean isLastLine = false;
    try{
   
    BufferedReader reader = new BufferedReader(new FileReader(filName));
    String lineString = reader.readLine();
    
    String query = "Insert into entertainment(mediaID, mediaName, releaseYear, rated, duration, IMDB, rottenTomatoes, mediaDescription) values(?,?,?,?,?,?,?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating Entertainment table...");
    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setString(2, line[1]);
                }
                else{
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
            
                if(!(line[2].equals(""))){
                    statement.setInt(3, Integer.parseInt(cleanString(line[2])));
                }
                else{
                    statement.setNull(3, java.sql.Types.INTEGER);
                }
                
                if(!(line[3].equals(""))){
                    statement.setString(4, line[3]);
                }
                else{
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }

                if(!(line[4].equals(""))){
                    statement.setInt(5, Integer.parseInt(cleanString(line[4])));
                }
                else{
                    statement.setNull(5, java.sql.Types.INTEGER);
                }
                
                if(!(line[5].equals(""))){

                    rd = new Random(); 
                    if( line[5].equals("-1"))
                        statement.setFloat(6,(rd.nextInt(10) + rd.nextFloat()));
                    else
                        statement.setFloat(6, Float.parseFloat(cleanString(line[5])));
                }
                else{
                    statement.setNull(6, java.sql.Types.DOUBLE);
                }
            
                if(!(line[6].equals(""))){
                    if( line[6].equals("-1") )    
                        statement.setInt(7, getRandomNumber(0, 101));
                    else
                        statement.setInt(7, Integer.parseInt(cleanString(line[6])));
                }
                else{
                    statement.setNull(7, java.sql.Types.DOUBLE);
                }
            
                if(!(line[7].equals(""))){
                    statement.setString(8, line[7]);
                }
                else{
                    statement.setNull(8, java.sql.Types.VARCHAR);
                }
                
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
            corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
           // e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating entertainment table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);

}

public static void populateCast(String fileName){
    
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;
    try{
   
    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into cast(castID, castName) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating Cast table...");
    
    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
            
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setString(2, line[1]);
                }
                else{
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
            corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
           // e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating cast table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);

}

public static void populateDirectors(String fileName){
        
        int corruptedRows = 0;
        int totalRowInserted = 0;
        boolean isLastLine = false;
    
        try{
    
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineString = reader.readLine();
        
        String query = "Insert into director(dirID, dirName) values(?,?)";
        PreparedStatement statement =  connection.prepareStatement(query);
    
        int batchTracker = 0;
    
        System.out.println("Populating Directors table...");
        
        while(!isLastLine){
            isLastLine = (lineString = reader.readLine()) == null;
            if(!isLastLine){
                try{
                
                    String[] line = lineString.split(",");
                    if(!(line[0].equals(""))){
                        statement.setInt(1, Integer.parseInt(line[0]));
                    }
                    else{
                        statement.setNull(1, java.sql.Types.INTEGER);
                    }
        
                    if(!(line[1].equals(""))){
                        statement.setString(2, line[1]);
                    }
                    else{
                        statement.setNull(2, java.sql.Types.VARCHAR);
                    }
                
                    statement.addBatch();
                    batchTracker++;
                    totalRowInserted++;
                }catch(Exception e){
                    //System.out.println("Error in line: " + lineString);
                    corruptedRows++;
                }
        }
            try{
                if(batchTracker == 5000 || isLastLine){
                    statement.executeBatch();
                    batchTracker = 0;
                }
            }catch(SQLException e){
                 e.printStackTrace();
                batchTracker = 0;
            }
        }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e){
            //e.printStackTrace();
        }
    
        System.out.println("Completed populating director table.");
        System.out.println("Total rows inserted: " + totalRowInserted);
        System.out.println("Total corrupted rows bypassed: " + corruptedRows);
    
}

public static void populateCountry(String fileName){
        
        int corruptedRows = 0;
        int totalRowInserted = 0;
        boolean isLastLine = false;
        try{
    
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineString = reader.readLine();
        
        String query = "Insert into country(countryID, countryName) values(?,?)";
        PreparedStatement statement =  connection.prepareStatement(query);
    
        int batchTracker = 0;
    
        System.out.println("Populating Country table...");

        while(!isLastLine){
            isLastLine = (lineString = reader.readLine()) == null;
            if(!isLastLine){
                try{
                
                    String[] line = lineString.split(",");
                    if(!(line[0].equals(""))){
                        statement.setInt(1, Integer.parseInt(line[0]));
                    }
                    else{
                        statement.setNull(1, java.sql.Types.INTEGER);
                    }
    
                    if(!(line[1].equals(""))){
                        statement.setString(2, line[1]);
                    }
                    else{
                        statement.setNull(2, java.sql.Types.VARCHAR);
                    }
                
                    statement.addBatch();
                    batchTracker++;
                    totalRowInserted++;
                }catch(Exception e){
                    //System.out.println("Error in line: " + lineString);
                    corruptedRows++;
                    e.printStackTrace();
                }
            }
            try{
                if(batchTracker == 100 || isLastLine){
                    statement.executeBatch();
                    batchTracker = 0;
                }
            }catch(SQLException e){
             e.printStackTrace();
                batchTracker = 0;
            }
        }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    
        System.out.println("Completed populating country table.");
        System.out.println("Total rows inserted: " + totalRowInserted);
        System.out.println("Total corrupted rows bypassed: " + corruptedRows);
    
}

public static void populateGenre(String fileName){
            
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into genre(genreID, genreName) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating Genre table...");
   
    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
            
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setString(2, line[1]);
                }
                else{
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 100 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating genre table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
        
}

public static void populateIsA(String fileName){
        
        int corruptedRows = 0;
        int totalRowInserted = 0;
        boolean isLastLine = false;
    
        try{
    
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineString = reader.readLine();
        
        String query = "Insert into isA(mediaID, mediaTypeID) values(?,?)";
        PreparedStatement statement =  connection.prepareStatement(query);
    
        int batchTracker = 0;
    
        System.out.println("Populating IsA table...");
    
        while(!isLastLine){
            isLastLine = (lineString = reader.readLine()) == null;
            if(!isLastLine){
                try{
                
                    String[] line = lineString.split(",");
                    if(!(line[0].equals(""))){
                        statement.setInt(1, Integer.parseInt(line[0]));
                    }
                    else{
                        statement.setNull(1, java.sql.Types.INTEGER);
                    }
    
                    if(!(line[1].equals(""))){
                        statement.setInt(2, Integer.parseInt(line[1]));
                    }
                    else{
                        statement.setNull(2, java.sql.Types.INTEGER);
                    }
                
                    statement.addBatch();
                    batchTracker++;
                    totalRowInserted++;
                }catch(Exception e){
                    //System.out.println("Error in line: " + lineString);
                    corruptedRows++;
                }
            }
            try{
                if(batchTracker == 5000 || isLastLine){
                    statement.executeBatch();
                    batchTracker = 0;
                }
            }catch(SQLException e){
                //e.printStackTrace();
                batchTracker = 0;
            }
        }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e){
            //e.printStackTrace();
        }
    
        System.out.println("Completed populating isA table.");
        System.out.println("Total rows inserted: " + totalRowInserted);
        System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static void populateStreamedOn(String fileName){
        
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into streamedOn(mediaID, platformID, dateAdded) values(?,?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating StreamedOn table...");

    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
            
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setInt(2, Integer.parseInt(line[1]));
                }
                else{
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
                if(!(line[2].equals(""))){
                    statement.setString(3, line[2]);
                }
                else{
                    statement.setNull(3, java.sql.Types.DATE);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating streamedOn table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static void populateDirectedBy(String fileName){
            
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into directedBy(mediaID, dirID) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating DirectedBy table...");

    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setInt(2, Integer.parseInt(line[1]));
                }
                else{
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating directedBy table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static void populateMadeIn(String fileName){
            
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into madeIn(mediaID, countryID) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating MadeIn table...");

    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setInt(2, Integer.parseInt(line[1]));
                }
                else{
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating madeIn table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static void populateCastInvolved(String fileName){
            
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into castInvolved(mediaID, castID) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating CastInvolved table...");

    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setInt(2, Integer.parseInt(line[1]));
                }
                else{
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 2000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating castInvolved table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static void populateMediaGenre(String fileName){
                
    int corruptedRows = 0;
    int totalRowInserted = 0;
    boolean isLastLine = false;

    try{

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String lineString = reader.readLine();
    
    String query = "Insert into mediaGenre(mediaID, genreID) values(?,?)";
    PreparedStatement statement =  connection.prepareStatement(query);

    int batchTracker = 0;

    System.out.println("Populating MediaGenre table...");

    while(!isLastLine){
        isLastLine = (lineString = reader.readLine()) == null;
        if(!isLastLine){
            try{
                String[] line = lineString.split(",");
                if(!(line[0].equals(""))){
                    statement.setInt(1, Integer.parseInt(line[0]));
                }
                else{
                    statement.setNull(1, java.sql.Types.INTEGER);
                }

                if(!(line[1].equals(""))){
                    statement.setInt(2, Integer.parseInt(line[1]));
                }
                else{
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
            
                statement.addBatch();
                batchTracker++;
                totalRowInserted++;
            }catch(Exception e){
                //System.out.println("Error in line: " + lineString);
                corruptedRows++;
            }
        }
        try{
            if(batchTracker == 5000 || isLastLine){
                statement.executeBatch();
                batchTracker = 0;
            }
        }catch(SQLException e){
            //e.printStackTrace();
            batchTracker = 0;
        }
    }
    } catch (IOException e){
        e.printStackTrace();
    } catch (SQLException e){
        //e.printStackTrace();
    }

    System.out.println("Completed populating mediaGenre table.");
    System.out.println("Total rows inserted: " + totalRowInserted);
    System.out.println("Total corrupted rows bypassed: " + corruptedRows);
            
}

public static String cleanString(String str){
    return str.replaceAll("\"", "");
}

public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
}

}
