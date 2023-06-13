package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.relational.core.mapping.Table;
import ru.yandex.practicum.filmorate.validation.LoginCorrect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Jacksonized
public class User {

    @Id
    private Long id;
    @Email
    private String email;
    @LoginCorrect
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public void addFriend(User user) {
        friends.add(user.getId());
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
    }

    public Set<Long> findCommonFriendsWithUser(User user) {
        Set<Long> intersection = new HashSet<>(this.friends);
        intersection.retainAll(user.getFriends());
        return intersection;
    }
}
