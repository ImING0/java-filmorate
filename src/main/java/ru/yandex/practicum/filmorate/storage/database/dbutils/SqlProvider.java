package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.stereotype.Component;

@Component
@SuppressWarnings(value = "style")
public class SqlProvider {

    public String insertUserInDbSql() {
        return "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)"
                + "VALUES (?, ?, ?, ?)";
    }

    public String updateUserInDbSql() {
        return "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?,"
                + "BIRTHDAY = ?"
                + "WHERE ID = ?";
    }

    public String findUserByIdInDbSql() {
        /*SQL запрос должен вернуть самого юзера, а также список его друзей для последующей
        * обработки и воссоздания юзера в коде.*/
        return "SELECT u.EMAIL u_email,"
                + "u.LOGIN u_login,"
                + "u.NAME u_name,"
                + "u.BIRTHDAY u_birthday, "
                + "uf.ID f_id,"
                + "uf.EMAIL f_email,"
                + "uf.LOGIN f_login,"
                + "uf.NAME f_name,"
                + "uf.BIRTHDAY f_birthday "
                + "FROM USERS AS u "
                + "LEFT JOIN FRIENDSHIP_STATUS fs ON u.ID = FS.USER_ID "
                + "LEFT JOIN USERS uf ON uf.ID = fs.FRIEND_ID "
                + "WHERE u.ID = ?;";
    }

    public String findAllUsersInDbSql() {
        return "SELECT u.ID "
                + "FROM USERS as u "
                + "ORDER BY u.ID ASC ;";
    }

    /*Проверяет, есть ли пользователь в БД с указанным id. Если пользователь не найден, то вернет
     0*/
    public String isUserExistInDbSql() {
        return "SELECT COUNT(*) "
                + "FROM USERS "
                + "WHERE USERS.ID = ?";
    }

    /*Добавляет друга в БД*/
    public String addFriendForUserInDbSql() {
        return "INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID) " +
                "VALUES (?,?);";
    }

    /*Удаляет пользователя из друзей*/

    public String deleteFriendForUserInDbSql() {
        return "DELETE FROM FRIENDSHIP_STATUS "
                + "WHERE USER_ID = ? AND FRIEND_ID = ?;";
    }

    /*Получить всех друзей пользователя
    * Сначала выбирает всех друзей пользователя из БД, потом выгружет их друзей.
    * На основе данных будет воссоздан друг юзера и друзья друга*/

    public String findUserFriendsByUserIdSql() {
        return "SELECT u.id        AS u_id, "
                + "       u.email     AS u_email, "
                + "       u.login     AS u_login, "
                + "       u.name      AS u_name, "
                + "       u.birthday  AS u_birthday, "
                + "       uf.id       AS f_id, "
                + "       uf.email    AS f_email, "
                + "       uf.login    AS f_login, "
                + "       uf.name     AS f_name, "
                + "       uf.birthday AS f_birthday "
                + "FROM USERS u "
                + "         LEFT JOIN FRIENDSHIP_STATUS fs ON u.id = fs.user_id "
                + "         LEFT JOIN USERS uf ON uf.id = fs.friend_id "
                + "WHERE u.id IN (SELECT friends_of_user.friend_id "
                + "               FROM USERS main_user "
                + "                        INNER JOIN FRIENDSHIP_STATUS friends_of_user ON "
                + "main_user.id = friends_of_user.user_id "
                + "               WHERE main_user.id = ?);";
    }

    public String getCommonFriendsForUserFromDbSql () {
        return "WITH common_friend_ids as (SELECT fs.friend_id f_id "
                + "                           FROM USERS u "
                + "                                    INNER JOIN FRIENDSHIP_STATUS fs ON u.id = "
                + "fs.user_id "
                + "                           WHERE u.id = ? "
                + "                           INTERSECT "
                + "                           SELECT ff.friend_id f_id "
                + "                           FROM USERS uf "
                + "                                    INNER JOIN FRIENDSHIP_STATUS ff ON uf.id ="
                + " ff.user_id "
                + "                           WHERE uf.id = ?) "
                + "SELECT u.id        u_id, "
                + "       u.email     u_email, "
                + "       u.login     u_login, "
                + "       u.name      u_name, "
                + "       u.birthday  u_birthday, "
                + "       uf.id       f_id, "
                + "       uf.email    f_email, "
                + "       uf.login    f_login, "
                + "       uf.name     f_name, "
                + "       uf.birthday f_birthday "
                + "FROM USERS u "
                + "         LEFT JOIN FRIENDSHIP_STATUS fs ON u.id = fs.user_id "
                + "         LEFT JOIN USERS uf ON uf.id = fs.friend_id "
                + "WHERE u.id IN (SELECT f_id FROM common_friend_ids)";
    }


