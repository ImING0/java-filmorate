package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.HashMap;

@RestController
@RequestMapping("films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        log.debug("Film created {}", film);
        Film created = inMemoryFilmStorage.save(film);
        return ResponseEntity.ok(created);
    }


}
