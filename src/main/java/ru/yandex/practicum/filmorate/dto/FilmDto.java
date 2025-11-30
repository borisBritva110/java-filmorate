package ru.yandex.practicum.filmorate.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {

    private Long id;

    @NotBlank(message = "Name не должен быть пустой")
    @NotNull(message = "Name не должен быть null")
    private String name;

    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Длина фильма должна быть положительным числом")
    private long duration;

    @NotNull(message = "Mpa не может быть пустой", groups = ValidationException.class)
    private MpaRatingDto mpa;

    private Set<GenreDto> genres;

    private Set<Integer> likes;
}