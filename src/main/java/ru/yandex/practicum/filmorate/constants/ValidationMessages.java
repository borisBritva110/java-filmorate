package ru.yandex.practicum.filmorate.constants;

public final class ValidationMessages {
    public static final String RELEASE_DATE_TOO_EARLY = "Дата выпуска фильма не может быть раньше 28 декабря 1895 года";
    public static final String FILM_ID_REQUIRED = "ID фильма должен быть указан";
    public static final String FILM_NOT_FOUND = "Фильм с ID %d не найден";
    public static final String FILM_ADDED = "Добавлен новый фильм: {}";
    public static final String FILM_UPDATED = "Обновлен фильм: {}";
    public static final String FILM_LIKE_ADDED = "Пользователь {} поставил лайк фильму {}";
    public static final String FILM_LIKE_REMOVED = "Пользователь {} удалил лайк с фильма {}";
    public static final String USER_ADDED = "Добавлен новый пользователь: {}";
    public static final String USER_UPDATED = "Обновлен пользователь: {}";
    public static final String FRIEND_ADDED = "Пользователь {} добавил в друзья пользователя {}";
    public static final String FRIEND_REMOVED = "Пользователь {} удалил из друзей пользователя {}";

    private ValidationMessages() {
    }
}