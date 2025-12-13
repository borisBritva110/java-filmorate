package ru.yandex.practicum.filmorate.storage.user;

public final class UserQueries {

    public static final String FIND_ALL =
        "SELECT user_id, email, login, name, birthday FROM users";

    public static final String FIND_BY_ID =
        "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";

    public static final String INSERT =
        "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

    public static final String UPDATE =
        "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";

    public static final String DELETE =
        "DELETE FROM users WHERE user_id = ?";

    public static final String EXISTS_BY_ID =
        "SELECT COUNT(*) FROM users WHERE user_id = ?";

    public static final String CHECK_EMAIL_EXISTS =
        "SELECT COUNT(*) FROM users WHERE email = ?";

    public static final String CHECK_EMAIL_EXISTS_EXCLUDING_ID =
        "SELECT COUNT(*) FROM users WHERE email = ? AND user_id != ?";

    public static final String CHECK_LOGIN_EXISTS =
        "SELECT COUNT(*) FROM users WHERE login = ?";

    public static final String CHECK_LOGIN_EXISTS_EXCLUDING_ID =
        "SELECT COUNT(*) FROM users WHERE login = ? AND user_id != ?";

    public static final String ADD_FRIEND =
        "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";

    public static final String REMOVE_FRIEND =
        "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";

    public static final String GET_FRIENDS =
        "SELECT u.user_id, u.email, u.login, u.name, u.birthday FROM users u JOIN friendship f ON u.user_id = f.friend_id WHERE f.user_id = ?";

    public static final String GET_COMMON_FRIENDS =
        "SELECT u.user_id, u.email, u.login, u.name, u.birthday FROM users u " +
            "JOIN friendship f1 ON u.user_id = f1.friend_id " +
            "JOIN friendship f2 ON u.user_id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";

    private UserQueries() {
    }
}