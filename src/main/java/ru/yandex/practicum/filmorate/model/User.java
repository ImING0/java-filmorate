package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.validation.LoginCorrect;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@Jacksonized
public class User {

    private Long id;
    @Email
    private String email;
    @LoginCorrect
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
