package ru.yandex.practicum.filmorate.storage.mpa;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.MpaRating;

public interface MpaStorage {

    List<MpaRating> findAll();

    Optional<MpaRating> findById(Long id);

    boolean existsById(Long id);
}