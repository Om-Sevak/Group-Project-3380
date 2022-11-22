--print entertainment with directors
-- SELECT * from entertainment;
 --select * from cast;
 --select * from director;
 --SELECT * from country;
 --SELECT * from genre;
--SELECT * from isA;
 --SELECT * from mediaType;
 --SELECT * from platform;
--SELECT * from mediaGenre;
 --SELECT * from madeIn;
 --SELECT * from castInvolved;
--SELECT * FROM entertainment JOIN directedBy ON entertainment.mediaID = directedBy.mediaID JOIN director ON directedBy.dirID = director.dirID JOIN streamedOn ON entertainment.mediaID = streamedOn.mediaID JOIN platform ON streamedOn.platformID = platform.platformID JOIN madeIn ON entertainment.mediaID = madeIn.mediaID JOIN country ON madeIn.countryID = country.countryID JOIN castInvolved ON entertainment.mediaID = castInvolved.mediaID JOIN cast ON castInvolved.castID = cast.castID JOIN isA ON entertainment.mediaID = isA.mediaID JOIN mediaType ON isA.mediaTypeID = mediaType.mediaTypeID ;
--select mediaName, platformID from entertainment JOIN streamedOn ON entertainment.mediaID = streamedON.mediaID;
--select mediaName, countryName from entertainment JOIN madeIn ON entertainment.mediaID = madeIN.mediaID JOIN country ON madeIn.countryID = country.countryID ;
--select mediaName, castName from entertainment JOIN castInvolved ON entertainment.mediaID = castInvolved.mediaID JOIN cast ON castInvolved.castID = cast.castID ;
--select mediaName, mediaTypeName from entertainment JOIN isA ON entertainment.mediaID = isA.mediaID JOIN mediaType ON isA.mediaTypeID = mediaType.mediaTypeID ;


-- Movies of a given cast whose IMDB is more than 8(var)
--SELECT mediaName,castName from cast join castInvolved on cast.castID=castInvolved.castID join entertainment on castInvolved.mediaID=entertainment.mediaID
--where cast.castID=6579 and entertainment.IMDB>=8;

-- 10 most recent release year tv-shows/movies of a given actor/director
--SELECT top 10 mediaName,releaseYear 
--from cast join castInvolved on cast.castID=castInvolved.castID 
--join entertainment on castInvolved.mediaID=entertainment.mediaID 
--where cast.castID=6579 
--order by releaseYear desc;

-- Select directors who have directed more than 10 TVSHOWS/MOVIES
  --select directors who have directed more than 10 movies
    -- select Director.dirName , dirID
	-- from Director
	-- where dirId in (
	-- 	select dirID
    --     from directedBy
	-- 	group by dirID
	-- 	having count(*)>=10
	-- );

-- Select entertainment which is streamed on  3 no. of platforms
	-- select mediaID, mediaName
	-- from Entertainment
	-- where mediaId in (
	-- 	select mediaId
	-- 	from streamedOn
	-- 	group by MediaId
	-- 	having count(*) = 3
	-- );



-- Top 5 shows on platform according to a given year
--SELECT top 5 mediaName 
--from streamedOn join entertainment on streamedOn.mediaID=entertainment.mediaID
--where releaseYear=2021 and platformID=1
--order by IMDB desc;

-- Top 5 movies particular to a genre using genreID
/* SELECT top 5 MediaName 
from genre join isA on genre.genreID=isA.mediaTypeID 
join entertainment on isA.mediaID=entertainment.mediaID
where genreID=1 
order by IMDB desc; */

-- -- Movies/Shows which most casts involved

-- SELECT top 5 mediaName,entertainment.mediaID, count(*) as casts
-- from entertainment join castInvolved on entertainment.mediaID=castInvolved.mediaID
-- group by entertainment.mediaID,entertainment.mediaName
-- order by casts desc;

-- -- Movies with least cast involved
-- SELECT top 5 mediaName,entertainment.mediaID, count(*) as casts
-- from entertainment left join castInvolved on entertainment.mediaID=castInvolved.mediaID
-- group by entertainment.mediaID,entertainment.mediaName
-- order by casts;

-- -- Movies w no directors
-- SELECT mediaName 
-- from entertainment join directedBy on entertainment.mediaId=directedBy.mediaID
-- where directedBy.dirId=0;

-- -- Movies and tVshows where two given cast have worked together
-- SELECT mediaName
-- from cast join castInvolved on cast.castId=castInvolved.castID
-- join entertainment on castInvolved.mediaId=entertainment.mediaID
-- where cast.castId=46199
-- INTERSECT
-- SELECT mediaName
-- from cast join castInvolved on cast.castId=castInvolved.castID
-- join entertainment on castInvolved.mediaId=entertainment.mediaID
-- where cast.castId=46200

-- -- Top five countries with most number of movies made
-- SELECT top 5 countryName,COUNT(*) as made 
-- from country join madeIn on country.countryId=madeIN.countryID
-- group by country.countryId
-- order by made desc;

