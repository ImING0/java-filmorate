/*
SELECT f.ID AS film_id,
       f.NAME AS film_name,
       f.DESCRIPTION AS film_description,
       f.RELEASE_DATE AS film_release_date,
       f.DURATION AS film_duration,
       r.ID AS rating_id,
       r.NAME AS rating_name,
       g.ID AS genre_id,
       g.NAME AS genre_name
FROM FILMS as f
--Добавляем рейтинг
         LEFT JOIN RATINGS r ON f.RATING_ID = r.ID
-- Добавляем таблицу фильмы-жанры для получения информации о жанре
         LEFT JOIN FILMS_GENRE fg on f.ID = fg.FILM_ID
         LEFT JOIN GENRES g on g.ID = fg.FILM_ID
WHERE f.ID = ?;*/

SELECT f.ID AS film_id,
       f.NAME AS film_name,
       f.DESCRIPTION AS film_description,
       f.RELEASE_DATE AS film_release_date,
       f.DURATION AS film_duration,
       r.ID AS rating_id,
       r.NAME AS rating_name,
       g.ID AS genre_id,
       g.NAME AS genre_name,
       u.ID AS u_id
FROM FILMS as f
--Добавляем рейтинг
         LEFT JOIN RATINGS r ON f.RATING_ID = r.ID
-- Добавляем таблицу фильмы-жанры для получения информации о жанре
         LEFT JOIN FILMS_GENRE fg on f.ID = fg.FILM_ID
         LEFT JOIN GENRES g on g.ID = fg.FILM_ID
         LEFT JOIN FILMS_USER_LIKE ful on f.ID = ful.FILM_ID
         LEFT JOIN USERS u on ful.USER_ID = u.ID
WHERE f.ID = ?;