package ru.yandex.practicum.filmorate.constants;

public final class ValidationMessages {
    public static final String RELEASE_DATE_TOO_EARLY = "Дата выпуска фильма не может быть раньше 28 декабря 1895 года";
    public static final String FILM_ID_REQUIRED = "ID фильма должен быть указан";
    public static final String FILM_NOT_FOUND = "Фильм с ID %d не найден";
    public static final String FILM_ADDED = "Добавлен новый фильм: {}";
    public static final String FILM_UPDATED = "Обновлен фильм: {}";

    private ValidationMessages() {}
}