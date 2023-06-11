package ru.yandex.practicum.filmorate.storage.database;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

public class UserDbStorage implements UserStorage {

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
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
        return null;
    }
}
