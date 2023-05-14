package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    User createUser(User user);
    User updateUser(User user);
    List<User> findAllUsers();
    void addFriend(Long userId, Long newFriendId);
    void removeFriend(Long userId, Long friendId);
    List<User> getCommonFriendForUser(Long userId, Long friendId);

    List<User> getUserFriends(Long userId);
    User findUserById(Long userId);

}
