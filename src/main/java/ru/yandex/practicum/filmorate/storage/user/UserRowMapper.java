package ru.yandex.practicum.filmorate.storage.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.dto.UserDto;

@Component
public class UserRowMapper implements RowMapper<UserDto> {

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserDto.builder()
            .id(rs.getLong("user_id"))
            .email(rs.getString("email"))
            .login(rs.getString("login"))
            .name(rs.getString("name"))
            .birthday(rs.getDate("birthday").toLocalDate())
            .build();
    }
}