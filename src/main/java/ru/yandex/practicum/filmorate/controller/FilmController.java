package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.constants.ValidationMessages;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.film.FilmService;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto newFilm) {
        FilmDto createdFilm = filmService.createFilm(newFilm);
        log.info(ValidationMessages.FILM_ADDED, createdFilm.getName());
        return createdFilm;
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto updatedFilm) {
        FilmDto updated = filmService.updateFilm(updatedFilm);
        log.info(ValidationMessages.FILM_UPDATED, updated.getName());
        return updated;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        log.info(ValidationMessages.FILM_LIKE_ADDED, userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.info(ValidationMessages.FILM_LIKE_REMOVED, userId, id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}