package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {

	@Autowired
	private FilmController filmController;
	private Film film;

	@Test
	void shouldCreateAndGetFilm() {
		film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(120);

		Film createdFilm = filmController.create(film);
		assertNotNull(createdFilm.getId());
		assertEquals("Test Film", createdFilm.getName());
		List<Film> films = filmController.findAll();
		assertEquals(1, films.size());
	}

	@Test
	void shouldUpdateFilmSuccessfully() {
		film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(120);

		Film createdFilm = filmController.create(film);
		Film updatedFilm = new Film();
		updatedFilm.setId(createdFilm.getId());
		updatedFilm.setName("Updated Film");
		updatedFilm.setDescription("Updated Description");
		updatedFilm.setReleaseDate(LocalDate.of(2005, 1, 1));
		updatedFilm.setDuration(150);

		Film result = filmController.update(updatedFilm);
		assertEquals("Updated Film", result.getName());
		assertEquals("Updated Description", result.getDescription());
	}

	@Test
	void shouldThrowValidationExceptionForEarlyReleaseDate() {
		Film invalidFilm = new Film();
		invalidFilm.setName("Invalid Film");
		invalidFilm.setDescription("Invalid Description");
		invalidFilm.setReleaseDate(LocalDate.of(1890, 1, 1));
		invalidFilm.setDuration(100);

		ValidationException exception = assertThrows(ValidationException.class,
			() -> filmController.create(invalidFilm));

		assertEquals("Дата выпуска фильма не может быть раньше 28 декабря 1895 года", exception.getMessage());
	}
}
