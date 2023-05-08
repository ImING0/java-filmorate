package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.validation.ReleaseDateCorrect;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class Film {


    private Long id;
    @NotEmpty(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Превышено максимальное кол-во символов - 200")
    private String description;
    @ReleaseDateCorrect
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private long duration;
}
