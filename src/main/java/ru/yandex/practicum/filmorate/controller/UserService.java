package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserService {
    private long nextId = 1;
    private final Map<Long, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    public long getNextId() {
        return nextId++;
    }

    public User createUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        long userId = getNextId();
        newUser.setId(userId);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(User updatedUser) {
        if (updatedUser.getId() == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }
        if (!users.containsKey(updatedUser.getId())) {
            throw new ValidationException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }
        User existingUser = users.get(updatedUser.getId());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());
        return existingUser;
    }
}
