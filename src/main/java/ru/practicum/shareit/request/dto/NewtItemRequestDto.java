package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class NewtItemRequestDto {
    @NotNull(message = "Описание запроса вещи не заполнено")
    private String description;
}
