package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    List<Film> findAllFilms();
}