package ru.yandex.practicum.filmorate.exeption;

public class FilmIsAlreadyExistException extends RuntimeException{

    public FilmIsAlreadyExistException(String message) {
        super(message);
    }
}
