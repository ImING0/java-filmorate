package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCorrect {
    String message() default "Логин не должен быть пустым или содержать пробелы!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
