import java.beans.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

import org.apache.ibatis.executor.ReuseExecutor;


public class EntertainmentDB {

    private static Connection connection;
    public static void main(String[] args) {
        connection = SetUPConnection.getConnection();
		runConsole();
    }

    public static void runConsole() {

		Scanner console = new Scanner(System.in);
		System.out.print("Welcome! Type h for help. ");
		System.out.print("db > ");
		String line = console.nextLine();
		String[] parts;
		String arg = "";

		while (line != null && !line.equals("q")) {

			try{
			
			parts = line.split("\\s+");
			if (line.indexOf(" ") > 0)
				arg = line.substring(line.indexOf(" ")).trim();

			if (parts[0].equals("h"))
				printHelp();
			else if (parts[0].equals("title")) {
				
				title(arg);
			}

			 else if (parts[0].equals("cMovie")) {
				
			 	if (parts.length == 4){
					 String actorName = parts[1] + " " + parts[2];	
					 Float IMDB = Float.parseFloat(parts[3]);
					cMovie(actorName,IMDB);
				}else{
                    System.out.println("Enter the right input: cMovie firstName LastName IMDB");
                }
				
			}	
			else if (parts[0].equals("recentMovie")) {
					if (parts.length == 3){
						String actorName = parts[1] + " " + parts[2];
						recentMovie(actorName);
					} 
					else
						System.out.println("recentMovie firstName lastName");
				
			}

			else if (parts[0].equals("director10")) {
				
					if (parts.length == 1)
						director10();
					else
						System.out.println("Require an argument for this command");
                    }
               

			else if (parts[0].equals("streamedOn")) {
                    
					if (parts.length == 2){
						int noOfPlatforms = Integer.parseInt(parts[1]);
						streamedOn(noOfPlatforms);
					}
						
					else{
						System.out.println("Require an argument for this command");
                    }				
			}

			else if (parts[0].equals("platformYear")) {
				parts = line.split(",");
				String[] platfrom = parts[0].split("\\s+");
				String service = "";
				if(platfrom.length == 2){
					service = platfrom[1];
				} else if( platfrom.length == 3){
					service = platfrom[1] + " " + platfrom[2];
				}

				int year = Integer.parseInt(parts[1].trim());

				platformYear(service, year);

			 } else if (parts[0].equals("movieGenre")) {
				System.out.println(arg);
				movieGenre(arg);
			}

			 else if (parts[0].equals("mostCast")) {
				mostCast();
			}

			else if (parts[0].equals("leastCast")) {
				leastCast();
			}

			else if (parts[0].equals("noDirectors")) {
				noDirectors();
			}
			
			else if (parts[0].equals("castTogether")) {
				if(parts.length == 5){
					String cast1 = parts[1] + " " + parts[2];
                    String cast2 = parts[3] + " " + parts[4];
					castTogether(cast1,cast2);
                }else{
                    System.out.println("please provide castTogether cast1firstName cast1lastname cast2firstname cast2lastname");
				}

			}
            else if (parts[0].equals("countryMade")) {
				countryMade();
			}
            else if (parts[0].equals("childTV")) {
				childTV();
			}
            
			else if (parts[0].equals("shortFilm")) {
				shortFilm();
			}
            else if (parts[0].equals("showSeasons")) {
				if(parts.length == 2){
					int noOfSeasons = Integer.parseInt(parts[1]);
					showSeasons(noOfSeasons);
				}
			}
            else if (parts[0].equals("internationalMovies")) {
				internationalMovies();
			}
			else if (parts[0].equals("movieCountry")) {
				movieCountry(arg);
			}
			else if (parts[0].equals("moreGenre")) {
				moreGenre();
			}
			else if (parts[0].equals("directorTheme")) {
				directorTheme(arg);
			}
			else if (parts[0].equals("aboveAverage")) {
				String platformName = "";
				String ratedBy = "";
				if(parts.length == 4){
					platformName = parts[1] + " " +parts[2];
					ratedBy = parts[3];
					aboveAverage(platformName, ratedBy);
				}else if(parts.length== 3){
                    platformName = parts[1];
                    ratedBy = parts[2];
                    aboveAverage(platformName, ratedBy);
                }else{
                    System.out.println("Correct command: aboveAverage platformName ratingType(could be imdb or rotten)");
                }

				
			}
			else
				System.out.println("Read the help with h, or find help somewhere else.");
			
			} catch (Exception e){
				System.out.println("Error in line: " + line);
			}
			System.out.print("db > ");
			line = console.nextLine();
		}
		console.close();
}


