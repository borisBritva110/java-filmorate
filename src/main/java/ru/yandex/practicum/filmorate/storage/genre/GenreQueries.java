package ru.yandex.practicum.filmorate.storage.genre;

public final class GenreQueries {

    public static final String FIND_ALL =
        "SELECT * FROM genre ORDER BY genre_id";

    public static final String FIND_BY_ID =
        "SELECT * FROM genre WHERE genre_id = ?";

    public static final String EXISTS_BY_ID =
        "SELECT COUNT(*) FROM genre WHERE genre_id = ?";

    private GenreQueries() {}
}