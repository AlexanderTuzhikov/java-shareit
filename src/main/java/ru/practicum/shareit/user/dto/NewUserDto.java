package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewUserDto {
    @NotNull(message = "Имя пользователя Null")
    @NotBlank(message = "Имя пользователя Blank")
    private String name;
    @NotNull(message = "Электронная почта Null")
    @NotBlank(message = "Электронная почта Blank")
    @Email(message = "Не верная электронная почта")
    private String email;
}
