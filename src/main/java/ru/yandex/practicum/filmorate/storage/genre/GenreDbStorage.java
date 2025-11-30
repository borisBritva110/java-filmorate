package ru.yandex.practicum.filmorate.storage.genre;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.dto.GenreDto;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper genreRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<GenreDto> findAll() {
        return jdbcTemplate.query(GenreQueries.FIND_ALL, genreRowMapper);
    }

    @Override
    public Optional<GenreDto> findById(Integer id) {
        List<GenreDto> genres = jdbcTemplate.query(GenreQueries.FIND_BY_ID, genreRowMapper, id);
        return genres.stream().findFirst();
    }

    @Override
    public boolean existsById(Integer id) {
        Integer count = jdbcTemplate.queryForObject(GenreQueries.EXISTS_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }
}