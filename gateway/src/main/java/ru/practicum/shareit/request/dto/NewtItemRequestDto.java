package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewtItemRequestDto {
    @NotNull(message = "Описание запроса вещи не заполнено")
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
