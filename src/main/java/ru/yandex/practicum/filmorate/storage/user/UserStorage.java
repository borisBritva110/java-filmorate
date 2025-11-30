package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.dto.UserDto;

public interface UserStorage {
    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    UserDto save(UserDto user);
    boolean existsById(long id);
    void deleteById(Long id);
    void addFriend(Long userId, Long friendId);
    void removeFriend(Long userId, Long friendId);
    List<UserDto> getFriends(Long userId);
    List<UserDto> getCommonFriends(Long userId, Long otherId);
}