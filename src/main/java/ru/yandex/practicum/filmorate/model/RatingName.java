package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RatingName {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    @JsonValue
    private final String rating;

    RatingName(String rating) {
        this.rating = rating;
    }

    public static RatingName fromString(String name) {
        for (RatingName rating : RatingName.values()) {
            if (rating.rating.equalsIgnoreCase(name)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Некорректный рейтинг: " + name);
    }

    @Override
    public String toString() {
        return rating;
    }
}
