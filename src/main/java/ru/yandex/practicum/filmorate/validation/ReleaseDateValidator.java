package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateCorrect, LocalDate> {

    private static final LocalDate DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate localDate,
            ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(DATE);
    }
}
