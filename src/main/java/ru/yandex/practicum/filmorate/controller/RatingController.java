package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final RatingDbStorage ratingDbStorage;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        log.info("Received request to getAllRatings.");
        List<Rating> ratings = ratingDbStorage.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable("id") Integer id) {
        log.info("Received request to getRatingById: {}", id);
        Rating rating = ratingDbStorage.getRatingById(id);
        return ResponseEntity.ok(rating);
    }

}