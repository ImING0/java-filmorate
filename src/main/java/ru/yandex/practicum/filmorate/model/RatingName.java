package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum RatingName {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private final String name;

    RatingName(String name) {
        this.name = name;
    }

    public static RatingName fromString(String name) {
        for (RatingName rating : RatingName.values()) {
            if (rating.name.equalsIgnoreCase(name)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Некорректный рейтинг: " + name);
    }

    @Override
    public String toString() {
        return name;
    }
}
