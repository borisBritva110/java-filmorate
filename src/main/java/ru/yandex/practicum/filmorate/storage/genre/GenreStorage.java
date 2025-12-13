package ru.yandex.practicum.filmorate.storage.genre;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {

    List<Genre> findAll();

    Optional<Genre> findById(Integer id);

    boolean existsById(Long id);
}