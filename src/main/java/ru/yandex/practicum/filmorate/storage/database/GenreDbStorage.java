package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreName;
import ru.yandex.practicum.filmorate.storage.database.dbutils.CNGetter;
import ru.yandex.practicum.filmorate.storage.database.dbutils.GenreAsTable;
import ru.yandex.practicum.filmorate.storage.database.dbutils.SqlProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlProvider sqlProvider;
    private final CNGetter cnGetter;

    public List<Genre> findAll() {
        String sql = sqlProvider.getAllGenresSql();
        List<Integer> genreIds = jdbcTemplate.queryForList(sql, Integer.class);
        List<Genre> genres = new ArrayList<>();
        if (genreIds.isEmpty()) {
            return genres;
        } else {
            genreIds.forEach(id -> {
                genres.add(getById(id));
            });
        }
        return genres;
    }


    public Genre getById(Integer id) {
        throwIfGenreNotExistInDb(id);
        GenreAsTable genreAsTable = cnGetter.getGenreColumn();
        String sql = sqlProvider.getGenreByIdSql();
        Genre genre = jdbcTemplate.query(sql, rs -> {
            Genre genreResult = null;
            while (rs.next()) {
                Integer genreIdRs = rs.getInt(genreAsTable.getId());
                String genreNameRs = rs.getString(genreAsTable.getName());
                GenreName genreName = GenreName.fromString(genreNameRs);
                genreResult = Genre.builder().id(genreIdRs).name(genreName)
                        .build();
            }
            return genreResult;
        }, id);
        return genre;
    }

    private void throwIfGenreNotExistInDb(Integer genreId) {
        Integer answer = jdbcTemplate.queryForObject(sqlProvider.isGenreExist(), Integer.class
                , genreId);
        if (answer == 0) {
            throw new ResourceNotFoundException(
                    String.format("Жанр с таким [ID] - %d не найден.", genreId));
        }
    }
}
