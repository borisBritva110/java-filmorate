package ru.yandex.practicum.filmorate.storage.mpa;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper mpaRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRowMapper = mpaRowMapper;
    }

    @Override
    public List<MpaRatingDto> findAll() {
        return jdbcTemplate.query(MpaQueries.FIND_ALL, mpaRowMapper);
    }

    @Override
    public Optional<MpaRatingDto> findById(Integer id) {
        List<MpaRatingDto> mpaRatings = jdbcTemplate.query(MpaQueries.FIND_BY_ID, mpaRowMapper, id);
        return mpaRatings.stream().findFirst();
    }

    @Override
    public boolean existsById(Integer id) {
        Integer count = jdbcTemplate.queryForObject(MpaQueries.EXISTS_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }
}