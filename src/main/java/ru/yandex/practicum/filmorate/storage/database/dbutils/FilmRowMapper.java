package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film.FilmBuilder builder = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("film_release_date")
                        .toLocalDate())
                .duration(rs.getLong("film_duration"));

        // Mapping rating
        if (rs.getLong("rating_id") != 0) {
            Rating rating = Rating.builder()
                    .id(rs.getInt("rating_id"))
                    .name(RatingName.valueOf(rs.getString("rating_name")))
                    .build();
            builder.mpa(rating);
        }

        // Mapping genres
        Set<Genre> genres = new HashSet<>();
        do {
            int genreId = rs.getInt("genre_id");
            if (genreId == 0) {
                break; // No more genres for this film
            }
            Genre genre = Genre.builder()
                    .id(genreId)
                    .name(GenreName.valueOf(rs.getString("genre_name")))
                    .build();
            genres.add(genre);
        } while (rs.next());
        builder.genres(new ArrayList<>(genres));

        // Mapping likes
        Set<Long> likes = new HashSet<>();
        do {
            long userId = rs.getLong("u_id");
            if (userId == 0) {
                break; // No more likes for this film
            }
            likes.add(userId);
        } while (rs.next());
        builder.likes(likes);

        return builder.build();
    }
}
