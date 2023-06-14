package ru.yandex.practicum.filmorate.storage.database.dbutils;

import org.springframework.stereotype.Component;

@Component
public class CNGetter {

    public UserAsTable getUserColumns() {
        return UserAsTable.builder()
                .id("u_id")
                .email("u_email")
                .login("u_login")
                .name("u_name")
                .birthday("u_birthday")
                .build();
    }

    public FriendAsTable getFriendColumns() {
        return FriendAsTable.builder()
                .id("f_id")
                .email("f_email")
                .login("f_login")
                .name("f_name")
                .birthday("f_birthday")
                .build();
    }
}
