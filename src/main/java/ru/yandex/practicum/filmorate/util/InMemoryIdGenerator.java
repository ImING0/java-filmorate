package ru.yandex.practicum.filmorate.util;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InMemoryIdGenerator implements IdGenerator{

    private Long id = 0L;

    @Override
    public Long generateId() {
        id++;
        return id;
    }

}
