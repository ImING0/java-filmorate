package ru.yandex.practicum.filmorate.storage.database.dbutils;

public class CNGetter {

    public UserAsTable getUserColumns() {
        return UserAsTable.builder()
                .email("user_email")
                .login("user_login")
                .name("user_name")
                .birthday("user_birthday")
                .build();
    }
}
