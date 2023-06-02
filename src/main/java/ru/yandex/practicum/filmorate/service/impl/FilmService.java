package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {

    private final InMemoryFilmStorage filmStorage;

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

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        Map<Long, Film> filmMap = filmStorage.getFilmMap();
        if (!filmMap.containsKey(filmId))
            throw new ResourceNotFoundException("Фильм с id [%d] не найден");
        filmMap.get(filmId)
                .addLikeToFilm(userId);
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        Map<Long, Film> filmMap = filmStorage.getFilmMap();
        if (!filmMap.containsKey(filmId))
            throw new ResourceNotFoundException("Фильм с id [%d] не найден");
        filmMap.get(filmId)
                .removeLikeFromFilm(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        Map<Long, Film> filmMap = filmStorage.getFilmMap();
        return filmMap.values()
                .stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes()
                        .size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }
}
