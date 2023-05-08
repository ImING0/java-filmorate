package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Film created {}", film);
        Film created = inMemoryFilmStorage.save(film);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Film updated {}", film);
        try {
            Film updated = inMemoryFilmStorage.update(film);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (FilmNotFoundException e) {
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Получен запрос на список всех фильмов");
        List<Film> films = inMemoryFilmStorage.findAll();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }
}
