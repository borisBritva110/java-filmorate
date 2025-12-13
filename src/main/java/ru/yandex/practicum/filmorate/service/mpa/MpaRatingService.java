package ru.yandex.practicum.filmorate.service.mpa;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@Slf4j
public class MpaRatingService {
    private final MpaStorage mpaStorage;

    public MpaRatingService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaRating getRating(Long id) {
        log.info("Getting rating by id {}", id);
        return mpaStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Рейтинг MPA с ID " + id + " не найден"));
    }

    public Collection<MpaRating> getRatings() {
        log.info("Getting all ratings");
        return mpaStorage.findAll();
    }
}