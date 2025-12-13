package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class UserDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserDbStorage userStorage;
    private UserRowMapper userRowMapper;

    @BeforeEach
    void setUp() {
        userRowMapper = new UserRowMapper();
        userStorage = new UserDbStorage(jdbcTemplate, userRowMapper);
    }

    @Test
    void testFindAllUsers() {
        User user1 = createTestUser("user1@test.com", "user1");
        User user2 = createTestUser("user2@test.com", "user2");

        userStorage.save(user1);
        userStorage.save(user2);

        List<User> users = userStorage.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getLogin)
            .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void testFindUserById() {
        User user = createTestUser("test@test.com", "testuser");
        User savedUser = userStorage.save(user);

        Optional<User> foundUser = userStorage.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
        assertThat(foundUser.get().getLogin()).isEqualTo("testuser");
    }

    @Test
    void testSaveUser() {
        User user = createTestUser("newuser@test.com", "newuser");
        user.setName("Name");

        User savedUser = userStorage.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("newuser@test.com");
        assertThat(savedUser.getLogin()).isEqualTo("newuser");
        assertThat(savedUser.getName()).isEqualTo("Name");
    }

    @Test
    void testUpdateUser() {
        User user = createTestUser("original@test.com", "original");
        User savedUser = userStorage.save(user);

        savedUser.setEmail("updated@test.com");
        savedUser.setName("Updated Name");
        User updatedUser = userStorage.save(savedUser);

        assertThat(updatedUser.getEmail()).isEqualTo("updated@test.com");
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    @Test
    void testExistsById() {
        User user = createTestUser("test@test.com", "testuser");
        User savedUser = userStorage.save(user);

        assertThat(userStorage.existsById(savedUser.getId())).isTrue();
        assertThat(userStorage.existsById(999L)).isFalse();
    }

    @Test
    void testAddAndRemoveFriend() {
        User user1 = createTestUser("user1@test.com", "user1");
        User user2 = createTestUser("user2@test.com", "user2");

        User savedUser1 = userStorage.save(user1);
        User savedUser2 = userStorage.save(user2);

        userStorage.addFriend(savedUser1.getId(), savedUser2.getId());

        List<User> friends = userStorage.getFriends(savedUser1.getId());
        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getLogin()).isEqualTo("user2");

        userStorage.removeFriend(savedUser1.getId(), savedUser2.getId());

        List<User> friendsAfterRemoval = userStorage.getFriends(savedUser1.getId());
        assertThat(friendsAfterRemoval).isEmpty();
    }

    private User createTestUser(String email, String login) {
        return User.builder()
            .email(email)
            .login(login)
            .name("")
            .birthday(LocalDate.of(1990, 1, 1))
            .build();
    }
}