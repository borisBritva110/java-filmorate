package ru.yandex.practicum.filmorate.storage.film;

public final class FilmQueries {

    public static final String FIND_ALL =
        "SELECT f.*, m.name as mpa_name FROM film f LEFT JOIN mpa_rating m ON f.mpa_id = m.mpa_id";

    public static final String FIND_BY_ID =
        "SELECT f.*, m.name as mpa_name FROM film f LEFT JOIN mpa_rating m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";

    public static final String INSERT =
        "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE =
        "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";

    public static final String DELETE =
        "DELETE FROM film WHERE film_id = ?";

    public static final String EXISTS_BY_ID =
        "SELECT COUNT(*) FROM film WHERE film_id = ?";

    public static final String FIND_POPULAR =
        "SELECT f.*, m.name as mpa_name, COUNT(l.user_id) as likes_count " +
            "FROM film f " +
            "LEFT JOIN mpa_rating m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, m.name " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";

    public static final String LOAD_GENRES =
        "SELECT DISTINCT g.genre_id, g.name FROM film_genre fg " +
            "JOIN genre g ON fg.genre_id = g.genre_id " +
            "WHERE fg.film_id = ? " +
            "ORDER BY g.genre_id";

    public static final String DELETE_GENRES =
        "DELETE FROM film_genre WHERE film_id = ?";

    public static final String INSERT_GENRES =
        "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    public static final String ADD_LIKE =
        "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

    public static final String REMOVE_LIKE =
        "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private FilmQueries() {
    }
}