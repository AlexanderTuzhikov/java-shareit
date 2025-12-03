package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class NewItemDto {
    @NotNull(message = "Имя вещи Null")
    @NotBlank(message = "Имя вещи Blank")
    private String name;
    @NotNull(message = "Описание вещи Null")
    @NotBlank(message = "Описание вещи Blank")
    private String description;
    @NotNull(message = "Доступность к бронированию Null")
    private Boolean available;
    private long requestId;
}
