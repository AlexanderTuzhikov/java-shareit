package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateItemRequestDto {
    @NotNull(message = "ID запроса не заполнен")
    private Long id;
    private String description;

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

}
