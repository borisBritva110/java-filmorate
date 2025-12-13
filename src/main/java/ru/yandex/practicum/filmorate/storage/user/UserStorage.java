package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    boolean existsById(long id);

    void deleteById(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}