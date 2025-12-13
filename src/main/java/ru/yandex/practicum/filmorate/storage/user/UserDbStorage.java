package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(UserQueries.FIND_ALL, userRowMapper);
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(UserQueries.FIND_BY_ID, userRowMapper, id);
        return users.stream().findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            Integer emailCount = jdbcTemplate.queryForObject(UserQueries.CHECK_EMAIL_EXISTS, Integer.class, user.getEmail());

            if (emailCount != null && emailCount > 0) {
                throw new ValidationException("Пользователь с email " + user.getEmail() + " уже существует");
            }

            Integer loginCount = jdbcTemplate.queryForObject(UserQueries.CHECK_LOGIN_EXISTS, Integer.class, user.getLogin());

            if (loginCount != null && loginCount > 0) {
                throw new ValidationException("Пользователь с логином " + user.getLogin() + " уже существует");
            }

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(UserQueries.INSERT, new String[]{"user_id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
                return ps;
            }, keyHolder);

            user.setId(keyHolder.getKey().longValue());
        } else {
            Integer emailCount = jdbcTemplate.queryForObject(UserQueries.CHECK_EMAIL_EXISTS_EXCLUDING_ID, Integer.class, user.getEmail(), user.getId());

            if (emailCount != null && emailCount > 0) {
                throw new ValidationException("Пользователь с email " + user.getEmail() + " уже существует");
            }

            Integer loginCount = jdbcTemplate.queryForObject(UserQueries.CHECK_LOGIN_EXISTS_EXCLUDING_ID, Integer.class, user.getLogin(), user.getId());

            if (loginCount != null && loginCount > 0) {
                throw new ValidationException("Пользователь с логином " + user.getLogin() + " уже существует");
            }

            jdbcTemplate.update(UserQueries.UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        }
        return user;
    }

    @Override
    public boolean existsById(long id) {
        Integer count = jdbcTemplate.queryForObject(UserQueries.EXISTS_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(UserQueries.DELETE, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (!existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        if (!existsById(friendId)) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден");
        }

        if (userId.equals(friendId)) {
            throw new ValidationException("Пользователь не может добавить себя в друзья");
        }

        jdbcTemplate.update(UserQueries.ADD_FRIEND, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(UserQueries.REMOVE_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(UserQueries.GET_FRIENDS, userRowMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        return jdbcTemplate.query(UserQueries.GET_COMMON_FRIENDS, userRowMapper, userId, otherId);
    }
}