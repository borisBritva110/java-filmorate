package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    List<Film> findAll();

    Optional<Film> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Film save(Film film);

    List<Film> findPopularFilms(int count);
}