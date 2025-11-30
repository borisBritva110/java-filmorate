package ru.yandex.practicum.filmorate.storage.mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

@Component
public class MpaRowMapper implements RowMapper<MpaRatingDto> {

    @Override
    public MpaRatingDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MpaRatingDto.builder()
            .id(rs.getInt("mpa_id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .build();
    }
}