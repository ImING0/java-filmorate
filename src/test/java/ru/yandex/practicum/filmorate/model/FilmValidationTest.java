package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
@ExtendWith(MockitoExtension.class)
*/
public class FilmValidationTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    public void testFilmValidation() {
        Film film = Film.builder()
                .name("")
                .description("A".repeat(201))
                .releaseDate(LocalDate.of(1800, Month.JANUARY, 1))
                .duration(-10L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(4, violations.size(), "Некорректное количество ошибок валидации");

        violations.forEach(violation -> {
            String message = violation.getMessage();
            switch (violation.getPropertyPath()
                    .toString()) {
                case "name":
                    assertEquals("Название не может быть пустым", message);
                    break;
                case "description":
                    assertEquals("Превышено максимальное кол-во символов - 200", message);
                    break;
                case "releaseDate":
                    assertEquals("Дата не может быть раньше чем 28 декабря 1895", message);
                    break;
                case "duration":
                    assertEquals("Продолжительность не может быть отрицательной", message);
                    break;
            }
        });

        film = Film.builder()
                .name("The Godfather")
                .description(
                        "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.")
                .releaseDate(LocalDate.of(1972, Month.MARCH, 24))
                .duration(175L)
                .build();

        violations = validator.validate(film);
        assertEquals(0, violations.size(),
                "Ошибки валидации не должны возникать для корректного объекта");
    }
}