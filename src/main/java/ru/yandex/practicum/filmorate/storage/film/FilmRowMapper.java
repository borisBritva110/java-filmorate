package ru.yandex.practicum.filmorate.storage.film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRating mpa = MpaRating.builder()
            .id(rs.getLong("mpa_id"))
            .name(rs.getString("mpa_name"))
            .build();

        return Film.builder()
            .id(rs.getLong("film_id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .releaseDate(rs.getDate("release_date").toLocalDate())
            .duration(rs.getInt("duration"))
            .mpa(mpa)
            .genres(new HashSet<>())
            .build();
    }
}