package ru.yandex.practicum.filmorate.storage.film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

@Component
public class FilmRowMapper implements RowMapper<FilmDto> {

    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRatingDto mpa = MpaRatingDto.builder()
            .id(rs.getInt("mpa_id"))
            .name(rs.getString("mpa_name"))
            .build();

        Set<GenreDto> genres = new HashSet<>();

        return FilmDto.builder()
            .id(rs.getLong("film_id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .releaseDate(rs.getDate("release_date").toLocalDate())
            .duration(rs.getLong("duration"))
            .mpa(mpa)
            .genres(genres)
            .build();
    }
}