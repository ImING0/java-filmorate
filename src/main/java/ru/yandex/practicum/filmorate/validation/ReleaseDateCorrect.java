package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = ReleaseDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDateCorrect {
    String message() default "The date cannot be earlier than December 28, 1895";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
