package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum GenreName {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    ANIMATION("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private final String description;

    GenreName(String description) {
        this.description = description;
    }

    public static GenreName fromString(String name) {
        for (GenreName genre : GenreName.values()) {
            if (genre.getDescription().equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Некорректное название жанра: " + name);
    }

    @Override
    public String toString() {
        return description;
    }
}
