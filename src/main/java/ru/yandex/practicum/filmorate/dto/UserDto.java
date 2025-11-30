package ru.yandex.practicum.filmorate.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Email должен быть валидным")
    @Pattern(regexp = ".+@.+\\..+", message = "Email должен быть в формате example@example.com")
    private String email;

    @NotBlank(message = "Логин обязателен", groups = ValidationException.class)
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы", groups = ValidationException.class)
    private String login;
    private String name;

    @NotNull(message = "Дата рождения не может быть пустой",groups = ValidationException.class)
    @PastOrPresent(message = "Дата рождения не может быть в будущем", groups = ValidationException.class)
    private LocalDate birthday;
    private Set<Long> friendsId;
}