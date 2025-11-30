package ru.yandex.practicum.filmorate.storage.mpa;

public final class MpaQueries {

    public static final String FIND_ALL =
        "SELECT * FROM mpa_rating ORDER BY mpa_id";

    public static final String FIND_BY_ID =
        "SELECT * FROM mpa_rating WHERE mpa_id = ?";

    public static final String EXISTS_BY_ID =
        "SELECT COUNT(*) FROM mpa_rating WHERE mpa_id = ?";

    private MpaQueries() {}
}