package ru.yandex.practicum.filmorate.service.genre;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

@AllArgsConstructor
@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Integer id) {
        log.info("Отправлен жанр с id: {}", id);
        return genreStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Жанр с ID " + id + " не найден"));
    }

    public Collection<Genre> getGenres() {
        log.info("Отправлен список всех жанров");
        return genreStorage.findAll();
    }
}