package ru.yandex.practicum.filmorate.service.film;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.constants.ValidationMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

@Service
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       MpaStorage mpaStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Фильм с ID " + id + " не найден"));
    }

    public Film createFilm(Film newFilm) {
        validateFilm(newFilm);
        if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException(ValidationMessages.RELEASE_DATE_TOO_EARLY);
        }
        return filmStorage.save(newFilm);
    }

    public Film updateFilm(Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            throw new ValidationException(ValidationMessages.FILM_ID_REQUIRED);
        }

        if (!filmStorage.existsById(updatedFilm.getId())) {
            throw new NotFoundException(String.format(ValidationMessages.FILM_NOT_FOUND, updatedFilm.getId()));
        }

        validateFilm(updatedFilm);
        return filmStorage.save(updatedFilm);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        int limit = (count == null) ? 10 : count;
        return filmStorage.findPopularFilms(limit);
    }

    private void validateFilm(Film film) {
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new ValidationException("MPA rating is required");
        }

        if (!mpaStorage.existsById(film.getMpa().getId())) {
            throw new NotFoundException("MPA rating with ID " + film.getMpa().getId() + " does not exist");
        }

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() < 1 || genre.getId() > 6) {
                    throw new NotFoundException("Invalid genre ID: " + genre.getId());
                }

                if (!genreStorage.existsById(genre.getId())) {
                    throw new NotFoundException("Genre with ID " + genre.getId() + " does not exist");
                }
            }
        }
    }
}
