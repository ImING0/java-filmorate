package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.dbutils.CNGetter;
import ru.yandex.practicum.filmorate.storage.database.dbutils.SqlProvider;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlProvider sqlProvider;
    private final CNGetter cnGetter;

    @Override
    public User save(User user) {
        return saveUserInDbInternal(user);
    }

    private User saveUserInDbInternal (User user) {
        String sql = sqlProvider.insertUserInDbSql();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setObject(4, user.getBirthday());
            return preparedStatement;
        }, keyHolder);
        Long userId = (Long) keyHolder.getKey();
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    private void updateUserInDbInternal (User user) {
        /*Метод принимает объект класса User, и обновляет все его поля в базе данных.*/
        jdbcTemplate.update(sqlProvider.updateUserInBdSql(), user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void addFriendForUser(Long userId, Long newFriendId) {

    }

    @Override
    public void removeFriendForUser(Long userId, Long friendId) {

    }

    @Override
    public List<User> getCommonFriendForUser(Long userId, Long friendId) {
        return null;
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        return null;
    }

    @Override
    public User findUserById(Long userId) {

        String sql = sqlProvider.findUserByIdInBdSql();

    }

    private User getUSerFromSqlResultSet() {

    }
}
