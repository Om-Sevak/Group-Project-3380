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