package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.constants.ValidationMessages;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        Film createdFilm = filmService.createFilm(newFilm);
        log.info(ValidationMessages.FILM_ADDED, createdFilm.getName());
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {
        Film updated = filmService.updateFilm(updatedFilm);
        log.info(ValidationMessages.FILM_UPDATED, updated.getName());
        return updated;
    }
}