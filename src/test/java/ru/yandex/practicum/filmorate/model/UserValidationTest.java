package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testUserValidation() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("valid_login")
                .name("John")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertTrue(validator.validate(user)
                .isEmpty(), "User should be valid");
    }

    @Test
    public void testInvalidEmail() {
        User user = User.builder()
                .id(1L)
                .email("invalid_email")
                .login("valid_login")
                .name("John")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertFalse(validator.validate(user)
                .isEmpty(), "User should have invalid email");
    }

    @Test
    public void testInvalidLogin() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("invalid login")
                .name("John")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        assertFalse(validator.validate(user)
                .isEmpty(), "User should have invalid login");
    }

    @Test
    public void testInvalidBirthday() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("valid_login")
                .name("John")
                .birthday(LocalDate.now()
                        .plusDays(1))
                .build();

        assertFalse(validator.validate(user)
                .isEmpty(), "User should have invalid birthday");
    }
}