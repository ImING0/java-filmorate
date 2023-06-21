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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public User update(User user) {
        throwIfUserNotExistInDb(user.getId());
        updateUserInDbInternal(user);
        return findUserById(user.getId());
    }

    @Override
    public List<User> findAll() {
        /*Переиспользуем метод поиска по id, получим id всех юзеров и пройдемся циклом,
         * вызывая для каждого метод получения по id*/
        List<User> users = new ArrayList<>();
        String allUserIdsSql = sqlProvider.findAllUsersInDbSql();
        List<Long> userIds = jdbcTemplate.queryForList(allUserIdsSql, Long.class);
        /*Пробежимся по полученным id, если пусто, то вернём пустой список.*/
        if (userIds.isEmpty()) {
            return users;
        } else {
            userIds.forEach(id -> {
                User user = findUserById(id);
                users.add(user);
            });
        }
        return users;
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
        throwIfUserNotExistInDb(userId);
        throwIfUserNotExistInDb(friendId);
        UserAsTable userColumn = cnGetter.getUserColumns();
        FriendAsTable friendColumn = cnGetter.getFriendColumns();
        String sql = sqlProvider.getCommonFriendsForUserFromDbSql();
        List<User> usersFriends = jdbcTemplate.query(sql, rs -> {
            Map<Long, User> userMap = new HashMap<>();
            while (rs.next()) {
                /*Создаем юзера из строки*/
                Long id = rs.getLong(userColumn.getId());
                String email = rs.getString(userColumn.getEmail());
                String login = rs.getString(userColumn.getLogin());
                String name = rs.getString(userColumn.getName());
                LocalDate birthday = LocalDate.parse(rs.getString(userColumn.getBirthday()));
                User user = User.builder()
                        .email(email)
                        .login(login)
                        .name(name)
                        .birthday(birthday)
                        .id(id)
                        .build();
                /*Создаем друга*/

                Long friendIdFromResultSet = rs.getLong(friendColumn.getId());
                User friend = null;
                if (friendIdFromResultSet != 0L) {
                    String friendEmail = rs.getString(friendColumn.getEmail());
                    String friendLogin = rs.getString(friendColumn.getLogin());
                    String friendName = rs.getString(friendColumn.getName());
                    LocalDate friendBirthday = LocalDate.parse(
                            rs.getString(friendColumn.getBirthday()));
                    friend = User.builder()
                            .email(friendEmail)
                            .login(friendLogin)
                            .name(friendName)
                            .birthday(friendBirthday)
                            .id(friendIdFromResultSet)
                            .build();
                }


                /*проверяем, если в мапе нет пользователя с id, то добавляем его в неё
                 * Но сначала добавляем друга в пользователя*/
                if (!userMap.containsKey(user.getId())) {
                    if (friend != null) {
                        user.addFriend(friend);
                    }
                    userMap.put(user.getId(), user);
                    /*иначе извлекаем юзера из мапы, добавляем друга и кладем обратно*/
                } else {
                    User userFromMap = userMap.get(user.getId());
                    if (friend != null) {
                        userFromMap.addFriend(friend);
                        userMap.put(userFromMap.getId(), userFromMap);
                    }
                }
            }
            return List.copyOf(userMap.values());
        }, userId, friendId);
        return usersFriends;
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        UserAsTable userColumn = cnGetter.getUserColumns();
        FriendAsTable friendColumn = cnGetter.getFriendColumns();
        throwIfUserNotExistInDb(userId);
        String sql = sqlProvider.findUserFriendsByUserIdSql();
        List<User> usersFriends = jdbcTemplate.query(sql, rs -> {
            Map<Long, User> userMap = new HashMap<>();
            while (rs.next()) {
                /*Создаем юзера из строки*/
                Long id = rs.getLong(userColumn.getId());
                String email = rs.getString(userColumn.getEmail());
                String login = rs.getString(userColumn.getLogin());
                String name = rs.getString(userColumn.getName());
                LocalDate birthday = LocalDate.parse(rs.getString(userColumn.getBirthday()));
                User user = User.builder()
                        .email(email)
                        .login(login)
                        .name(name)
                        .birthday(birthday)
                        .id(id)
                        .build();
                /*Создаем друга*/

                Long friendId = rs.getLong(friendColumn.getId());
                User friend = null;
                if (friendId != 0L) {
                    String friendEmail = rs.getString(friendColumn.getEmail());
                    String friendLogin = rs.getString(friendColumn.getLogin());
                    String friendName = rs.getString(friendColumn.getName());
                    LocalDate friendBirthday = LocalDate.parse(
                            rs.getString(friendColumn.getBirthday()));
                    friend = User.builder()
                            .email(friendEmail)
                            .login(friendLogin)
                            .name(friendName)
                            .birthday(friendBirthday)
                            .id(friendId)
                            .build();
                }


                /*проверяем, если в мапе нет пользователя с id, то добавляем его в неё
                 * Но сначала добавляем друга в пользователя*/
                if (!userMap.containsKey(user.getId())) {
                    if (friend != null) {
                        user.addFriend(friend);
                    }
                    userMap.put(user.getId(), user);
                    /*иначе извлекаем юзера из мапы, добавляем друга и кладем обратно*/
                } else {
                    User userFromMap = userMap.get(user.getId());
                    if (friend != null) {
                        userFromMap.addFriend(friend);
                        userMap.put(userFromMap.getId(), userFromMap);
                    }
                }
            }
            return List.copyOf(userMap.values());
        }, userId);
        return usersFriends;
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

    private void updateUserInDbInternal(User user) {
        /*Метод принимает объект класса User, и обновляет все его поля в базе данных.*/
        jdbcTemplate.update(sqlProvider.updateUserInDbSql(), user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
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
                .id(friendId)
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
