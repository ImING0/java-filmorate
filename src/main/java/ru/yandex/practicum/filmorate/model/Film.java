package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

}
