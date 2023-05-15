package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceIsAlreadyExistException;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap;
    private final IdGenerator idGenerator;

    @Override
    public Film save(Film film) {
        if (filmMap.containsKey(film.getId())) {
            String massage = String.format("Фильм с id [%d] уже есть", film.getId());
            throw new ResourceIsAlreadyExistException(massage);
        }
        film.setId(idGenerator.generateId());
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            String massage = String.format("Фильм с id [%d] не найден", film.getId());
            throw new ResourceNotFoundException(massage);
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return List.copyOf(filmMap.values());
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        if (!filmMap.containsKey(filmId))
            throw new ResourceNotFoundException("Фильм с id [%d] не найден");
        filmMap.get(filmId)
                .addLikeToFilm(userId);
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        if (!filmMap.containsKey(filmId))
            throw new ResourceNotFoundException("Фильм с id [%d] не найден");
        filmMap.get(filmId)
                .removeLikeFromFilm(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        return filmMap.values().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film findFilmById(Long filmId) {
        if (!filmMap.containsKey(filmId))
            throw new ResourceNotFoundException("Фильм с id [%d] не найден");
        return filmMap.get(filmId);
    }
}
