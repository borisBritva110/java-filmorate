package ru.yandex.practicum.filmorate.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserService {
    private long nextId = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public long getNextId() {
        return nextId++;
    }

    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    public User createUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        long userId = getNextId();
        newUser.setId(userId);
        users.put(newUser.getId(), newUser);
        friends.put(userId, new HashSet<>());
        return newUser;
    }

    public User updateUser(User updatedUser) {
        if (updatedUser.getId() == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }
        if (!users.containsKey(updatedUser.getId())) {
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }
        User existingUser = users.get(updatedUser.getId());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());
        return existingUser;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null) {
            throw new ValidationException("Пользователь с ID " + userId + " не найден");
        }

        if (friend == null) {
            throw new ValidationException("Друг с ID " + friendId + " не найден");
        }

        friends.get(userId).add(friendId);
        friends.get(friendId).add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null) {
            throw new ValidationException("Пользователь с ID " + userId + " не найден");
        }

        if (friend == null) {
            throw new ValidationException("Друг с ID " + friendId + " не найден");
        }

        friends.get(userId).remove(friendId);
        friends.get(friendId).remove(userId);
    }

    public List<User> getFriends(Long userId) {
        getUserById(userId); // Проверка существования пользователя
        return friends.getOrDefault(userId, new HashSet<>()).stream()
            .map(this::getUserById)
            .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        getUserById(userId);
        getUserById(otherId);

        Set<Long> userFriends = friends.getOrDefault(userId, new HashSet<>());
        Set<Long> otherFriends = friends.getOrDefault(otherId, new HashSet<>());

        return userFriends.stream()
            .filter(otherFriends::contains)
            .map(this::getUserById)
            .collect(Collectors.toList());
    }
}
