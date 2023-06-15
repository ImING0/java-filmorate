package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ResourceIsAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceInterface userServiceInterface;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя {}", user);
        try {
            User userCreated = userServiceInterface.createUser(setNameAsLoginIfNameIsEmpty(user));
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ResourceIsAlreadyExistException e) {
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновления пользователя{}", user);
        User userUpdated = userServiceInterface.updateUser(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServiceInterface.findAllUsers(), HttpStatus.OK);
    }

    /*Добавление в друзья*/
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userServiceInterface.addFriend(userId, friendId);
    }

    /*Удаление из друзей*/
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userServiceInterface.removeFriend(userId, friendId);
    }

    /*Получить список пользователей, являющихся его друзьями.*/
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userServiceInterface.getUserFriends(userId), HttpStatus.OK);
    }

    /*Возвращает юзера по его id*/
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userServiceInterface.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /*Получить список друзей, общих с другим пользователем.*/
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriendsForUser(@PathVariable("id") Long userId,
            @PathVariable("otherId") Long friendId) {
        return userServiceInterface.getCommonFriendForUser(userId, friendId);
    }

    /*Ставим логин как имя, если было передано пустое имя*/
    private User setNameAsLoginIfNameIsEmpty(User user) {
        if (ObjectUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
