package ru.yandex.practicum.filmorate.service.mpa;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;

import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@Slf4j
public class MpaRatingService {
    private final MpaStorage mpaStorage;

    public MpaRatingService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaRatingDto getRating(Integer id) {
        log.info("Getting rating by id {}", id);
        return mpaStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Рейтинг MPA с ID " + id + " не найден"));
    }

    public Collection<MpaRatingDto> getRatings() {
        log.info("Getting all ratings");
        return mpaStorage.findAll();
    }
}