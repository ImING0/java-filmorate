package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceIsAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap;
    private final IdGenerator idGenerator;

    public Map<Long, User> getUserMap() {
        return userMap;
    }

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
            String message = String.format("Пользователь с id [%d] не найден", user.getId());
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
    public User findUserById(Long userId) {
        return findUserOrThrow(userId);
    }

    public User findUserOrThrow(Long userId) {
        if (!userMap.containsKey(userId)) {
            throw new ResourceNotFoundException(
                    String.format("Пользователь с таким ID - [%d] не найден.", userId));
        } else {
            return userMap.get(userId);
        }
    }

    public User findFriendOrThrow(Long friendId) {
        if (!userMap.containsKey(friendId)) {
            throw new ResourceNotFoundException(
                    String.format("Друг с таким [ID] - %d не найден", friendId));
        } else {
            return userMap.get(friendId);
        }
    }
}
