package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class FilmDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private FilmDbStorage filmStorage;
    private MpaDbStorage mpaStorage;
    private GenreDbStorage genreStorage;
    private FilmRowMapper filmRowMapper;

    @BeforeEach
    void setUp() {
        filmRowMapper = new FilmRowMapper();
        mpaStorage = new MpaDbStorage(jdbcTemplate, new MpaRowMapper());
        genreStorage = new GenreDbStorage(jdbcTemplate, new GenreRowMapper());
        filmStorage = new FilmDbStorage(jdbcTemplate, filmRowMapper);
    }

    @Test
    void testFindAllFilms() {
        Film film1 = createTestFilm("Film 1", "Description 1");
        Film film2 = createTestFilm("Film 2", "Description 2");

        filmStorage.save(film1);
        filmStorage.save(film2);

        List<Film> films = filmStorage.findAll();

        assertThat(films).hasSize(2);
        assertThat(films).extracting(Film::getName)
            .containsExactlyInAnyOrder("Film 1", "Film 2");
    }

    @Test
    void testFindFilmById() {
        Film film = createTestFilm("Test Film", "Test Description");
        Film savedFilm = filmStorage.save(film);

        Optional<Film> foundFilm = filmStorage.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getName()).isEqualTo("Test Film");
        assertThat(foundFilm.get().getDescription()).isEqualTo("Test Description");
    }

    @Test
    void testSaveFilmWithGenres() {
        Film film = createTestFilm("Film with Genres", "Description");
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.builder().id(1L).build());
        genres.add(Genre.builder().id(2L).build());
        film.setGenres(genres);

        Film savedFilm = filmStorage.save(film);

        assertThat(savedFilm.getId()).isNotNull();
        assertThat(savedFilm.getGenres()).hasSize(2);
        assertThat(savedFilm.getGenres())
            .extracting(Genre::getId)
            .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testUpdateFilm() {
        Film film = createTestFilm("Original Name", "Original Description");
        Film savedFilm = filmStorage.save(film);

        savedFilm.setName("Updated Name");
        savedFilm.setDescription("Updated Description");
        Film updatedFilm = filmStorage.save(savedFilm);

        assertThat(updatedFilm.getName()).isEqualTo("Updated Name");
        assertThat(updatedFilm.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testExistsById() {
        Film film = createTestFilm("Test Film", "Test Description");
        Film savedFilm = filmStorage.save(film);

        assertThat(filmStorage.existsById(savedFilm.getId())).isTrue();
        assertThat(filmStorage.existsById(999L)).isFalse();
    }

    private Film createTestFilm(String name, String description) {
        return Film.builder()
            .name(name)
            .description(description)
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(120)
            .mpa(MpaRating.builder().id(1L).build())
            .build();
    }
}