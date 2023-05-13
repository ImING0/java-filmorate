package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;

    @Override
    public Film createFilm(Film film) {
        return filmStorage.save(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }
}
