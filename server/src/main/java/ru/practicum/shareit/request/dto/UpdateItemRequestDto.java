package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class UpdateItemRequestDto {
    private Long id;
    private String description;

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

}
