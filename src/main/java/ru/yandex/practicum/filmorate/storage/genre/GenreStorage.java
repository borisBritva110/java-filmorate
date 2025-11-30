package ru.yandex.practicum.filmorate.storage.genre;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.dto.GenreDto;

public interface GenreStorage {
    List<GenreDto> findAll();
    Optional<GenreDto> findById(Integer id);
    boolean existsById(Integer id);
}