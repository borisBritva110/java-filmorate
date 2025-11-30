package ru.yandex.practicum.filmorate.service.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return userStorage.findAll();
    }

    public UserDto getUserById(Long id) {
        return userStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
    }

    public UserDto createUser(UserDto newUser) {
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        validateUser(newUser);
        return userStorage.save(newUser);
    }

    public UserDto updateUser(UserDto updatedUser) {
        if (updatedUser.getId() == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }
        if (!userStorage.existsById(updatedUser.getId())) {
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }
        validateUser(updatedUser);
        return userStorage.save(updatedUser);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public List<UserDto> getCommonFriends(Long userId, Long otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }

    private void validateUser(UserDto user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}