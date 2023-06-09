package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.validation.ReleaseDateCorrect;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Jacksonized
public class Film {

    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Превышено максимальное кол-во символов - 200")
    private String description;
    @ReleaseDateCorrect
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private long duration;
    @Builder.Default
    /*Поле хранит id пользователей, которым понравился фильм*/
    private Set<Long> likes = new HashSet<>();

    //TODO добавить жанр

    public void addLikeToFilm(Long userId) {
        likes.add(userId);
    }

    public void removeLikeFromFilm(Long userId) {
        likes.remove(userId);
    }
}
