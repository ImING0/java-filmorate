package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.dbutils.CNGetter;
import ru.yandex.practicum.filmorate.storage.database.dbutils.FriendAsTable;
import ru.yandex.practicum.filmorate.storage.database.dbutils.SqlProvider;
import ru.yandex.practicum.filmorate.storage.database.dbutils.UserAsTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

    private User saveUserInDbInternal(User user) {
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
        throwIfUserNotExistInDb(user.getId());
        updateUserInDbInternal(user);
        return findUserById(user.getId());
    }

    private void updateUserInDbInternal(User user) {
        /*Метод принимает объект класса User, и обновляет все его поля в базе данных.*/
        jdbcTemplate.update(sqlProvider.updateUserInDbSql(), user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public List<User> findAll() {
       /* String sql = sqlProvider.f*/
        return null;
    }

    @Override
    public void addFriendForUser(Long userId, Long newFriendId) {
        throwIfUserNotExistInDb(userId);
        throwIfUserNotExistInDb(newFriendId);
        String sql = sqlProvider.addFriendForUserInDbSql();
        jdbcTemplate.update(sql, userId, newFriendId);
    }

    @Override
    public void removeFriendForUser(Long userId, Long friendId) {
        throwIfUserNotExistInDb(userId);
        throwIfUserNotExistInDb(friendId);
        String sql = sqlProvider.deleteFriendForUserInDbSql();
        jdbcTemplate.update(sql, userId, friendId);
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
        throwIfUserNotExistInDb(userId);
        String sql = sqlProvider.findUserByIdInDbSql();
        User user = jdbcTemplate.query(sql, rs -> {
            User userResult = null;
            while (rs.next()) {
                User friend = getFriendFromSqlResultSet(rs, cnGetter.getFriendColumns());
                if (userResult == null) {
                    userResult = getUserFromSqlResultSet(rs, cnGetter.getUserColumns(), userId);
                }
                if (friend != null) {
                    userResult.addFriend(friend);
                }
            }
            return userResult;
        }, userId);
        return user;
    }

    private User getUserFromSqlResultSet(ResultSet resultSet, UserAsTable userColumn,
            Long userId) throws SQLException {
        String email = resultSet.getString(userColumn.getEmail());
        String login = resultSet.getString(userColumn.getLogin());
        String name = resultSet.getString(userColumn.getName());
        LocalDate birthday = LocalDate.parse(resultSet.getString(userColumn.getBirthday()));
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .id(userId)
                .build();
    }

    private User getFriendFromSqlResultSet(ResultSet resultSet, FriendAsTable friendColumn) throws
            SQLException {
        User friend = null;
        Long friendId = resultSet.getLong(friendColumn.getId());
        if (friendId == 0L) {
            return friend;
        }
        String email = resultSet.getString(friendColumn.getEmail());
        String login = resultSet.getString(friendColumn.getLogin());
        String name = resultSet.getString(friendColumn.getName());
        LocalDate birthday = LocalDate.parse(resultSet.getString(friendColumn.getBirthday()));
        friend = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        return friend;
    }


    private void throwIfUserNotExistInDb(Long userId) {
        String sql = sqlProvider.isUserExistInDbSql();
        Integer answer = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (answer == 0) {
            throw new ResourceNotFoundException(
                    String.format("Пользователь с таким [ID] - %d не найден.", userId));
        }
    }
}
