--print entertainment with directors
--  SELECT * from entertainment;
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
-- SELECT * FROM entertainment JOIN directedBy ON entertainment.mediaID = directedBy.mediaID JOIN director ON directedBy.dirID = director.dirID JOIN streamedOn ON entertainment.mediaID = streamedOn.mediaID JOIN platform ON streamedOn.platformID = platform.platformID JOIN madeIn ON entertainment.mediaID = madeIn.mediaID JOIN country ON madeIn.countryID = country.countryID JOIN castInvolved ON entertainment.mediaID = castInvolved.mediaID JOIN cast ON castInvolved.castID = cast.castID JOIN isA ON entertainment.mediaID = isA.mediaID JOIN mediaType ON isA.mediaTypeID = mediaType.mediaTypeID ;
--select mediaName, platformID from entertainment JOIN streamedOn ON entertainment.mediaID = streamedON.mediaID;
-- select mediaName, countryName from entertainment JOIN madeIn ON entertainment.mediaID = madeIN.mediaID JOIN country ON madeIn.countryID = country.countryID ;
--select mediaName, castName from entertainment JOIN castInvolved ON entertainment.mediaID = castInvolved.mediaID JOIN cast ON castInvolved.castID = cast.castID ;
--select mediaName, mediaTypeName from entertainment JOIN isA ON entertainment.mediaID = isA.mediaID JOIN mediaType ON isA.mediaTypeID = mediaType.mediaTypeID ;


-- Entertainment streamed on three platforms
-- SELECT mediaID, mediaName from entertainment
-- where mediaID in (
--     select mediaID from streamedOn
--     group by mediaID
--     having count(*) = 3 
-- )

-- Movies with more than two genres
--    with temp ( mediaId, mediaName, genreID) as 
--     (
--         SELECT entertainment.mediaID, entertainment.mediaName, count(mediaGenre.genreID) FROM entertainment 
--         join mediaGenre on entertainment.mediaID = mediaGenre.mediaID
--         group by mediaGenre.mediaID, entertainment.mediaName, entertainment.mediaID
--         having COUNT(*) > 2
--     )

--     SELECT  temp.mediaName, genre.genreName from temp 
--     join mediaGenre on temp.mediaId = mediaGenre.mediaID
--     join genre on mediaGenre.genreID = genre.genreID
--     order by temp.mediaID;

-- SELECT mediaId, mediaName from entertainment
-- where IMDB > (
--     SELECT  AVG(IMDB)
--     from 
--     entertainment join streamedOn on entertainment.mediaID = streamedOn.mediaID
--     where platformID = 1
--     GROUP BY platformID
--     having AVG(IMDB) > 0
-- );

-- SELECT mediaID, IMDB from entertainment
-- where IMDB < 5
-- order by IMDB DESC;

-- Movies made in a particular country
    -- select mediaName
    -- from entertainment
    -- join isA on entertainment.mediaId = isA.mediaId
    -- join madeIn on isA.mediaID = madeIn.mediaId
    -- join country on madeIn.countryID = country.countryID
    -- where country.countryName = 'Pakistan' and  isA.mediaTypeID = 1 ;

-- with moviesInCountry (mediaName, IMDB) as
--     (select mediaName, IMDB
--     from entertainment
--     join isA on entertainment.mediaId = isA.mediaId
--     join madeIn on isA.mediaID = madeIn.mediaId
--     join country on madeIn.countryID = country.countryID
--     where country.countryName = 'Pakistan' and isA.mediaTypeID = 1 )

--     Select top 5 mediaName, IMDB
--     from moviesInCountry 
--     order by IMDB desc;

-- SELECT entertainment.mediaID, entertainment.mediaName, COUNT(countryID) as noOfCountries FROM 
-- entertainment join madeIn on entertainment.mediaID = madeIn.mediaID
-- join isA on entertainment.mediaID = isA.mediaID
-- where mediaTypeID = 1 
-- GROUP BY entertainment.mediaName, entertainment.mediaID
-- having COUNT(countryID)> 2;