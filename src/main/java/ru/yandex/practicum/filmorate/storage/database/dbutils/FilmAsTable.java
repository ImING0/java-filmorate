package ru.yandex.practicum.filmorate.storage.database.dbutils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmAsTable {
    private String id;
    private String name;
    private String description;
    private String releaseDate;
    private String duration;
}
