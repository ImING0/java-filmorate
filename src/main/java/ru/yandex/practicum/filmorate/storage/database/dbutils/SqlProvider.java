package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.stereotype.Component;

@Component
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

    /*Проверяет, есть ли пользователь в БД с указанным id. Если пользователь не найден, то вернет
     0*/
    public String isUserExistInDb() {
        return "SELECT COUNT(*) "
                + "FROM USERS "
                + "WHERE USERS.ID = ?";
    }
}
