
use cs3380;


drop table if exists madeIn;
drop table if exists mediaGenre;
drop table if exists castInvolved;
drop table if exists directedBy;
drop table if exists streamedOn;
drop table if exists isA;
drop table if exists mediaType;
drop table if exists platform;
drop table if exists cast;
drop table if exists genre;
drop table if exists country;
drop table if exists director;
drop table if exists entertainment;

CREATE TABLE entertainment(
    mediaID integer primary key IDENTITY(1,1),
    mediaName VARCHAR(500) UNIQUE not NULL,
    releaseYear integer,
    rated VARCHAR(50),
    rottenTomatoes VARCHAR(10),
    IMDB VARCHAR(10),
    duration VARCHAR(50),
    mediaDescription VARCHAR (500)
);

CREATE TABLE director(
    dirID integer primary key IDENTITY(1,1),
    dirName VARCHAR(100) UNIQUE not NULL
);

CREATE TABLE country(
    countryID integer primary key IDENTITY(1,1),
    countryName VARCHAR(50) UNIQUE not NULL
);

CREATE TABLE genre(
    genreID integer primary key IDENTITY(1,1),
    genreName VARCHAR(100) UNIQUE not NULL
);

CREATE TABLE cast(
    castID integer primary key IDENTITY(1,1),
    castName VARCHAR(100) UNIQUE not NULL
);

CREATE TABLE platform(
    platformID integer primary key IDENTITY(1,1),
    platformName VARCHAR(100) UNIQUE 
);

CREATE TABLE mediaType(
    mediaTypeID integer primary key IDENTITY(1,1),
    mediaTypeName VARCHAR(100) UNIQUE not NULL
);

CREATE TABLE isA(
    mediaID integer PRIMARY KEY --REFERENCES entertainment(mediaID),
   , mediaTypeID INTEGER --REFERENCES mediaType(mediaTypeID)  
);

CREATE TABLE streamedOn(
    mediaID integer -- REFERENCES entertainment(mediaID),
    ,platformID integer --REFERENCES platform(platformID),
    ,dateAdded VARCHAR(50),
    PRIMARY KEY(mediaID, platformID)
);

CREATE TABLE directedBy(
    mediaID integer --REFERENCES entertainment(mediaID),
    ,dirID INTEGER --REFERENCES director(dirID),
    primary KEY (mediaID, dirID)
);

CREATE TABLE madeIn(
    mediaID integer  --REFERENCES entertainment(mediaID),
    ,countryID INTEGER --REFERENCES country(countryID),
    PRIMARY KEY(mediaID, countryID)
);

CREATE TABLE castInvolved(
    mediaID integer --REFERENCES entertainment(mediaID),
    ,castID INTEGER --REFERENCES cast(castID),
    PRIMARY KEY(mediaID, castID)
);

CREATE TABLE mediaGenre(
    mediaID  integer --REFERENCES entertainment(mediaID),
    ,genreID INTEGER --REFERENCES genre(genreID),
    PRIMARY KEY(mediaID, genreID)
);

--These are fixed media type
INSERT into mediaType (mediaTypeName) VALUES ('Movie');
INSERT into mediaType (mediaTypeName) VALUES ('Tv-Show');
--These are fixed platforms
INSERT INTO platform (platformName) VALUES ('Netflix');
INSERT INTO platform (platformName) VALUES ('Amazon Prime');
INSERT INTO platform (platformName) VALUES ('Disney Plus');
INSERT INTO platform (platformName) VALUES ('Hulu');

SELECT * from entertainment;

