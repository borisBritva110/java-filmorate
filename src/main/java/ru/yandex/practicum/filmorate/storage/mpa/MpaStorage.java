package ru.yandex.practicum.filmorate.storage.mpa;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

public interface MpaStorage {

    List<MpaRatingDto> findAll();

    Optional<MpaRatingDto> findById(Integer id);

    boolean existsById(Integer id);
}