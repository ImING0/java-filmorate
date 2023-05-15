package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    List<Film> findAll();

    void addLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);

    List<Film> getMostPopularFilms(Long count);

    Film findFilmById(Long filmId);
}
