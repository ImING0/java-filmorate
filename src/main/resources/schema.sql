drop table if exists GENRES cascade;
drop table if exists RATINGS cascade;
drop table if exists FILMS cascade;
drop table if exists FILMS_GENRE cascade;
drop table if exists USERS cascade;
drop table if exists FILMS_USER_LIKE cascade;
drop table if exists FRIENDSHIP_STATUS cascade;

create table PUBLIC.GENRES
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(15) not null
);

create table PUBLIC.RATINGS
(
    ID   BIGINT auto_increment
        primary key,
    NAME CHARACTER VARYING(15) not null
);

create table PUBLIC.FILMS
(
    ID           BIGINT auto_increment
        primary key,
    NAME         CHARACTER VARYING(63)  not null,
    DESCRIPTION  CHARACTER VARYING(255) not null,
    RELEASE_DATE TIMESTAMP              not null,
    DURATION     BIGINT                 not null,
    RATING_ID    BIGINT,
    constraint "films_ratings_id_fk"
        foreign key (RATING_ID) references PUBLIC.RATINGS
);

create table PUBLIC.FILMS_GENRE
(
    FILM_ID  BIGINT  not null,
    GENRE_ID INTEGER not null,
    constraint FILMS_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILMS_GENRE_FILMS_ID_FK
        foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint FILMS_GENRE_GENRES_ID_FK
        foreign key (FILM_ID) references PUBLIC.GENRES
);

create table PUBLIC.USERS
(
    ID       BIGINT auto_increment
        primary key,
    EMAIL    CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING not null,
    NAME     CHARACTER VARYING not null,
    BIRTHDAY DATE              not null
);

create table PUBLIC.FILMS_USER_LIKE
(
    FILM_ID BIGINT not null,
    USER_ID BIGINT not null,
    constraint FILMS_USER_LIKE_FILM_ID_USER_PK
        primary key (FILM_ID, USER_ID),
    constraint FILMS_USER_LIKE_FILMS_ID_FK
        foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint FILMS_USER_LIKE_USERS_ID_FK
        foreign key (USER_ID) references PUBLIC.USERS
);

create table PUBLIC.FRIENDSHIP_STATUS
(
    USER_ID   BIGINT not null,
    FRIEND_ID BIGINT not null,
    constraint FRIENDSHIP_STATUS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint FRIENDSHIP_STATUS_USER_ID_FRIEND_FK
        foreign key (FRIEND_ID) references PUBLIC.USERS,
    constraint FRIENDSHIP_STATUS_USER_ID_USER_FK
        foreign key (USER_ID) references PUBLIC.USERS,
    constraint FRIENDS_ARE_DISTINCT_CK
        check ("USER_ID" <> "FRIEND_ID")
);

create unique index PUBLIC.USERS_EMAIL_INDEX
    on PUBLIC.USERS (EMAIL);

alter table PUBLIC.USERS
    add unique (EMAIL);

