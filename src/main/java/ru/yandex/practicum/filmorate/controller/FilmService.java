package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.constants.ValidationMessages;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long nextId = 1;
    private final Map<Long, Film> films = new HashMap<>();

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public long getNextId() {
        return nextId++;
    }

    public Film createFilm(Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException(ValidationMessages.RELEASE_DATE_TOO_EARLY);
        }

        long filmId = getNextId();
        newFilm.setId(filmId);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film updateFilm(Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            throw new ValidationException(ValidationMessages.FILM_ID_REQUIRED);
        }

        if (!films.containsKey(updatedFilm.getId())) {
            throw new ValidationException(String.format(ValidationMessages.FILM_NOT_FOUND, updatedFilm.getId()));
        }

        Film existingFilm = films.get(updatedFilm.getId());
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
        existingFilm.setDuration(updatedFilm.getDuration());
        return existingFilm;
    }
}
