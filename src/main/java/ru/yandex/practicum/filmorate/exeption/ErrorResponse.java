package ru.yandex.practicum.filmorate.exeption;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private int code;
    private List<String> fieldErrors;
}