	private static void printHelp() {
		
		System.out.println("Entertainment database");
		System.out.println("Commands:");
		System.out.println("h - Get help");
		System.out.println("title <search> - search for title and get all the information regarding the title");
		System.out.println("cMovie <actorFirstName> <actorLastName> <IMDB> - Top shows for this person according wiht given IMDB rating");
		System.out.println("recentMovie <actorFirstName> <actorLastName> - 10 most recent TV/Movie shows of this actor");
		System.out.println("director10 - directors who have directed more than 10 movies/tv shows");
		System.out.println("streamedOn  <no. of platform> - returns all title stereamed on no. of platform provided i.e. if passed 2, then \nit will return titles that are streamed on 2 different platforms");
		System.out.println("platformYear <platformName>, <releaseYear> - Top 5 title on given platform by given year");
		System.out.println("movieGenre <genreName> - Top 5 movies according to based on the given genre");
		System.out.println("mostCast - Top 5 Titles with most cast involved");
		System.out.println("leastCast - Top 5 title with least cast involved");
		System.out.println("noDirectors - Title's with no directors");
		System.out.println("castTogether <cast1firstName> <cast1lastName> <cast2firstName> <cast2LastName> - Titles in which given cast have worked together");
		System.out.println("countryMade - Top 5 countries with most titles were made");
		System.out.println("childTV - Top 5 popular titles that are rated child friendly");
		System.out.println("shortFilm - All moviest that has duration under 1 hour");
		System.out.println("showSeasons <no. of seasons> - All tv shows that have give number of seasons");
		System.out.println("internationalMovies - All movies that are made in more than 2 countries ");
		System.out.println("movieCountry <countryName> - Top 5 movies in given countries");
		System.out.println("moreGenre - Titles with more than four genre's");
		System.out.println("directorTheme <directorName> - provides the genre for which directed is most known for");
		System.out.println("aboveAverage <platformName> <imdb or rotten> - Given platform and being rated by, gives all the titles that have above average ratings");
		System.out.println("");

		System.out.println("q - Exit the program");

		System.out.println("---- end help ----- ");
	}