    public String insertFilmInDbSql() {
        return "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)\n"
                + "VALUES (?, ?, ?, ?, ?);";
    }

    public String updateFilmInDb() {
        return "UPDATE FILMS "
                + " SET NAME         = ?, "
                + "    DESCRIPTION  = ?, "
                + "    RELEASE_DATE = ?, "
                + "    DURATION     = ?, "
                + "    RATING_ID    = ? "
                + "WHERE ID = ?;";
    }

    public String addLikeToFilmSql() {
        return "INSERT INTO FILMS_USER_LIKE (FILM_ID, USER_ID) "
                + "VALUES (?, ?);";
    }

    public String deleteSingleLikeFromFilm () {
        return "DELETE FROM FILMS_USER_LIKE "
                + "WHERE FILM_ID = ? AND USER_ID = ?";
    }


    public String findFilmInDbByIdSql () {
        return " SELECT f.ID AS film_id, "
                + "       f.NAME AS film_name, "
                + "       f.DESCRIPTION AS film_description, "
                + "       f.RELEASE_DATE AS film_release_date, "
                + "       f.DURATION AS film_duration, "
                + "       r.ID AS rating_id, "
                + "       r.NAME AS rating_name, "
                + "       g.ID AS genre_id, "
                + "       g.NAME AS genre_name, "
                + "       u.ID AS u_id "
                + " FROM FILMS as f "
                + "         LEFT JOIN RATINGS r ON f.RATING_ID = r.ID "
                + "         LEFT JOIN FILMS_GENRE fg on f.ID = fg.FILM_ID "
                + "         LEFT JOIN GENRES g on g.ID = fg.GENRE_ID "
                + "         LEFT JOIN FILMS_USER_LIKE ful on f.ID = ful.FILM_ID "
                + "         LEFT JOIN USERS u on ful.USER_ID = u.ID "
                + "WHERE f.ID = ?;";
    }

    public String findAllFilmsInDbSql() {
        return "SELECT film.ID AS f_id "
                + "FROM FILMS as film "
                + "ORDER BY f_id ASC;";
    }

    /*Добавить жанр фильму*/
    public String addGenreToFilmSql() {
        return " INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) "
                + "VALUES (?, ?) ";
    }

    public String deleteAllGenresFromFilm() {
        return "DELETE FROM FILMS_GENRE "
                + "WHERE FILM_ID = ?";
    }

    /*Проверяет есть ли фильм в БД*/
    public String isFilmExistInDb() {
        return " SELECT COUNT(*) "
                + "FROM FILMS as film "
                + "WHERE film.ID = ?;";
    }

    public String getRatingByIdSql() {
        return " SELECT r.ID AS rating_id, "
                + "       r.NAME AS rating_name "
                + "FROM RATINGS AS r "
                + "WHERE r.ID = ?;";
    }

    public String getAllRatings() {
        return " SELECT r.ID   AS rating_id, "
                + "       r.NAME AS rating_name "
                + "FROM RATINGS AS r "
                + "ORDER BY ID ASC;";
    }

    public String getGenreByIdSql() {
        return "SELECT GENRES.ID AS genre_id, "
                + "       GENRES.NAME AS genre_name "
                + "FROM GENRES "
                + "WHERE GENRES.ID = ?";
    }

    public String getAllGenresSql() {
        return "SELECT GENRES.ID AS genre_id, "
                + "FROM GENRES ";
    }

    public String isGenreExist() {
        return " SELECT COUNT(*) "
                + "FROM GENRES "
                + "WHERE ID = ?;";
    }

    /*Проверяем есть ли такой рейтинг*/
    public String isRatingExist () {
        return " SELECT COUNT(*) "
                + "FROM RATINGS "
                + "WHERE ID = ?;";
    }

    /*Получить id самых популярных фильмов.*/
    public String getMostPopularFilmIds() {
        return "SELECT FILMS.ID "
                + "FROM FILMS "
                + "         LEFT JOIN FILMS_USER_LIKE AS l ON FILMS.ID = l.FILM_ID "
                + "GROUP BY FILMS.ID "
                + "ORDER BY l.FILM_ID DESC " + "LIMIT ?";
    }
}
