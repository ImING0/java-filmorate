package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmServiceInterface filmService;
    private final UserServiceInterface userService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Фильм создан {}", film);
        Film created = filmService.createFilm(film);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Фильм обновлен {}", film);
        Film updated = filmService.updateFilm(film);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        /*Сначала проверяем есть ли такой юзер*/
        userService.findUserById(userId);
        filmService.addLikeToFilm(filmId, userId);
        log.info("Пользователем {} был поставлен лайк фильму {}", userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        userService.findUserById(userId);
        filmService.removeLikeFromFilm(filmId, userId);
        log.info("Пользователем {} был удалён лайк фильму {}", userId, filmId);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getMostPopularFilms(
            @RequestParam(name = "count", defaultValue = "10") Long count) {
        List<Film> films = filmService.getMostPopularFilms(count);
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Получен запрос на список всех фильмов");
        List<Film> films = filmService.findAllFilms();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") Long id) {
        Film film = filmService.findFilmById(id);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}
