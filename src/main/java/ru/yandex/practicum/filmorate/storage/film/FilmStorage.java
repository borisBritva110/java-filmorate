package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.dto.FilmDto;

public interface FilmStorage {
    List<FilmDto> findAll();
    Optional<FilmDto> findById(Long id);
    FilmDto save(FilmDto film);
    boolean existsById(Long id);
    void deleteById(Long id);
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    List<FilmDto> findPopularFilms(int count);
}