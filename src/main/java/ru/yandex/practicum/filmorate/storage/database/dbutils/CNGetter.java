package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.stereotype.Component;

@Component
public class CNGetter {

    public UserAsTable getUserColumns() {
        return UserAsTable.builder()
                .id("u_id")
                .email("u_email")
                .login("u_login")
                .name("u_name")
                .birthday("u_birthday")
                .build();
    }

    public FriendAsTable getFriendColumns() {
        return FriendAsTable.builder()
                .id("f_id")
                .email("f_email")
                .login("f_login")
                .name("f_name")
                .birthday("f_birthday")
                .build();
    }

    public FilmAsTable getFilmColumns() {
        return FilmAsTable.builder()
                .id("film_id")
                .name("film_name")
                .description("film_description")
                .releaseDate("film_release_date")
                .duration("film_duration")
                .build();
    }

    public GenreAsTable getGenreColumn() {
        return GenreAsTable.builder()
                .id("genre_id")
                .name("genre_name")
                .build();
    }

    public RatingAsTable getRatingAsTable() {
        return RatingAsTable.builder()
                .id("rating_id")
                .name("rating_name")
                .build();
    }
}
