package ru.yandex.practicum.filmorate.exeption;

public class ResourceIsAlreadyExistException extends RuntimeException{

    public ResourceIsAlreadyExistException(String message) {
        super(message);
    }
}