    private static void title(String titleName){
        try{
		 
			String sql = "select entertainment.mediaName as Title ,entertainment.IMDB as IMDB_Rating ,entertainment.duration as Movie_Duration,\n" +
								"    entertainment.mediaDescription as Description, entertainment.releaseYear as ReleaseYear ,entertainment.rated as rating,entertainment.rottenTomatoes as rottenTomatoes,\n" +
								"    genre.genreName as Genre,country.countryName as country, director.dirName as Director, Cast.castName as casts\n" +
								"    \n" +
								"    from entertainment join isA on entertainment.mediaId =isA.mediaId\n" +
								"    join mediaGenre on entertainment.mediaID = mediaGenre.mediaID\n" +
								"    join genre on mediaGenre.genreID = genre.genreID\n" +
								"    join directedBy on entertainment.mediaID = directedBy.mediaID\n" +
								"    join director on directedBy.dirID = director.dirID\n" +
								"    join castInvolved on entertainment.mediaID = castInvolved.mediaID\n" +
								"    join cast on castInvolved.castID = cast.castID\n" +
								"    join madeIn on entertainment.mediaID = madeIn.mediaID\n" +
								"    join country on madeIn.countryID = country.countryID \n" +
								"    WHERE entertainment.mediaName LIKE ?";


			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + titleName + "%");
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.next();

			String title = resultSet.getString("Title");

			System.out.println("Title: " + title + "\n" + "IMDB: " +resultSet.getFloat("IMDB_Rating") + "\n"
			+ "Duration: " + resultSet.getInt("Movie_Duration") + "\n" + "Description: "+ resultSet.getString("Description")+ "\n" +
			"Release Year:  "+resultSet.getInt("ReleaseYear") + "\n" + "Rating: "+resultSet.getString("rating")
			+ "\n" + "Rotten Tomatoes: "+resultSet.getInt("rottenTomatoes") );

            List<String> genre = new ArrayList<>();
            List<String> director = new ArrayList<>();
			List<String> cast = new ArrayList<>();
			boolean isSameTitle;
         
			do{
				isSameTitle = title.equals(resultSet.getString("Title"));
				
				genre.add(resultSet.getString("Genre"));
                director.add(resultSet.getString("Director"));
                cast.add(resultSet.getString("casts"));

			} while (resultSet.next() && isSameTitle );
			
			List<String> dupGenre = genre.stream().distinct().collect(Collectors.toList());
			List<String> dupDir = director.stream().distinct().collect(Collectors.toList());
			List<String> dupCast = cast.stream().distinct().collect(Collectors.toList());

			System.out.println("Genre list: " + dupGenre );
            System.out.println("Director list: "+ dupDir);
			System.out.println("Cast list: " + dupCast);
			
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
		e.printStackTrace(System.out);
	   }
	}

	private static void cMovie(String actorName, float IMDBRating){
		try{
		 
			String sql = "SELECT mediaName,castName,IMDB from cast join castInvolved on cast.castID=castInvolved.castID join entertainment on castInvolved.mediaID=entertainment.mediaID\n" +
				"where cast.castName  like ? and entertainment.IMDB >= ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,"%" + actorName + "%");
			statement.setFloat(2, IMDBRating);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.printf("Media Name : %s \nCast : %s \nIMDB: %.1f " , resultSet.getString("mediaName"),resultSet.getString("castName") ,resultSet.getFloat("IMDB") );

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}


 private static void recentMovie(String actorName){

		try{
			
			String sql = "SELECT top 10 mediaName,releaseYear \n" +
                    "from cast join castInvolved on cast.castID=castInvolved.castID \n" +
                    "join entertainment on castInvolved.mediaID=entertainment.mediaID \n" +
                    "where cast.castName= ? \n" +
                    "order by releaseYear desc";


			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, actorName );
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Name : " + resultSet.getString("mediaName") + "\nRelease Year : " + resultSet.getInt("releaseYear") + " ");

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
        
            
    }
    
      private static void director10(){
        try{
		 
			String sql = "     select Director.dirID , dirName\n" +
                    "\t from Director\n" +
                    "\t where dirId in (\n" +
                    "\t \tselect dirID\n" +
                    "         from directedBy\n" +
                    "\t \tgroup by dirID\n" +
                    "\t \thaving count(*)>=10\n" +
                    "\t )";
					
					PreparedStatement statement = connection.prepareStatement(sql);
			        ResultSet resultSet = statement.executeQuery();

					while (resultSet.next()) {
						System.out.println("Director ID : " + resultSet.getString("dirID") + "\nDirector : " + resultSet.getString("dirName") + " ");

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
    }
	
    private static void streamedOn(int noOfPlatform){
		try{
		 
			String sql = "select mediaID, mediaName\n" +
                    "\t from Entertainment\n" +
                    "\t where mediaId in (\n" +
                    "\t select mediaId\n" +
                    "\t from streamedOn\n" +
                    "\t group by MediaId\n" +
                    "\t having count(*) = ?\n" +
                    "\t);";


			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, noOfPlatform);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Media ID : " + resultSet.getString("mediaID") + "\n Media Name : " + resultSet.getString("mediaName") + " ");

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
    }


  private static void platformYear(String platformName, int releaseYear){	
        
		int platformID = platformMapping(platformName);
        try{
			String sql = "SELECT top 5 mediaName \n" +
                    "from streamedOn join entertainment on streamedOn.mediaID=entertainment.mediaID\n" +
                    "where releaseYear=? and platformID=? \n" +
                    "order by IMDB desc;";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, releaseYear);
			statement.setInt(2, platformID);
			
			ResultSet resultSet = statement.executeQuery();
			System.out.println("Title : ");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
					
    }

	private static int platformMapping(String name){
		int id = 1;

		if(name.toLowerCase().equals("amazon prime")){
			id = 2;
		}else if(name.toLowerCase().equals("disney plus")){
            id = 3;
        }else if(name.toLowerCase().equals("hulu")){
            id = 4;
        }

		return id;
	}

 private static void movieGenre(String genreName)
	{
		try{
			String sql =     "SELECT top 5 MediaName \n" +
			"from entertainment \n" + 
			"join mediaGenre ON mediaGenre.mediaID = entertainment.mediaID \n" +
			"join genre on genre.genreID = mediaGenre.genreID \n" +
			"where genreName like ? \n" +
			"order by IMDB desc;";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,"%" + genreName + "%");
			ResultSet resultSet = statement.executeQuery();
			System.out.println("Title : ");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}

	private static void mostCast()
	{
		try{
		 
			String sql = "SELECT top 5 entertainment.mediaName, count(*) as casts\n" +
                    " from entertainment join castInvolved on entertainment.mediaID=castInvolved.mediaID\n" +
                    " group by entertainment.mediaName\n" +
                    " order by casts desc;";
					
					PreparedStatement statement = connection.prepareStatement(sql);
			        ResultSet resultSet = statement.executeQuery();
					
					while (resultSet.next()) {
						System.out.println("\nTitle: " + resultSet.getString("mediaName") + " totalCast : " + resultSet.getInt("casts"));

					}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }	
	}

	private static void leastCast(){
		try{
		 
			String sql = "SELECT top 5 mediaName, count(*) as casts\n" +
                    " from entertainment left join castInvolved on entertainment.mediaID=castInvolved.mediaID\n" +
                    " group by entertainment.mediaName\n" +
                    " order by casts;";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println( "\nTitle: " + resultSet.getString("mediaName") + " totalCast : " + resultSet.getInt("casts"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
	

   	private static void noDirectors(){
		try{
		 
			String sql = "SELECT mediaName \n" +
                    "from entertainment join directedBy on entertainment.mediaId=directedBy.mediaID\n" +
                    "where directedBy.dirId = 0";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Media Name : " + resultSet.getString("mediaName"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
   }
   
  private static void castTogether(String cast1, String cast2){
		try{
		 
			String sql = "SELECT mediaName\n" +
                    " from cast join castInvolved on cast.castId=castInvolved.castID\n" +
                    " join entertainment on castInvolved.mediaId=entertainment.mediaID\n" +
                    " where cast.castName = ?\n" +
                    " INTERSECT\n" +
                    " SELECT mediaName\n" +
                    " from cast join castInvolved on cast.castId=castInvolved.castID\n" +
                    " join entertainment on castInvolved.mediaId=entertainment.mediaID\n" +
                    " where cast.castName = ?";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, cast1);
			statement.setString(2, cast2);
			ResultSet resultSet = statement.executeQuery();
			System.out.println("Title : ");

			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));
			}
			
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
   }
	
	  private static void countryMade(){
		try{
		 
			String sql = "SELECT top 5 countryName,COUNT(*) as made \n" +
                    "from country join madeIn on country.countryId=madeIN.countryID\n" +
                    " group by country.countryName\n" +
                    " order by made desc";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Country : " + resultSet.getString("countryName") + " Total title: " + resultSet.getInt("made"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
	private static void childTV(){
		try{
			
			
			String sql = "Select TOP 5 mediaName \n" +
                    "    from entertainment \n" +
                    "    where rated = 'PG' or rated=  'TV-Y7'or rated = '13+' or rated = '7+' " + 
					"	 order by IMDB Desc";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			System.out.println("Title : ");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}

	private static void shortFilm(){
		try{
		 
			String sql = "select medianame \n" +
                    "    from entertainment join isA on entertainment.mediaId = isA.mediaId\n" +
                    "    where entertainment.duration < 60 and isA.mediaTypeId = 1;";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			System.out.println("Title : ");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));
			}
			resultSet.close();
			statement.close();

	    }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
	
private static void showSeasons(int numberOfSeasons){
		try{
		 
			String sql = "Select mediaName,duration\n" +
                    " from entertainment join isA on entertainment.mediaId=isA.mediaID\n" +
                    " where isA.mediaTypeID = 2 and entertainment.duration = ?; ";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, numberOfSeasons);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Title : " + resultSet.getString("mediaName") + "\nSeasons : " + resultSet.getInt("duration") );

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}

		private static void internationalMovies(){
		try{
		 
			String sql = "SELECT entertainment.mediaID, entertainment.mediaName, COUNT(countryID) as Total_Countries FROM \n" +
                    " entertainment join madeIn on entertainment.mediaID = madeIn.mediaID\n" +
                    " join isA on entertainment.mediaID = isA.mediaID\n" +
                    " where mediaTypeID = 1 \n" +
                    " GROUP BY entertainment.mediaName, entertainment.mediaID\n" +
                    " having COUNT(countryID) > 2 ";
					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
		

			while (resultSet.next()) {
				System.out.println("ID : " + resultSet.getString("mediaID") + "\t Title : " + resultSet.getString("mediaName") 
					+ "\tTotal Countries : " + resultSet.getString("Total_Countries"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}

	private static void movieCountry(String countryName){
		try{
			String sql = "with moviesInCountry (mediaName, IMDB) as\n" +
                    "(select mediaName, IMDB\n" +
                    "from entertainment\n" +
                    "join isA on entertainment.mediaId = isA.mediaId\n" +
                    "join madeIn on isA.mediaID = madeIn.mediaId\n" +
                    "join country on madeIn.countryID = country.countryID\n" +
                    "where country.countryName = ? and isA.mediaTypeID = 1 )\n" +
                    "Select top 5 mediaName, IMDB\n" +
                    "from moviesInCountry \n" +
                    "order by IMDB desc";


			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, countryName);
			ResultSet resultSet = statement.executeQuery();
			System.out.println("Name : ");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("mediaName"));
			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
		private static void moreGenre(){
		try{
		 
			String sql = "with moreThanTwoGenre ( mediaId, mediaName, genreID) as \n" +
                    "     (\n" +
                    "         SELECT entertainment.mediaID, entertainment.mediaName, count(mediaGenre.genreID) FROM entertainment \n" +
                    "         join mediaGenre on entertainment.mediaID = mediaGenre.mediaID\n" +
                    "         group by mediaGenre.mediaID, entertainment.mediaName, entertainment.mediaID\n" +
                    "         having COUNT(*) > 4 \n" +
                    "     )\n" +
                    "     SELECT  moreThanTwoGenre.mediaName, genre.genreName from moreThanTwoGenre \n" +
                    "     join mediaGenre on moreThanTwoGenre.mediaId = mediaGenre.mediaID\n" +
                    "     join genre on mediaGenre.genreID = genre.genreID\n" +
                    "     order by moreThanTwoGenre.mediaID";

					
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println("Name : " + resultSet.getString("mediaName") + "\nGenre Name : " + resultSet.getString("genreName"));

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
	private static void directorTheme(String directorName){
		try{
			String sql = "select top 1 director.dirName, genre.genreName,count(*) as genres from \n" +
                    " director join directedBy on director.dirID = directedBy.dirID \n" +
                    " join mediaGenre on directedBy.mediaID = mediaGenre.mediaID \n" +
                    " join genre on mediaGenre.genreId = genre.genreID \n" +
                    " where director.dirName = ? \n" +
                    " group by director.dirName, genre.genreName\n" +
                    " order by genres desc";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, directorName);
			ResultSet resultSet = statement.executeQuery();
													
			while (resultSet.next()) {
				System.out.println("Director Name: " + resultSet.getString("dirName") + "\n" + "Theme of director: " + resultSet.getString("genreName") +
					 " Total genre : " + resultSet.getString("genres") );

			}
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
	
	private static void aboveAverage(String platformName,String ratingType ){
		try{
			int platformID = platformMapping(platformName);
			String sql = "";
			if(ratingType.equalsIgnoreCase("imdb")) {
			//Select movies/tv shows from a particular platform with rating above average (average rating of that platform)\n
			sql = " SELECT mediaName, IMDB from entertainment\n" +
                    " where IMDB > (\n" +
                    "     SELECT  AVG(IMDB)\n" +
                    "     from \n" +
                    "     entertainment join streamedOn on entertainment.mediaID = streamedOn.mediaID\n" +
                    "     where platformID = ?\n" +
                    "    GROUP BY platformID\n" +
                    "    having AVG(IMDB) > 0\n" +
                    " )";
			}
			else if( ratingType.equalsIgnoreCase("rotten")){
			//Select movies/tv shows from a particular platform with rating above average (average rating of that platform)\n
			 sql = " SELECT mediaName, rottenTomatoes from entertainment\n" +
                    " where rottenTomatoes > (\n" +
                    "     SELECT  AVG(rottenTomatoes)\n" +
                    "     from \n" +
                    "     entertainment join streamedOn on entertainment.mediaID = streamedOn.mediaID\n" +
                    "     where platformID = ?\n" +
                    "    GROUP BY platformID\n" +
                    "    having AVG(rottenTomatoes) > 0\n" +
                    " )";
			 
			
			}

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, platformID);
			ResultSet resultSet = statement.executeQuery();
			System.out.println();
			while (resultSet.next()) {
				if(ratingType.equalsIgnoreCase("imdb"))
					// System.out.println("Title : " + resultSet.getString("mediaName") + " Rating : " + resultSet.getString("IMDB"));
					System.out.printf("Title : %s Rating : %.1f \n", resultSet.getString("mediaName"),resultSet.getFloat("IMDB"));
				else
					System.out.println("Title : " + resultSet.getString("mediaName") + " Rating : " + resultSet.getString("rottenTomatoes"));
                    
            }
			resultSet.close();
			statement.close();

	   }catch(SQLException e){
			e.printStackTrace(System.out);
	   }
	}
 
}
    


   
