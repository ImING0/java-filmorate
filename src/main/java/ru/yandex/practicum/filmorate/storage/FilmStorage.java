package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long filmId);
}
