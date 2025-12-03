package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotNull(message = "ID пользователя Null")
    private long id;
    private String name;
    @Email(message = "Не верная электронная почта")
    private String email;

    public boolean hasName() {
        return !(name == null);
    }

    public boolean hasEmail() {
        return !(email == null);
    }
}
