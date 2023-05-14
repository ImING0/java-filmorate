package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceIsAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap;
    private final IdGenerator idGenerator;

    @Override
    public User save(User user) {
        if (userMap.containsKey(user.getId())) {
            throw new ResourceIsAlreadyExistException("Данный пользователь уже есть в системе");
        }
        user.setId(idGenerator.generateId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!userMap.containsKey(user.getId())) {
            String message = String.format("Пользователь с id %d не найден", user.getId());
            throw new ResourceNotFoundException(message);
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(userMap.values());
    }

    @Override
    public void addFriendForUser(Long userId, Long newFriendId) {
        /*Проверяем есть ли юзер и его друг в памяти*/
        if (!userMap.containsKey(userId)) throw new ResourceNotFoundException(
                String.format("Пользователь с таким ID - %d не найден.", userId));
        if (!userMap.containsKey(newFriendId)) throw new ResourceNotFoundException(String.format(
                "Друг с таким ID - %d не найден", newFriendId));

        /*Если всё ок, то добавляем юзера в друзья*/
        User user = userMap.get(userId);
        User friend = userMap.get(newFriendId);
    }

    @Override
    public void removeFriendForUser(Long userId, Long friendId) {
        /*Проверяем есть ли юзер и его друг в памяти*/
        if (!userMap.containsKey(userId)) throw new ResourceNotFoundException(
                String.format("Пользователь с таким ID - %d не найден.", userId));
        if (!userMap.containsKey(friendId)) throw new ResourceNotFoundException(String.format(
                "Друг с таким ID - %d не найден", friendId));
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        user.removeFriend(friend);
    }

    @Override
    public List<User> getCommonFriendForUser(Long userId, Long friendId) {
        /*Проверяем есть ли юзер и его друг в памяти*/
        if (!userMap.containsKey(userId)) throw new ResourceNotFoundException(
                String.format("Пользователь с таким ID - %d не найден.", userId));
        if (!userMap.containsKey(friendId)) throw new ResourceNotFoundException(String.format(
                "Друг с таким ID - %d не найден", friendId));
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        Set<Long> commonFriendsIds = user.findCommonFriendsWithUser(friend);

        /*Собираем список общих друзей*/
        List<User> commonFriends = new ArrayList<>();
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
        /*Проверяем есть ли такой юзер*/
        if (!userMap.containsKey(userId)) throw new ResourceNotFoundException(
                String.format("Пользователь с таким ID - %d не найден.", userId));
        User user = userMap.get(userId);
        Set<Long> friendsIds = user.getFriends();
        List<User> userFriends = new ArrayList<>();
        friendsIds.forEach(id -> {
            User friend = userMap.get(id);
            if (friend != null) {
                userFriends.add(friend);
            }
        });
        return userFriends;
    }
}
