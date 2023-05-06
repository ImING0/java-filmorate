package ru.yandex.practicum.filmorate.util;

import org.springframework.stereotype.Component;

@Component
public class InMemoryIdGenerator implements IdGenerator{

    private Long id = 0L;

    @Override
    public Long generateId() {
        id++;
        return id;
    }

}
