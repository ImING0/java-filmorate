package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.database.dbutils.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlProvider sqlProvider;
    private final CNGetter cnGetter;

    @Transactional
    @Override
    public Film save(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = sqlProvider.insertFilmInDbSql();
        Long filmId;
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa()
                    .getId());
            return preparedStatement;
        }, keyHolder);
        filmId = (Long) keyHolder.getKey();

        /*Если есть жанры, то добавляем жанры фильму*/
        if (!film.getGenres()
                .isEmpty()) {
            film.getGenres()
                    .forEach(genre -> {
                        addGenreToFilm(filmId, genre.getId());
                    });
        }
        return findFilmById(filmId);
    }

    private void addGenreToFilm(Long filmId, Integer genreId) {
        String sql = sqlProvider.addGenreToFilmSql();
        jdbcTemplate.update(sql, filmId, genreId);
    }

    private void deleteAllGenresFromFilm(Long filmId) {
        String sql = sqlProvider.deleteAllGenresFromFilm();
        jdbcTemplate.update(sql, filmId);
    }

    @Transactional
    @Override
    public Film update(Film film) {
        throwIfFilmNotExistInDb(film.getId());
        String sql = sqlProvider.updateFilmInDb();
        Integer mpaId = film.getMpa()
                .getId();
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), mpaId, film.getId());

        /*Если есть жанры, то добавляем жанры фильму*/
        if (!film.getGenres()
                .isEmpty()) {
            /*теперь нужно удалить все жанры и снова их вставить*/
            deleteAllGenresFromFilm(film.getId());

            film.getGenres()
                    .stream()
                    .map(Genre::getId) // Оставляем только id жанров
                    .distinct() // Оставляем только уникальные id жанров
                    .forEach(genreId -> addGenreToFilm(film.getId(), genreId));
        } else {
            /*Если пусто, значиит надо усе удалить*/
            deleteAllGenresFromFilm(film.getId());
        }


        return findFilmById(film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sql = sqlProvider.findAllFilmsInDbSql();
        List<Long> filmIds = jdbcTemplate.queryForList(sql, Long.class);
        List<Film> films = new ArrayList<>();
        if (filmIds.isEmpty()) {
            return films;
        } else {
            filmIds.forEach(id -> {
                films.add(findFilmById(id));
            });
        }
        return films;
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        throwIfFilmNotExistInDb(filmId);
        String sql = sqlProvider.addLikeToFilmSql();
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        throwIfFilmNotExistInDb(filmId);
        String sql = sqlProvider.deleteSingleLikeFromFilm();
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        String sql = sqlProvider.getMostPopularFilmIds();
        List<Film> films = new ArrayList<>();
        List<Long> filmIds = jdbcTemplate.queryForList(sql, Long.class, count);
        if (filmIds.isEmpty()) {
            return films;
        } else {
            filmIds.forEach(id -> {
                films.add(findFilmById(id));
            });
        }
        return films;
    }

    @Override
    public Film findFilmById(Long filmId) {
        throwIfFilmNotExistInDb(filmId);
        GenreAsTable genreColumns = cnGetter.getGenreColumn();
        RatingAsTable ratingAsTable = cnGetter.getRatingAsTable();
        FilmAsTable filmAsTable = cnGetter.getFilmColumns();
        String sql = sqlProvider.findFilmInDbByIdSql();
        Film film = jdbcTemplate.query(sql, rs -> {
            /*Сюда будем сохранять все жанры фильма*/
            List<Genre> genres = new ArrayList<>();
            Rating rating = null;
            Set<Long> userlikes = new HashSet<>();
            Film f = null;
            while (rs.next()) {
                Long userLikeId = rs.getLong("u_id");
                userlikes.add(userLikeId);
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
                    if (rating_id != 0) {
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
                            .duration(filmDur)
                            .description(filmDescrRs)
                            .releaseDate(filmReleaseDate)
                            .build();
                }
            }
            f.setMpa(rating);
            f.setGenres(genres);
            f.setLikes(userlikes);
            return f;
        }, filmId);
        return film;
    }

    private void throwIfFilmNotExistInDb(Long filmId) {
        Integer answer = jdbcTemplate.queryForObject(sqlProvider.isFilmExistInDb(), Integer.class,
                filmId);
        if (answer == 0) {
            throw new ResourceNotFoundException(
                    String.format("Фильм с таким [ID] - %d не найден.", filmId));
        }
    }
}
