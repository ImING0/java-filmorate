package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> filmMapMap;
    private final IdGenerator idGenerator;
    @Override
    public Film save(Film film) {
        film.setId(idGenerator.generateId());
        filmMapMap.put(film.getId(), film);
        return film;
    }
}
