package ru.yandex.practicum.filmorate.service.film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.constants.ValidationMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;

@Service
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long nextId = 1;
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private final UserService userService;

    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с ID " + id + " не найден");
        }
        return film;
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
        likes.put(filmId, new HashSet<>());
        return newFilm;
    }

    public Film updateFilm(Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            throw new ValidationException(ValidationMessages.FILM_ID_REQUIRED);
        }

        if (!films.containsKey(updatedFilm.getId())) {
            throw new NotFoundException(String.format(ValidationMessages.FILM_NOT_FOUND, updatedFilm.getId()));
        }

        Film existingFilm = films.get(updatedFilm.getId());
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
        existingFilm.setDuration(updatedFilm.getDuration());
        return existingFilm;
    }

    public void addLike(Long filmId, Long userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        likes.get(filmId).add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        likes.get(filmId).remove(userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        int limit = (count == null) ? 10 : count;

        return films.values().stream()
            .sorted(Comparator.comparing((Film film) ->
                likes.getOrDefault(film.getId(), new HashSet<>()).size()).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
}
