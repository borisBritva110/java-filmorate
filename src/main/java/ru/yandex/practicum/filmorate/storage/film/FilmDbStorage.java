package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = jdbcTemplate.query(FilmQueries.FIND_ALL, filmRowMapper);

        if (films.isEmpty()) {
            return films;
        }

        loadGenresForFilms(films);
        return films;
    }

    @Override
    public Optional<Film> findById(Long id) {
        List<Film> films = jdbcTemplate.query(FilmQueries.FIND_BY_ID, filmRowMapper, id);
        Optional<Film> film = films.stream().findFirst();
        film.ifPresent(this::loadGenresForFilm);
        return film;
    }

    private void loadGenresForFilm(Film film) {
        List<Genre> genres = jdbcTemplate.query(FilmQueries.LOAD_GENRES, (rs, rowNum) ->
                Genre.builder()
                    .id(rs.getLong("genre_id"))
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
        jdbcTemplate.update(FilmQueries.DELETE, id);
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
    public Film save(Film film) {
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
    public List<Film> findPopularFilms(int count) {
        List<Film> films = jdbcTemplate.query(FilmQueries.FIND_POPULAR, filmRowMapper, count);
        films.forEach(this::loadGenresForFilm);
        return films;
    }

    private void loadGenresForFilms(List<Film> films) {
        List<Long> filmIds = films.stream().map(Film::getId).toList();

        String placeholders = String.join(",", Collections.nCopies(filmIds.size(), "?"));

        String query = String.format(
            "SELECT fg.film_id, g.genre_id, g.name " +
                "FROM film_genre fg " +
                "JOIN genre g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id IN (%s) " +
                "ORDER BY fg.film_id, g.genre_id",
            placeholders
        );

        Map<Long, Set<Genre>> filmGenresMap = new HashMap<>();

        jdbcTemplate.query(query, filmIds.toArray(), (rs, rowNum) -> {
            Long filmId = rs.getLong("film_id");
            Genre genre = Genre.builder().id(rs.getLong("genre_id")).name(rs.getString("name")).build();
            filmGenresMap.computeIfAbsent(filmId, k -> new HashSet<>()).add(genre);
            return null;
        });

        for (Film film : films) {
            Set<Genre> genres = filmGenresMap.getOrDefault(film.getId(), new HashSet<>());
            film.setGenres(genres);
        }
    }

    private void saveFilmGenres(Film film) {
        jdbcTemplate.update(FilmQueries.DELETE_GENRES, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Object[]> batchArgs = film.getGenres().stream()
                .map(genre -> new Object[]{film.getId(), genre.getId()})
                .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(FilmQueries.INSERT_GENRES, batchArgs);
        }
    }
}