package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ResourceIsAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя {}", user);
        try {
            User userCreated = inMemoryUserStorage.save(setNameAsLoginIfNameIsEmpty(user));
            return new  ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ResourceIsAlreadyExistException e) {
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновления пользователя{}", user);
        try {
            User userUpdated = inMemoryUserStorage.update(user);
            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage());
        }
        return new  ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(inMemoryUserStorage.findAll(), HttpStatus.OK);
    }

    private User setNameAsLoginIfNameIsEmpty(User user) {
        if (ObjectUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
