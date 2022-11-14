CREATE TABLE entertainment(
    mediaID integer primary key IDENTITY(1,1),
    mediaName VARCHAR(1000) UNIQUE not NULL,
    releaseYear integer,
    ageRestriction integer check (ageRestriction >= 0),
    rottenTomatoes integer check (rottenTomatoes >= 0 AND rottenTomatoes <= 100),
    IMDB DECIMAL (2, 1) check (IMDB >= 0 AND IMDB <= 100),
    duration INTEGER check (duration > 0),
    mediaDescription VARCHAR
);

CREATE TABLE director(
    dirID integer primary key IDENTITY(1,1),
    dirName VARCHAR UNIQUE not NULL
);

CREATE TABLE country(
    countryID integer primary key IDENTITY(1,1),
    countryName VARCHAR UNIQUE not NULL
);

CREATE TABLE genre(
    genreID integer primary key IDENTITY(1,1),
    genreName VARCHAR UNIQUE not NULL
);

CREATE TABLE cast(
    castID integer primary key IDENTITY(1,1),
    castName VARCHAR UNIQUE not NULL
);

CREATE TABLE platform(
    platformID integer primary key IDENTITY(1,1),
    platformName VARCHAR UNIQUE 
);

CREATE TABLE mediaType(
    mediaTypeID integer primary key IDENTITY(1,1),
    mediaTypeName VARCHAR UNIQUE not NULL
);

CREATE TABLE isA(
    mediaID integer PRIMARY KEY REFERENCES entertainment(mediaID),
    mediaTypeID INTEGER REFERENCES mediaType(mediaTypeID)  
);

CREATE TABLE streamedOn(
    mediaID integer REFERENCES entertainment(mediaID),
    platformID integer REFERENCES platform(platformID),
    dateAdded VARCHAR
    PRIMARY KEY(mediaID, platformID)
);

CREATE TABLE directedBy(
    mediaID integer REFERENCES entertainment(mediaID),
    dirID INTEGER REFERENCES director(dirID),
    primary KEY (mediaID, dirID)
);

CREATE TABLE madeIn(
    mediaID integer REFERENCES entertainment(mediaID),
    countryID INTEGER REFERENCES country(countryID),
    PRIMARY KEY(mediaID, countryID)
);

CREATE TABLE castInvolved(
    mediaID integer REFERENCES entertainment(mediaID),
    castID INTEGER REFERENCES cast(castID),
    PRIMARY KEY(mediaID, castID)
);

CREATE TABLE mediaGenre(
    mediaID integer REFERENCES entertainment(mediaID),
    genreID INTEGER REFERENCES genre(genreID),
    PRIMARY KEY(mediaID, genreID)
);

--These are fixed media type
INSERT into mediaType VALUES (0, "Movie");
INSERT into mediaType VALUES (1, "Tv-Show");
--These are fixed platforms
INSERT INTO platform VALUES (0, "Netflix");
INSERT INTO platform VALUES (1, "Hulu");
INSERT INTO platform VALUES (2, "Disney Plus");
INSERT INTO platform VALUES (3, "Amazon Prime");