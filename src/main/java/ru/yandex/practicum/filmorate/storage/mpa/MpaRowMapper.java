package ru.yandex.practicum.filmorate.storage.mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.MpaRating;

@Component
public class MpaRowMapper implements RowMapper<MpaRating> {

    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
            .id(rs.getLong("mpa_id"))
            .name(rs.getString("name"))
            .build();
    }
}