package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewItemDto {
    @NotNull(message = "description is null")
    @NotBlank(message = "description is blank")
    private String name;
    @NotNull(message = "description is null")
    @NotBlank(message = "description is blank")
    private String description;
    @NotNull(message = "available is null")
    private Boolean available;
    private Long requestId;
}
