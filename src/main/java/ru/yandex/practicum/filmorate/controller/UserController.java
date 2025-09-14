package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        log.info("Добавлен новый пользователь: {}", createdUser.getLogin());
        return createdUser;
    }

    @PutMapping
    public User update(@Valid@RequestBody User updatedUser) {
        User updated = userService.updateUser(updatedUser);
        log.info("Обновлен пользователь: {}", updated.getLogin());
        return updated;
    }
}
