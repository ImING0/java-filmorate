package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import java.time.LocalDate;

public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
