package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        return userStorage.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.update(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    @Override
    public void addFriend(Long userId, Long newFriendId) {
        userStorage.addFriendForUser(userId, newFriendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriendForUser(userId, friendId);
    }

    @Override
    public List<User> getCommonFriendForUser(Long userId, Long friendId) {
        return userStorage.getCommonFriendForUser(userId, friendId);
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        return userStorage.getUserFriends(userId);
    }

    @Override
    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }
}
