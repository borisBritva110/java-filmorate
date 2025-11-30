package ru.yandex.practicum.filmorate.storage.genre;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.dto.GenreDto;

@Component
public class GenreRowMapper implements RowMapper<GenreDto> {

    @Override
    public GenreDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GenreDto.builder()
            .id(rs.getInt("genre_id"))
            .name(rs.getString("name"))
            .build();
    }
}