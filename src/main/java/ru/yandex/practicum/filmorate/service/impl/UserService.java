package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final InMemoryUserStorage userStorage;

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
        User user = userStorage.findUserOrThrow(userId);
        User friend = userStorage.findFriendOrThrow(newFriendId);
        user.addFriend(friend);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserOrThrow(userId);
        User friend = userStorage.findFriendOrThrow(friendId);
        user.removeFriend(friend);
    }

    @Override
    public List<User> getCommonFriendForUser(Long userId, Long friendId) {
        User user = userStorage.findUserOrThrow(userId);
        User friend = userStorage.findFriendOrThrow(friendId);
        Set<Long> commonFriendsIds = user.findCommonFriendsWithUser(friend);

        /*Собираем список общих друзей*/
        List<User> commonFriends = new ArrayList<>();
        Map<Long, User> userMap = userStorage.getUserMap();
        for (Long id : commonFriendsIds) {
            User someFriend = userMap.get(id);
            if (someFriend != null) {
                commonFriends.add(someFriend);
            }
        }
        return commonFriends;
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        User user = userStorage.findUserOrThrow(userId);
        Set<Long> friendsIds = user.getFriends();
        List<User> userFriends = new ArrayList<>();
        friendsIds.forEach(id -> {
            User friend = userStorage.getUserMap().get(id);
            if (friend != null) {
                userFriends.add(friend);
            }
        });
        return userFriends;
    }

    @Override
    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }
}
