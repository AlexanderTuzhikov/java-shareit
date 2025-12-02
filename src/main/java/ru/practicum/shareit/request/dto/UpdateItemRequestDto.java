package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class UpdateItemRequestDto {
    @NotNull(message = "ID запроса не заполнен")
    private long id;
    private String description;

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

}
