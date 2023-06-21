package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GenreName {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    ANIMATION("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    @JsonValue
    private final String genre;

    GenreName(String genre) {
        this.genre = genre;
    }

    public static GenreName fromString(String name) {
        for (GenreName genre : GenreName.values()) {
            if (genre.getGenre()
                    .equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Некорректное название жанра: " + name);
    }

    @Override
    public String toString() {
        return genre;
    }
}
