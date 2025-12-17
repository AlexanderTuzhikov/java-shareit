package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewUserDto {
    @NotNull(message = "name is null")
    @NotBlank(message = "name is blank")
    private String name;
    @NotNull(message = "email is null")
    @NotBlank(message = "email is blank")
    @Email(message = "invalid email")
    private String email;
}
