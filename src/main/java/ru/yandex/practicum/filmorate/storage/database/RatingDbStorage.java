package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.RatingName;
import ru.yandex.practicum.filmorate.storage.database.dbutils.CNGetter;
import ru.yandex.practicum.filmorate.storage.database.dbutils.RatingAsTable;
import ru.yandex.practicum.filmorate.storage.database.dbutils.SqlProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class RatingDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlProvider sqlProvider;
    private final CNGetter cnGetter;

    public Rating getRatingById(Integer id) {
        throwIfRatingNotExist(id);
        RatingAsTable ratingAsTable = cnGetter.getRatingAsTable();
        String sql = sqlProvider.getRatingByIdSql();
        Rating rating = null;
        rating = jdbcTemplate.query(sql, rs -> {
            Rating r = null;
            while (rs.next()) {
                Integer ratingId = rs.getInt(ratingAsTable.getId());
                String ratingName = rs.getString(ratingAsTable.getName());

                r = Rating.builder()
                        .id(ratingId)
                        .name(RatingName.fromString(ratingName))
                        .build();
            }
            return r;
        }, id);
        return rating;
    }

    public List<Rating> getAllRatings() {
        RatingAsTable ratingAsTable = cnGetter.getRatingAsTable();
        String sql = sqlProvider.getAllRatings();
        List<Rating> ratings = jdbcTemplate.query(sql, rs -> {
            List<Rating> ratingList = new ArrayList<>();
            while (rs.next()) {
                Integer ratingId = rs.getInt(ratingAsTable.getId());
                String ratingName = rs.getString(ratingAsTable.getName());

                ratingList.add(Rating.builder()
                        .id(ratingId)
                        .name(RatingName.fromString(ratingName))
                        .build());
            }
            return ratingList;
        });
        return ratings;
    }

    /*public Rating addRating(Rating rating) {
        return null;
    }*/

    private void throwIfRatingNotExist(Integer id) {
        String sql = sqlProvider.isRatingExist();
        Integer answer = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (answer == 0) {
            throw new ResourceNotFoundException(
                    String.format("Рейтинг с таким id %d не " + "найден", id));
        }
    }
}
