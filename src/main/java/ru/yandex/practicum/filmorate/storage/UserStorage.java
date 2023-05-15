package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User save(User user);

    User update(User user);

    List<User> findAll();

    void addFriendForUser(Long userId, Long newFriendId);

    void removeFriendForUser(Long userId, Long friendId);

    List<User> getCommonFriendForUser(Long userId, Long friendId);

    List<User> getUserFriends(Long userId);

    User findUserById(Long userId);
}
