package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.stereotype.Component;

@Component
public class SqlProvider {

    public String insertUserInDbSql() {
        return "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)"
                + "VALUES (?, ?, ?, ?)";
    }

    public String updateUserInBdSql() {
        return "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?."
                + "BIRTHDAY = ?"
                + "WHERE ID = ?";
    }

    public String findUserByIdInBdSql () {
        /*SQL запрос должен вернуть самого юзера, а также список его друзей для последующей
        * обработки и воссоздания юзера в коде.*/
        return "SELECT user.email AS user_email, " +
                "user.login AS user_login, " +
                "user.name AS user_name, " +
                "user.birthday AS user_birthday, " +
                "uf.id AS friend_id, " +
                "uf.email AS friend_email, " +
                "uf.login AS friend_login, " +
                "uf.name AS friend_name, " +
                "uf.birthday AS friend_birthday " +
                "FROM USERS AS user" +
                "LEFT JOIN FRIENDSHIP_STATUS fs ON user.id = fs.user_id " +
                "LEFT JOIN Users uf ON uf.id = fs.friend_id " +
                "WHERE user.id = ?";
    }
}
