package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldCreateAndGetUser() {
        User createdUser = userController.create(user);
        assertNotNull(createdUser.getId());
        assertEquals("testlogin", createdUser.getLogin());

        List<User> users = userController.findAll();
        assertEquals(1, users.size());
    }

    @Test
    void shouldSetLoginAsNameWhenNameIsEmpty() {
        User userWithoutName = new User();
        userWithoutName.setEmail("test2@mail.ru");
        userWithoutName.setLogin("testlogin2");
        userWithoutName.setName("");
        userWithoutName.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.create(userWithoutName);
        assertEquals("testlogin2", createdUser.getName());

        userWithoutName.setName(null);
        User createdUser2 = userController.create(userWithoutName);
        assertEquals("testlogin2", createdUser2.getName());
    }

    @Test
    void shouldThrowValidationExceptionForUpdateWithoutId() {
        User userWithoutId = new User();
        userWithoutId.setId(null);
        userWithoutId.setEmail("noid@mail.ru");
        userWithoutId.setLogin("noid");
        userWithoutId.setName("No ID");
        userWithoutId.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class,
            () -> userController.update(userWithoutId));

        assertEquals("ID пользователя должен быть указан", exception.getMessage());
    }

    @Test
    void shouldHandleMultipleUsers() {
        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1991, 2, 2));

        userController.create(user1);
        userController.create(user2);

        List<User> users = userController.findAll();
        assertEquals(2, users.size());
    }
}