-- -- Child friendly tv-shows
--     Select mediaName 
--     from entertainment 
--     where rated="PG" or rated="TV-Y7";

-- Movies under an hour (short-film) 
    -- select medianame 
    -- from entertainment join isA on entertainment.mediaId = isA.mediaId
    -- where entertainment.duration < 60 and isA.mediaTypeId = 1; 


-- --Tv-shows with (no.of) seasons
-- ;
--Select mediaName,duration
-- from entertainment join isA on entertainment.mediaId=isA.mediaID
-- where isA.mediaTypeID=2 and entertainment.duration = 1;

--  WHOLE DESCRIPTION ABOUT A MOVIE/TV-SHOW
    -- select entertainment.mediaName as Title ,entertainment.IMDB as IMDB_Rating ,entertainment.duration as Movie_Duration,
    -- entertainment.mediaDescription as Description, entertainment.releaseYear as ReleaseYear ,entertainment.rated as rating,entertainment.rottenTomatoes as rottenTomatoes,
    -- genre.genreName as Genre,country.countryName as country, director.dirName as Director, Cast.castName as casts
    
    -- from entertainment join isA on entertainment.mediaId =isA.mediaId
    -- join mediaGenre on entertainment.mediaID = mediaGenre.mediaID
    -- join genre on mediaGenre.genreID = genre.genreID
    -- join directedBy on entertainment.mediaID = directedBy.mediaID
    -- join director on directedBy.dirID = director.dirID
    -- join castInvolved on entertainment.mediaID = castInvolved.mediaID
    -- join cast on castInvolved.castID = cast.castID
    -- join madeIn on entertainment.mediaID = madeIn.mediaID
    -- join country on madeIn.countryID = country.countryID 
    -- WHERE entertainment.mediaName like '%naruto%';
    

        -- SELECT top 5 mediaName 
        -- from streamedOn join entertainment on streamedOn.mediaID=entertainment.mediaID
        -- where releaseYear=2012 and platformID= 1
        -- order by IMDB desc

        -- SELECT top 5 MediaName 
        -- from entertainment
        -- join mediaGenre ON mediaGenre.mediaID = entertainment.mediaID
        -- join genre on genre.genreID = mediaGenre.genreID
        -- where genreName like '%TV dramas%'
        -- order by IMDB desc;

--  Movies that were made in more than two countries
-- SELECT entertainment.mediaID, entertainment.mediaName, COUNT(countryID) as Total Countries FROM 
-- entertainment join madeIn on entertainment.mediaID = madeIn.mediaID
-- join isA on entertainment.mediaID = isA.mediaID
-- where mediaTypeID = 1 
-- GROUP BY entertainment.mediaName, entertainment.mediaID
-- having COUNT(countryID)> 2;


-- Top 5 movies of a particular country
   --  with moviesInCountry (mediaName, IMDB) as
   --  (select mediaName, IMDB
   --  from entertainment
   --  join isA on entertainment.mediaId = isA.mediaId
   --  join madeIn on isA.mediaID = madeIn.mediaId
   --  join country on madeIn.countryID = country.countryID
   --  where country.countryName = 'Pakistan' and isA.mediaTypeID = 1 )

   --  Select top 5mediaName, IMDB
   --  from moviesInCountry 
   --  order by IMDB desc;
    
-- -- List all Entertainment with  more than two genres and also list all the genreName
--    with moreThanTwoGenre ( mediaId, mediaName, genreID) as 
--     (
--         SELECT entertainment.mediaID, entertainment.mediaName, count(mediaGenre.genreID) FROM entertainment 
--         join mediaGenre on entertainment.mediaID = mediaGenre.mediaID
--         group by mediaGenre.mediaID, entertainment.mediaName, entertainment.mediaID
--         having COUNT(*) > 2
--     )

--     SELECT  moreThanTwoGenre.mediaName, genre.genreName from moreThanTwoGenre 
--     join mediaGenre on moreThanTwoGenre.mediaId = mediaGenre.mediaID
--     join genre on mediaGenre.genreID = genre.genreID
--     order by moreThanTwoGenre.mediaID;

-- most popular genre of a particular director
select top 1 director.dirID,genre.genreName,count(*) as genres from 
director join directedBy on director.dirID = directedBy.dirID
join mediaGenre on directedBy.mediaID = mediaGenre.mediaID
join genre on mediaGenre.genreId=genre.genreID
where director.dirID = 692
group by director.dirID,genre.genreName
order by genres desc; 

-- Select movies/tv shows from a particular platform with rating above average (average rating of that platform)
-- SELECT mediaName from entertainment
-- where IMDB > (
--     SELECT  AVG(IMDB)
--     from 
--     entertainment join streamedOn on entertainment.mediaID = streamedOn.mediaID
--     where platformID = 1
--     GROUP BY platformID
--     having AVG(IMDB) > 0
-- );







  




