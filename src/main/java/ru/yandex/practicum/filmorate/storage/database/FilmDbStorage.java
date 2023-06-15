package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.database.dbutils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlProvider sqlProvider;
    private final CNGetter cnGetter;

    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {

    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        return null;
    }

    @Override
    public Film findFilmById(Long filmId) {
        GenreAsTable genreColumns = cnGetter.getGenreColumn();
        RatingAsTable ratingAsTable = cnGetter.getRatingAsTable();
        FilmAsTable filmAsTable = cnGetter.getFilmColumns();
        String sql = sqlProvider.findFilmInDbByIdSql();
        User user = jdbcTemplate.query(sql, rs -> {
            /*Сюда будем сохранять все жанры фильма*/
            List<Genre> genres = new ArrayList<>();
            Rating rating = null;
            Film f = null;
            while (rs.next()) {
                int genreIdRs = rs.getInt(genreColumns.getId());
                String genreNameRs = rs.getString(genreColumns.getName());
                if (genreIdRs != 0) {
                    GenreName genreName = GenreName.fromString(genreNameRs);
                    genres.add(Genre.builder()
                            .id(genreIdRs)
                            .name(genreName)
                            .build());
                }
                if (rating == null) {
                    int rating_id = rs.getInt(ratingAsTable.getId());
                    String ratingNameRs = rs.getString(ratingAsTable.getName());
                    if (rating_id != o) {
                        RatingName ratingName = RatingName.fromString(ratingNameRs);
                        rating = Rating.builder()
                                .id(rating_id)
                                .name(ratingName)
                                .build();
                    }
                }

                if (f == null) {
                    Long id = rs.getLong(filmAsTable.getId());
                    String filmNameRs = rs.getString(filmAsTable.getName());
                    String filmDescrRs = rs.getString(filmAsTable.getDescription());
                    LocalDate filmReleaseDate = rs.getDate(filmAsTable.getReleaseDate())
                            .toLocalDate();
                    Long filmDur = rs.getLong(filmAsTable.getDuration());
                    f = Film.builder()
                            .id(id)
                            .name(filmNameRs)
                            .description(filmDescrRs)
                            .releaseDate(filmReleaseDate).
                }
            } return User;
        }, filmId); return null;
    }
}
