package ru.yandex.practicum.filmorate.storage.film;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public List<FilmDto> findAll() {
        List<FilmDto> films = jdbcTemplate.query(FilmQueries.FIND_ALL, filmRowMapper);
        films.forEach(this::loadGenresForFilm);
        return films;
    }

    @Override
    public Optional<FilmDto> findById(Long id) {
        List<FilmDto> films = jdbcTemplate.query(FilmQueries.FIND_BY_ID, filmRowMapper, id);
        Optional<FilmDto> film = films.stream().findFirst();
        film.ifPresent(this::loadGenresForFilm);
        return film;
    }

    private void loadGenresForFilm(FilmDto film) {
        List<GenreDto> genres = jdbcTemplate.query(FilmQueries.LOAD_GENRES, (rs, rowNum) ->
                GenreDto.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("name"))
                    .build(),
            film.getId()
        );

        film.setGenres(new HashSet<>(genres));
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(FilmQueries.EXISTS_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(FilmQueries.INSERT, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(FilmQueries.ADD_LIKE, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(FilmQueries.REMOVE_LIKE, filmId, userId);
    }

    @Override
    public FilmDto save(FilmDto film) {
        if (film.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(FilmQueries.INSERT, new String[]{"film_id"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
                ps.setLong(4, film.getDuration());
                ps.setLong(5, film.getMpa().getId());
                return ps;
            }, keyHolder);
            film.setId(keyHolder.getKey().longValue());
            saveFilmGenres(film);
        } else {
            jdbcTemplate.update(FilmQueries.UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
            saveFilmGenres(film);
        }
        return film;
    }

    @Override
    public List<FilmDto> findPopularFilms(int count) {
        List<FilmDto> films = jdbcTemplate.query(FilmQueries.FIND_POPULAR, filmRowMapper, count);
        films.forEach(this::loadGenresForFilm);
        return films;
    }

    private void saveFilmGenres(FilmDto film) {
        jdbcTemplate.update(FilmQueries.DELETE_GENRES, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Object[]> batchArgs = film.getGenres().stream()
                .map(genre -> new Object[]{film.getId(), genre.getId()})
                .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(FilmQueries.INSERT_GENRES, batchArgs);
        }
    }
}